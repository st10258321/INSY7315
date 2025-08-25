package vcmsa.projects.wil_hustlehub.View

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Adapters.BrowseServiceAdapter
import vcmsa.projects.wil_hustlehub.MainActivity
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
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
    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo)
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
            if(!users.isNullOrEmpty()) {
                usersLoaded =
                    users.associate { user -> (user.userID to user.name) as Pair<String, String> }
                setAdapter()
                Toast.makeText(requireContext(), "Users Loaded: ${users.size}", Toast.LENGTH_SHORT).show()
            }
        }
            userViewModel.allServices.observe(viewLifecycleOwner) { services ->
                if (!services.isNullOrEmpty()) {
                    //create adapter to display the services
                    servicesLoaded = services
                    setAdapter()
                    Toast.makeText(requireContext(), "Services Loaded: ${services.size}", Toast.LENGTH_SHORT).show()
                }
            }

    }
    private fun setAdapter() {
        if(usersLoaded.isNotEmpty() && servicesLoaded.isNotEmpty()) {
            val adapter = BrowseServiceAdapter(
                services = servicesLoaded,
                getUserName = { userID -> (usersLoaded[userID] ?: "Unknown User") },
                onViewProfileClick = { service ->
                        //navigate to booking page with the service id being pushed via INTENT
                        Toast.makeText(requireContext(), "Service Clicked: ${service.serviceName}", Toast.LENGTH_SHORT).show()
                        val fragment = BookServiceFragment()
                        val bundle = Bundle()
                        bundle.putString("serviceID", service.serviceId)
                        fragment.arguments = bundle
                        val mainActivity = requireActivity() as MainActivity
                        mainActivity.supportFragmentManager.beginTransaction()
                            .replace(mainActivity.binding.navHostFragment.id, fragment)
                            .addToBackStack(null)
                            .commit()
                })
            binding.serviceRecyclerView.adapter = adapter
            Toast.makeText(requireContext(), "Adapter Set", Toast.LENGTH_SHORT).show()
        }
    }
}