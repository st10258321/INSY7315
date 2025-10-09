package vcmsa.projects.wil_hustlehub.View

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Adapters.BrowseServiceAdapter
import vcmsa.projects.wil_hustlehub.MainActivity
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentBrowseServicesBinding
import kotlin.getValue
import android.widget.EditText
import androidx.core.widget.addTextChangedListener


class BrowseServicesFragment: Fragment() {
    // Declare the binding variable
    private var _binding: FragmentBrowseServicesBinding? = null
    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    private val chatRepo = ChatRepository()
    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)
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
    private var filteredServices = emptyList<Service>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.getAllUsers()
        userViewModel.getAllServices()
        //search bar functionality
        if(binding.searchBar.text.toString().isEmpty()){
            servicesLoaded
            setAdapter()
        }
        //searching the services using the search bar via serviceName
        binding.searchBar.addTextChangedListener { editable ->
            val search = editable.toString().trim()
            filteredServices = if(!search.isEmpty()){
                servicesLoaded.filter { service ->
                    service.serviceName.contains(search, ignoreCase = true)
                }
            }else{
                servicesLoaded
            }
            setAdapter()
        }

        binding.applyFiltersBtn.setOnClickListener {
            val appliedCategory = binding.categorySpinner.selectedItem.toString()
            val appliedLocation = binding.locationSpinner.selectedItem.toString()
           // val appliedRating = binding.ratingSpinner.selectedItem.toString()
            if(appliedCategory == "All" && appliedLocation == "Any"){
                filteredServices = servicesLoaded
            }else if(appliedCategory == "All") {
                filteredServices = servicesLoaded.filter { service ->
                    service.location == appliedLocation
                }
            } else if(appliedLocation == "Any") {
                filteredServices = servicesLoaded.filter { service ->
                    service.category == appliedCategory
                }
            }
            else {
                filteredServices = servicesLoaded.filter { service ->
                    service.category == appliedCategory
                    service.location == appliedLocation
                }
            }
            setAdapter()
            if(binding.filterContainer.isVisible){
                binding.filterContainer.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction { binding.filterContainer.isVisible = false }
                    .start()
                binding.toggleFilterBtn.text = "Show Filters"
            }
            Toast.makeText(requireContext(),"Applied Filters", Toast.LENGTH_SHORT).show()
        }

        binding.toggleFilterBtn.setOnClickListener {
            if(binding.filterContainer.isVisible){
                binding.filterContainer.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction { binding.filterContainer.isVisible = false }
                    .start()
                    binding.toggleFilterBtn.text = "Show Filters"
            }else{
                binding.filterContainer.alpha = 0f
                binding.filterContainer.isVisible = true
                binding.filterContainer.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .start()
                binding.toggleFilterBtn.text ="Hide Filters"
            }
        }


        userViewModel.combinedData.observe(viewLifecycleOwner) {
            if (it.first != null && it.second != null && filteredServices.isEmpty()) {
                usersLoaded = it.first!!
                servicesLoaded = it.second!!
                filteredServices = servicesLoaded
                setAdapter()
            }
        }


    }
    private fun setAdapter() {
        if(usersLoaded.isNotEmpty() && filteredServices.isNotEmpty()) {
            val adapter = BrowseServiceAdapter(
                services = filteredServices,
                getUserName = { userID -> (usersLoaded[userID] ?: "Unknown User") },
                onViewProfileClick = { service ->
                        //navigate to booking page with the service id being pushed via INTENT
                        val fragment = ProfileFragment()
                        val bundle = Bundle()
                        bundle.putString("serviceProfiderID", service.userId)
                        fragment.arguments = bundle
                        val mainActivity = requireActivity() as MainActivity
                        mainActivity.supportFragmentManager.beginTransaction()
                            .replace(mainActivity.binding.navHostFragment.id, fragment)
                            .addToBackStack(null)
                            .commit()
                },
                onBookServiceClick = {  service ->
                    //navigate to booking page with the service id being pushed via INTENT
                    val fragment = BookServiceFragment()
                    val bundle = Bundle()
                    bundle.putString("serviceID", service.serviceId)
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
}