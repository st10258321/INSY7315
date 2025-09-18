package vcmsa.projects.wil_hustlehub.View

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import vcmsa.projects.wil_hustlehub.Adapters.BrowseServiceAdapter
import vcmsa.projects.wil_hustlehub.MainActivity
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentBrowseServicesBinding
import kotlin.getValue



class BrowseServicesFragment: Fragment() {
    // Declare the binding variable
    private var _binding: FragmentBrowseServicesBinding? = null
    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo)
    private val userViewModel: UserViewModel by viewModels { viewModelFactory }

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment_browse_services.xml layout.
        _binding = FragmentBrowseServicesBinding.inflate(inflater, container, false)
        return binding.root
    }
    private var usersLoaded : Map<String,String> = emptyMap()
    private var servicesLoaded = emptyList<Service>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.getAllUsers()
        userViewModel.getAllServices()

        userViewModel.allUsers.observe(viewLifecycleOwner) { users ->
            usersLoaded = if (!users.isNullOrEmpty()) {
                users.associate { it.userID to it.name }
            } else {
                getDummyUsers()
            }
            trySetAdapter()
        }

        userViewModel.allServices.observe(viewLifecycleOwner) { services ->
            servicesLoaded = if (!services.isNullOrEmpty()) {
                services
            } else {
                getDummyServices()
            }
            trySetAdapter()
        }
    }

    private fun trySetAdapter() {
        if (usersLoaded.isNotEmpty() && servicesLoaded.isNotEmpty()) {
            val adapter = BrowseServiceAdapter(
                services = servicesLoaded,
                getUserName = { userID -> usersLoaded[userID] ?: "Unknown User" },
                onViewProfileClick = { service ->
                    val fragment = BookServiceFragment()
                    val bundle = Bundle().apply {
                        putString("serviceID", service.serviceId)
                    }
                    fragment.arguments = bundle
                    val mainActivity = requireActivity() as MainActivity
                    mainActivity.supportFragmentManager.beginTransaction()
                        .replace(mainActivity.binding.navHostFragment.id, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            )
            binding.serviceRecyclerView.adapter = adapter
        }
    }

    private fun getDummyUsers(): Map<String, String> = mapOf(
        "user1" to "Jane Doe",
        "user2" to "Michael Mokoena",
        "user3" to "Thandi Nkosi"
    )

    private fun getDummyServices(): List<Service> = listOf(
        Service(
            serviceId = "s1",
            userId = "user1",
            serviceName = "Maths & Physics Tutoring",
            description = "Experienced final year student offering tutoring for Calculus, Algebra, and Physics. Online and in-person sessions available.",
            price = 150.0
        ),
        Service(
            serviceId = "s2",
            userId = "user2",
            serviceName = "Graphic Design & Branding",
            description = "Professional logos, social media kits, and brand identity packages tailored to your business.",
            price = 250.0
        ),
        Service(
            serviceId = "s3",
            userId = "user3",
            serviceName = "Mobile App Development",
            description = "Custom Android apps built with modern UI/UX and Firebase integration. Ideal for startups and small businesses.",
            price = 500.0
        )
    )
}