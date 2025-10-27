package vcmsa.projects.wil_hustlehub.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import vcmsa.projects.wil_hustlehub.MainActivity
import vcmsa.projects.wil_hustlehub.Repository.*
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentLoginBinding
import vcmsa.projects.wil_hustlehub.R

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 1001

    private val userViewModel: UserViewModel by viewModels {
        ViewModelFactory(
            UserRepository(),
            ServiceRepository(),
            BookServiceRepository(),
            ReviewRepository(),
            ChatRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSignUpPrompt.setOnClickListener {
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }


        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // <- ensure this matches your strings.xml
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Google Sign-In button click
        binding.googleSignInBtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        // Handle normal login button click
        val email = binding.loginEmail
        val password = binding.loginPassword

        binding.btnLoginSubmit.setOnClickListener {
            if (email.text.isNullOrEmpty() || password.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                userViewModel.login(email.text.toString(), password.text.toString())
            }
        }

        // Observe login status
        userViewModel.loginStat.observe(viewLifecycleOwner) { (success, message) ->
            if (success) {

                Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                Log.d("emailCheck", email.text.toString())
                if(email.text.toString() == "admin001@gmail.com") {
                    Log.d("--checking--", "admin login")
                    val intent = Intent(requireContext(), admin_portal::class.java)
                    startActivity(intent)
                }else{
                    val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("uid", message)
                    Log.d("--checking--", "$message")
                    editor.apply()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }

            } else {
                Toast.makeText(requireContext(), message ?: "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(Exception::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    // TODO: handle Firebase Auth with idToken
                    Log.d("GoogleSignIn", "ID Token: $idToken")
                    Log.d("--account email", account.email.toString())
                    userViewModel.googleLogin(idToken)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
