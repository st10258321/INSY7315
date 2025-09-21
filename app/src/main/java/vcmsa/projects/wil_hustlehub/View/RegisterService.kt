package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = FragmentRegisterServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actvCategory.setAdapter(ArrayAdapter.createFromResource(this, R.array.service_categories, android.R.layout.simple_spinner_item))
        binding.actvLocation.setAdapter(ArrayAdapter.createFromResource(this, R.array.service_locations, android.R.layout.simple_spinner_item))
        binding.actvPricingType.setAdapter(ArrayAdapter.createFromResource(this, R.array.pricing_types, android.R.layout.simple_spinner_item))


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
}