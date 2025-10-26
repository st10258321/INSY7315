package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import vcmsa.projects.wil_hustlehub.Adapter.ReviewAdapter
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentReviewsBinding
import kotlin.getValue

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!


    // Store the serviceId you’re showing reviews for
    private lateinit var serviceProviderId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
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

        // Get serviceProviderId from arguments (or ViewModel/session)
        serviceProviderId = arguments?.getString("serviceId") ?: "defaultServiceId"

        // Back button
        binding.addPetBackButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }


        val recyclerView = binding.reviewsRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ReviewAdapter(emptyList())
        recyclerView.adapter = adapter


        // Load reviews
        userViewModel.getReviewsForServiceProvider(serviceProviderId)
        Log.d("--check-reviews", "serviceProviderId: $serviceProviderId")
        userViewModel.serviceProviderReviews.observe(viewLifecycleOwner) { reviews ->
            if(reviews != null ) {
                adapter.updateReviews(reviews)
                Log.d("-countReviews", "${reviews.size}")
            }else if( reviews?.size == 0){
                binding.reviewStatusText.text = "No reviews yet"
            }
            //Toast.makeText(requireContext(), "Reviews: $reviews", Toast.LENGTH_SHORT).show()
        }
        userViewModel.getServiceProviderAverageRating(serviceProviderId)
        userViewModel.averageRating.observe(viewLifecycleOwner) { (avg, total) ->
            binding.ratingValue.text = avg?.toString() ?: "-"
            binding.ratingStars.rating = avg?.toFloat() ?: 0f
            binding.ratingCount.text = "Based on ${total ?: 0} reviews"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}