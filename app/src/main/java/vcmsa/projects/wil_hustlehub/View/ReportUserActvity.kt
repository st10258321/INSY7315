package vcmsa.projects.wil_hustlehub.View

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import vcmsa.projects.wil_hustlehub.MainActivity
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.databinding.ActivityReportUserActvityBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportUserActvity : AppCompatActivity() {
    val userRepo = UserRepository()
    val serviceRepo = ServiceRepository()
    val bookRepo = BookServiceRepository()
    val reviewRepo = ReviewRepository()
    val chatRepo = ChatRepository()
    private lateinit var binding: ActivityReportUserActvityBinding
    private var servicesOffered:  List<ServiceIdAndName>? = null
    val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo,reviewRepo,chatRepo)
    private var spUsername : String = ""



    private var imageString : String = ""
    private var photoUri : Uri? = null
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val denied = permissions.filter { !it.value }.keys
            if (denied.isEmpty()) {
                // All requested permissions granted
                openCameraOrGallery()
            } else {
                // Some permissions denied
                Toast.makeText(this, "Permissions required to continue: $denied", Toast.LENGTH_SHORT).show()
            }
        }
    val userViewModel: UserViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportUserActvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serviceProId = intent.getStringExtra("serviceProId")
        userViewModel.getMyServices(serviceProId!!)
        userViewModel.userServices.observe(this) { services ->
            servicesOffered = services?.map {
                ServiceIdAndName(it.serviceId, it.serviceName)
            }
            if (!servicesOffered.isNullOrEmpty()) {
                val adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, servicesOffered!!)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.servicesOfferedSpinner.adapter = adapter
            }
        }


        userViewModel.getUserData(serviceProId).observe(this) { user ->
            if(user != null) {
                binding.spUsername.text = "Service Provider Name: ${user.name}"
                spUsername = user.name
                binding.spDateJoined.text = "Date Joined HustleHub: ${user.createdDate}"
                binding.servicesOffered.text = "Services Offered: ${servicesOffered?.joinToString(", ")}"
            }
        }
        binding.btnUploadImage.setOnClickListener{
            checkPermissionsAndProceed()
        }

        binding.btnSubmitComplaint.setOnClickListener {
            val selected = binding.servicesOfferedSpinner.selectedItem as ServiceIdAndName
            val selectedid = selected.serviceId
            val reportedIssue = binding.complaintSpinner.selectedItem.toString()
            val additionalNotes = binding.edAdditionalNotes.text.toString()
            userViewModel.reportServiceProvider(serviceProId,spUsername,selectedid,reportedIssue,additionalNotes,imageString)
        }
        userViewModel.reportResult.observe(this){(success, message) ->
            if(success){
                Toast.makeText(this, "Complaint submitted successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun checkPermissionsAndProceed() {
        val permissionsToRequest = mutableListOf<String>()

        // Camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        // Notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }else{
            openCameraOrGallery()
        }
    }
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){
            success:Boolean ->
        if(success){
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show()
            photoUri?.let { uri ->
                imageString = uri.toString()
            }
        }else{
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCameraOrGallery() {
        val photoFile : File? = try{
            createImageFile()
        }catch (e: IOException) {
            Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show()
            null

        }
        photoFile?.also{
            photoUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileprovider", it)
            cameraLauncher.launch(photoUri!!)
        }

    }

    private fun createImageFile(): File{
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir : File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)

    }

     data class ServiceIdAndName(
        val serviceId :String,
        val serviceName : String
    ){
         override fun toString():String {
             return serviceName
         }
     }
}