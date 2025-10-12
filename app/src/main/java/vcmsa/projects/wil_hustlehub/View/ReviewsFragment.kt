package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.databinding.FragmentReviewsBinding
import kotlin.getValue

class ReviewsFragment : Fragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()
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

        // Get serviceProviderId from arguments (or ViewModel/session)
        serviceProviderId = arguments?.getString("serviceId") ?: "defaultServiceId"

        // Back button
        binding.addPetBackButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Add Review button → now uses actual serviceProviderId
        binding.addReviewButton.setOnClickListener {
            val addReviewFragment = AddReviewFragment.newInstance(serviceProviderId)
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, addReviewFragment)
                .addToBackStack(null)
                .commit()
        }

        // Profile nav item
        binding.profileNavItem.setOnClickListener {
            val profileFragment = ProfileFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, profileFragment)
                .addToBackStack(null)
                .commit()
        }
        // Load reviews
        userViewModel.getReviewsForServiceProvider(serviceProviderId)

        userViewModel.serviceProviderReviews.observe(viewLifecycleOwner) { reviews ->
            if (reviews != null) {
                binding.ratingCount.text = "Based on ${reviews.size} reviews"
            } else {
                binding.ratingCount.text = "No reviews yet"
            }
        }

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