package vcmsa.projects.wil_hustlehub.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import vcmsa.projects.wil_hustlehub.Model.User
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentRegisterBinding
import android.widget.RadioGroup
import vcmsa.projects.wil_hustlehub.R
import com.google.android.material.textfield.TextInputEditText
class RegisterFragment: Fragment() {
    // Declare the binding variable
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment_register.xml layout.
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_register, container, false)
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

        binding.tvLoginPrompt.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        binding.radioServiceProvider.setOnClickListener {
            binding.aboutMeLayout.visibility = View.VISIBLE
        }

        binding.radioCustomer.setOnClickListener {
            binding.aboutMeLayout.visibility = View.GONE
        }

            val radioGroupUserRole = view.findViewById<RadioGroup>(R.id.radioGroupUserRole)
            val aboutMeLayout = view.findViewById<TextInputEditText>(R.id.registerAboutMe)

            binding.radioGroupUserRole.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radioServiceProvider -> {
                        aboutMeLayout.visibility = View.VISIBLE
                    }
                    R.id.radioCustomer -> {
                        aboutMeLayout.visibility = View.GONE
                    }
                }
            }






        binding.btnRegisterSubmit.setOnClickListener {
            val user = User(
                userID = "",
                name = binding.registerName.text.toString(),
                email = binding.registerEmail.text.toString(),
                phoneNumber = binding.registerCellNumber.text.toString(),
                password = binding.registerPassword.text.toString(),
                aboutMe = binding.registerAboutMe.text.toString(),
                createdDate = ""
            )
            userViewModel.register(user)
        }
        userViewModel.registrationStat.observe(viewLifecycleOwner) {(success, message, registeredUser) ->
            if (success) {
            Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_SHORT).show()
                val loginFragment = LoginFragment()
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, loginFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }else{
                Toast.makeText(requireContext(), "Registration Failed:  ${message}", Toast.LENGTH_SHORT).show()
            }
        }



    }
}