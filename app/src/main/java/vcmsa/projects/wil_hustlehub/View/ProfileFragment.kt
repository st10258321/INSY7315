package vcmsa.projects.wil_hustlehub.View

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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



        try{
            //first check if this page was navigated from the browser service page,if not display the logged in user data
            serviceProId = arguments?.getString("serviceProfiderID")
            if(!serviceProId.isNullOrEmpty()) {
                userViewModel.getUserData(serviceProId!!).observe(viewLifecycleOwner) { user ->
                    binding.providerName.text = user?.name
                }
            }else{
                try{

                    if(userid != null) {
                        userViewModel.getUserData(userid).observe(viewLifecycleOwner) { user ->
                            binding.providerName.text = user?.name
                        }
                    }
                }catch(e : Exception){
                    Log.d("ProfileFragment- Logged in User error", e.message.toString())
                }
            }
        }catch(e : Exception){
            Log.d("ProfiileFragment- Service Provider error", e.message.toString())
        }



    }
}