package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
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
import vcmsa.projects.wil_hustlehub.databinding.ActivityReportUserActvityBinding

class ReportUserActvity : AppCompatActivity() {
    val userRepo = UserRepository()
    val serviceRepo = ServiceRepository()
    val bookRepo = BookServiceRepository()
    private lateinit var binding: ActivityReportUserActvityBinding
    private var servicesOffered:  List<String>? = null
    val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo)
    val userViewModel: UserViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportUserActvityBinding.inflate(layoutInflater)


        val serviceProId = intent.getStringExtra("serviceProId")
        userViewModel.getMyServices(serviceProId!!)
        userViewModel.userServices.observe(this) { services ->
            servicesOffered = services?.map { it.serviceId }
        }



        userViewModel.getUserData(serviceProId).observe(this) { user ->
            if(user != null) {
                binding.spUsername.text = user.name
                binding.spDateJoined.text = user.createdDate
                binding.servicesOffered.text = servicesOffered?.joinToString(", ")
            }
        }


    }
}