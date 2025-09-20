package vcmsa.projects.wil_hustlehub.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import vcmsa.projects.wil_hustlehub.Adapters.ProfileServiceAdapter
import vcmsa.projects.wil_hustlehub.MainActivity
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentProfileBinding
import kotlin.getValue

class ProfileFragment: Fragment() {
    private var serviceProId : String? = null
    // Declare the binding variable
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment_profile.xml layout.
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userRepo = UserRepository()
        val serviceRepo = ServiceRepository()
        val bookRepo = BookServiceRepository()
        val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo)
        val userViewModel: UserViewModel by viewModels { viewModelFactory }
        //get user data from the view model
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userid = sharedPreferences.getString("uid", null)
        serviceProId = arguments?.getString("serviceProfiderID")
        val isOwner = serviceProId.isNullOrEmpty() || serviceProId == userid

        binding.reportUserLayout.isVisible = !isOwner

        val adapter = ProfileServiceAdapter(emptyList(),
            onBookServiceClick = { service ->
            val fragment = BookServiceFragment()
            val bundle = Bundle()
            bundle.putString("serviceID", service.serviceId)
            fragment.arguments = bundle
            val mainActivity = requireActivity() as MainActivity
            mainActivity.supportFragmentManager.beginTransaction()
                .replace(mainActivity.binding.navHostFragment.id, fragment)
                .addToBackStack(null)
                .commit()
        },
            isOwner = isOwner
        )
        binding.profileServicesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.profileServicesRecycler.adapter = adapter

        try{
            //first check if this page was navigated from the browser service page,if not display the logged in user data

            if(!serviceProId.isNullOrEmpty()) {
                userViewModel.getMyServices(serviceProId!!)
                userViewModel.getUserData(serviceProId!!).observe(viewLifecycleOwner) { user ->
                    //fill the page with the service providers information as well as the report user section
                    binding.providerName.text = user?.name

                    userViewModel.userServices.observe(viewLifecycleOwner){services ->
                            adapter.updateServices(services ?: emptyList())
                    }
                    binding.profileServicesRecycler.adapter = adapter
                }
            }else{
                if(userid != null) {
                    userViewModel.getMyServices(userid)
                    userViewModel.getUserData(userid).observe(viewLifecycleOwner) { user ->
                        //fill the page with the user's section, check if they have any services they provide and fill that section too.
                        binding.providerName.text = user?.name

                        userViewModel.userServices.observe(viewLifecycleOwner) { services ->
                            adapter.updateServices(services ?: emptyList())
                        }
                        binding.profileServicesRecycler.adapter = adapter
                    }
                }
            }
        }catch(e : Exception){
            Log.d("ProfiileFragment- Service Provider error", e.message.toString())
        }

        //report user action
        binding.reportUserBtn.setOnClickListener {
            val intent = Intent(requireContext(), ReportUserActvity::class.java)
            intent.putExtra("serviceProId", serviceProId)
            startActivity(intent)
        }


    }
}