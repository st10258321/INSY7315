package vcmsa.projects.wil_hustlehub.View

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
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
    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
            val storageGranted = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    (permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false) &&
                            (permissions[Manifest.permission.READ_MEDIA_VIDEO] ?: false)
                }
                else -> permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
            }
            val notificationsGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions[Manifest.permission.POST_NOTIFICATIONS] ?: false
            } else true

            if (cameraGranted && storageGranted && notificationsGranted) {
                openCameraOrGallery()
            } else {
                Toast.makeText(this, "Permissions required to continue", Toast.LENGTH_SHORT).show()
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
            val location = binding.actvLocation.text.toString()
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

                userViewModel.addService(serviceName, category, description, price, "", days, times, location)
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

        // Storage / media
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
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
        } else {
            openCameraOrGallery()
        }
    }

    private fun openCameraOrGallery() {
        // TODO: Launch your camera or gallery logic
        Toast.makeText(this, "Ready to pick image or take photo", Toast.LENGTH_SHORT).show()
    }
}