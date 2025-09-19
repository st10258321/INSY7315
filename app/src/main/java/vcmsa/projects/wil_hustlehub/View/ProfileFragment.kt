package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        userViewModel.currentUserData.observe(viewLifecycleOwner){ user ->
            binding.providerName.text = user?.email
        }


    }
}