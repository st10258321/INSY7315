package vcmsa.projects.wil_hustlehub.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import vcmsa.projects.wil_hustlehub.MainActivity
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentLoginBinding
import kotlin.getValue

class LoginFragment: Fragment() {
    // Declare the binding variable
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment_login.xml layout.
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val userRepo = UserRepository()
        val serviceRepo = ServiceRepository()
        val bookRepo = BookServiceRepository()
        val reviewRepo = ReviewRepository()
        val chatRepo = ChatRepository()
        val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)
        val userViewModel: UserViewModel by viewModels { viewModelFactory }
        // Directs the user to the register page
        binding.tvSignUpPrompt.setOnClickListener {
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }

        val loginButton = binding.btnLoginSubmit
        val email = binding.loginEmail
        val password = binding.loginPassword
        loginButton.setOnClickListener {
            if(email.text.toString().isEmpty() || password.text.toString().isEmpty()){
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }else {
                userViewModel.login(email.text.toString(), password.text.toString())
            }

        }
        userViewModel.loginStat.observe(viewLifecycleOwner) { (success, message) ->
            if (success) {
                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}