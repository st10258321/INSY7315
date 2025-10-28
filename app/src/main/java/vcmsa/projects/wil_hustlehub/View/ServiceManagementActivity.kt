package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Adapters.ServiceAdapter
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.*
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import android.widget.ImageView

class ServiceManagementActivity : AppCompatActivity() {
    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    private val chatRepo = ChatRepository()

    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)
    private val userViewModel: UserViewModel by viewModels { viewModelFactory }

    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceAdapter: ServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_service_management)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val addPetBackButton = findViewById<ImageView>(R.id.add_pet_back_button)
        addPetBackButton.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewServices)
        recyclerView.layoutManager = LinearLayoutManager(this)

        serviceAdapter = ServiceAdapter(emptyList()) { service ->
            userViewModel.deleteService(service.serviceId)
        }

        recyclerView.adapter = serviceAdapter

        userViewModel.getAllServices()

        userViewModel.allServices.observe(this) { services ->
            if (services != null) {
                serviceAdapter.updateList(services)
            }
        }
        userViewModel.serviceStatus.observe(this) { (success, message) ->
            if(success){
                Toast.makeText(this, "Service deleted successfully", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Failed to delete service: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
