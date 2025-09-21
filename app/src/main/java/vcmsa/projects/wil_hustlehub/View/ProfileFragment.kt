package vcmsa.projects.wil_hustlehub.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import vcmsa.projects.wil_hustlehub.Model.User
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentProfileBinding
import kotlin.getValue

class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    private val chatRepo = ChatRepository()
    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)
    private val userViewModel: UserViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch user data from ViewModel
        var currentUser: User? = null

        userViewModel.currentUserData.observe(viewLifecycleOwner) { user ->
            currentUser = user
            binding.providerName.text = user?.name
        }

        // Configure the chat button
        binding.messageProviderBtn.setOnClickListener {
            val dummyProviderId = "provider_dummy_123"
            val chatFragment = ChatFragment().apply {
                arguments = Bundle().apply {
                    putString("providerId", dummyProviderId)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, chatFragment)
                .addToBackStack(null)
                .commit()
        }



        userViewModel.singleChat.observe(viewLifecycleOwner) { chat ->
            chat?.let {
                val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                    putExtra("chatId", it.chatId)
                }
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}