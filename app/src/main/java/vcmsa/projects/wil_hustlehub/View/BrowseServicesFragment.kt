package vcmsa.projects.wil_hustlehub.View

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
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



class BrowseServicesFragment : Fragment() {
    // Declare the binding variable
    private var _binding: FragmentBrowseServicesBinding? = null
    private val binding get() = _binding!!

    // Repositories
    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    private val chatRepo = ChatRepository()

    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)
    private val userViewModel: UserViewModel by viewModels { viewModelFactory }

    // In-memory cache
    private var usersLoaded: Map<String, String> = emptyMap()
    private var servicesLoaded: List<Service> = emptyList()

    // Cache keys
    private val PREF_NAME = "BrowseCache"
    private val USERS_KEY = "users_cache"
    private val SERVICES_KEY = "services_cache"
    private val CACHE_EXPIRY_KEY = "cache_expiry"

    // Cache expiry: 10 minutes
    private val CACHE_DURATION = 10 * 60 * 1000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowseServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Try to load cached data first
        loadFromCache()

        if (usersLoaded.isEmpty() || servicesLoaded.isEmpty()) {
            // Fetch from network if cache empty or expired
            userViewModel.getAllUsers()
            userViewModel.getAllServices()
        }

        // Observe ViewModel data
        userViewModel.combinedData.observe(viewLifecycleOwner) {
            if (it.first != null && it.second != null) {
                usersLoaded = it.first!!
                servicesLoaded = it.second!!
                saveToCache() // Save fresh data to cache
                setAdapter()
            }
        }

        binding.toggleFilterBtn.setOnClickListener {
            toggleFilters()
        }
    }

    // CACHE HANDLERS
    private fun loadFromCache() {
        val prefs = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val lastCacheTime = prefs.getLong(CACHE_EXPIRY_KEY, 0L)
        val now = System.currentTimeMillis()

        if (now - lastCacheTime > CACHE_DURATION) {
            // Cache expired
            prefs.edit().clear().apply()
            return
        }

        val usersJson = prefs.getString(USERS_KEY, null)
        val servicesJson = prefs.getString(SERVICES_KEY, null)

        if (!usersJson.isNullOrEmpty() && !servicesJson.isNullOrEmpty()) {
            try {
                val gson = Gson()
                val userType = object : TypeToken<Map<String, String>>() {}.type
                val serviceType = object : TypeToken<List<Service>>() {}.type

                usersLoaded = gson.fromJson(usersJson, userType)
                servicesLoaded = gson.fromJson(servicesJson, serviceType)

                if (usersLoaded.isNotEmpty() && servicesLoaded.isNotEmpty()) {
                    setAdapter()
                }
            } catch (e: Exception) {
                Log.e("Cache", "Error loading cache: ${e.message}")
            }
        }
    }

    private fun saveToCache() {
        try {
            val gson = Gson()
            val prefs = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            prefs.edit()
                .putString(USERS_KEY, gson.toJson(usersLoaded))
                .putString(SERVICES_KEY, gson.toJson(servicesLoaded))
                .putLong(CACHE_EXPIRY_KEY, System.currentTimeMillis())
                .apply()
        } catch (e: Exception) {
            Log.e("Cache", "Error saving cache: ${e.message}")
        }
    }

    // UI HANDLERS
    private fun toggleFilters() {
        if (binding.filterContainer.isVisible) {
            binding.filterContainer.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction { binding.filterContainer.isVisible = false }
                .start()
            binding.toggleFilterBtn.text = "Show Filters"
        } else {
            binding.filterContainer.alpha = 0f
            binding.filterContainer.isVisible = true
            binding.filterContainer.animate()
                .alpha(1f)
                .setDuration(200)
                .start()
            binding.toggleFilterBtn.text = "Hide Filters"
        }
    }

    private fun setAdapter() {
        if (usersLoaded.isNotEmpty() && servicesLoaded.isNotEmpty()) {
            val adapter = BrowseServiceAdapter(
                services = servicesLoaded,
                getUserName = { userID -> usersLoaded[userID] ?: "Unknown User" },
                onViewProfileClick = { service ->
                    val fragment = ProfileFragment().apply {
                        arguments = Bundle().apply {
                            putString("serviceProfiderID", service.userId)
                        }
                    }
                    val mainActivity = requireActivity() as MainActivity
                    mainActivity.supportFragmentManager.beginTransaction()
                        .replace(mainActivity.binding.navHostFragment.id, fragment)
                        .addToBackStack(null)
                        .commit()
                },
                onBookServiceClick = { service ->
                    val fragment = BookServiceFragment().apply {
                        arguments = Bundle().apply {
                            putString("serviceID", service.serviceId)
                        }
                    }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}