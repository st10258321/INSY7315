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
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.databinding.ActivityReportUserActvityBinding

class ReportUserActvity : AppCompatActivity() {
    val userRepo = UserRepository()
    val serviceRepo = ServiceRepository()
    val bookRepo = BookServiceRepository()
    val reviewRepo = ReviewRepository()
    val chatRepo = ChatRepository()
    private lateinit var binding: ActivityReportUserActvityBinding
    private var servicesOffered:  List<String>? = null
    val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo,reviewRepo,chatRepo)
    val userViewModel: UserViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportUserActvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val serviceProId = intent.getStringExtra("serviceProId")
        userViewModel.getMyServices(serviceProId!!)
        userViewModel.userServices.observe(this) { services ->
            servicesOffered = services?.map { it.serviceName }
        }



        userViewModel.getUserData(serviceProId).observe(this) { user ->
            if(user != null) {
                binding.spUsername.text = "Service Provider Name: ${user.name}"
                binding.spDateJoined.text = "Date Joined HustleHub: ${user.createdDate}"
                binding.servicesOffered.text = "Services Offered: ${servicesOffered?.joinToString(", ")}"
            }
        }


    }
}