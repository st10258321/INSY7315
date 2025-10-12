package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentAddReviewBinding
import kotlin.getValue

class AddReviewFragment : Fragment() {

    private var _binding: FragmentAddReviewBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()

    // You’ll pass this in from ReviewsFragment
    //private var serviceId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userRepo = UserRepository()
        val serviceRepo = ServiceRepository()
        val bookRepo = BookServiceRepository()
        val reviewRepo = ReviewRepository()
        val chatRepo = ChatRepository()

        val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo,reviewRepo,chatRepo)
        val userViewModel: UserViewModel by viewModels { viewModelFactory }


        val serviceId = arguments?.getString("serviceID")
        val serviceName = arguments?.getString("serviceName")

        binding.txtReviewTitle.text = "Review $serviceName"

        binding.addReviewRating.setOnRatingBarChangeListener { _, rating, _ ->
            binding.saveReviewBtn.isEnabled = rating > 0
        }


        Log.d("add-review--", "serviceId: $serviceId")
        // Save review
        binding.saveReviewBtn.setOnClickListener {
            val rating = binding.addReviewRating.rating.toInt()
            val reviewText = binding.addReviewText.text.toString().trim()

            if (serviceId.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Service not found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (rating == 0 || reviewText.isEmpty()) {
                Toast.makeText(requireContext(), "Please add rating and review", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userViewModel.addReview(serviceId!!, rating, reviewText)
        }

        // Observe status
        userViewModel.reviewStatus.observe(viewLifecycleOwner) { (success, message) ->
            if (success) {
                Toast.makeText(requireContext(), "Review added ✅", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack() // return to ReviewsFragment
            } else {
                Toast.makeText(requireContext(), "Failed: $message", Toast.LENGTH_SHORT).show()
            }
        }

        // Cancel button
        binding.cancelReviewBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_SERVICE_ID = "serviceId"

        fun newInstance(serviceId: String) = AddReviewFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_SERVICE_ID, serviceId)
            }
        }
    }
}