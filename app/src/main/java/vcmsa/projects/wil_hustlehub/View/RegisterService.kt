package vcmsa.projects.wil_hustlehub.View

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentRegisterServiceBinding
import kotlin.getValue
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterService : AppCompatActivity() {
    private lateinit var binding: FragmentRegisterServiceBinding
    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    private val chatRepo = ChatRepository()
    private val userViewModel : UserViewModel by viewModels {
        ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)
    }
    private var imageString : String = ""
    private var photoUri : Uri? = null
    private val CAMERA_REQUEST_CODE = 100
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = FragmentRegisterServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actvCategory.setAdapter(ArrayAdapter.createFromResource(this, R.array.service_categories, android.R.layout.simple_spinner_item))
        binding.actvLocation.setAdapter(ArrayAdapter.createFromResource(this, R.array.service_locations, android.R.layout.simple_spinner_item))
        binding.actvPricingType.setAdapter(ArrayAdapter.createFromResource(this, R.array.pricing_types, android.R.layout.simple_spinner_item))



        binding.btnUploadImages.setOnClickListener {
            checkPermissionsAndProceed()
        }


        binding.btnSubmitService.setOnClickListener {
            val serviceName = binding.etServiceTitle.text.toString()
            val category = binding.actvCategory.text.toString()
            val description = binding.etServiceDescription.text.toString()
            val price = binding.etServicePrice.text.toString()
            val availability = binding.etAvailability.text.toString()
            val location = binding.actvLocation.selectedItem.toString()
            if(serviceName.isEmpty()|| category.isEmpty() || description.isEmpty() || price.isEmpty()){
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }else{
                val price = price.toDouble()
                val availabilityInput = binding.etAvailability.text.toString()
                // Split the availability input into days and times
                // Example input: "Mon,Tue,Wed | 09:00-17:00"
                val parts = availabilityInput.split("|")
                val days = parts.getOrNull(0)?.split(",") ?: listOf()
                val times = parts.getOrNull(1)?.split(",") ?: listOf()

                userViewModel.addService(serviceName, category, description, price, imageString, days, times, location)
            }
        }
        userViewModel.serviceStatus.observe(this) { (success, message) ->
            if (success) {
                Toast.makeText(this, "Service added successfully", Toast.LENGTH_SHORT).show()
            } else {
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
}