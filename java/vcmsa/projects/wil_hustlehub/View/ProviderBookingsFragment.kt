package vcmsa.projects.wil_hustlehub.View


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import vcmsa.projects.wil_hustlehub.Adapters.ProviderBookingsAdapter
import vcmsa.projects.wil_hustlehub.Model.BookService
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentProviderBookingsBinding

class ProviderBookingsFragment : Fragment() {

    private var _binding: FragmentProviderBookingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProviderBookingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProviderBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ProviderBookingsFragment", "onViewCreated called")

        val userRepo = UserRepository()
        val serviceRepo = ServiceRepository()
        val bookRepo = BookServiceRepository()
        val reviewRepo = ReviewRepository()
        val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo)
        val userViewModel: UserViewModel by viewModels { viewModelFactory }

        adapter = ProviderBookingsAdapter(
            bookings = mutableListOf(),
            onConfirmAction = { booking ->
                Log.d("ProviderBookingsFragment", "Confirming booking: ${booking.bookingId}")
                userViewModel.confirmBooking(booking.bookingId)
            },
            onRejectAction = { booking ->
                Log.d("ProviderBookingsFragment", "Rejecting booking: ${booking.bookingId}")
                userViewModel.rejectBooking(booking.bookingId)
            }
        )

        binding.recyclerViewBookings.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBookings.adapter = adapter

        userViewModel.bookingsForMyServices.observe(viewLifecycleOwner) { bookings ->
            if (bookings.isNullOrEmpty()) {
                // Only show dummy bookings when LiveData is empty
                Log.d("ProviderBookingsFragment", "No real bookings, showing dummy")
                loadDummyBookings()
                Toast.makeText(requireContext(), getString(R.string.toast_no_bookings), Toast.LENGTH_SHORT).show()
            } else {
                Log.d("ProviderBookingsFragment", "Bookings received: ${bookings.size}")
                adapter.updateBookings(bookings)
                Toast.makeText(requireContext(), getString(R.string.toast_bookings_fetched, bookings.size), Toast.LENGTH_SHORT).show()
            }
        }

        userViewModel.getBookingsForMyServices()
    }

    // Function to load dummy bookings
    private fun loadDummyBookings() {
        val dummyBookings = listOf(
            BookService(
                bookingId = "1",
                userId = "user123",
                serviceId = "service123",
                serviceName = "Calculus Tutoring",
                date = "2025-09-15",
                time = "14:00",
                location = "Online",
                status = "Pending",
                message = "Focus on integration techniques"
            ),
            BookService(
                bookingId = "2",
                userId = "user456",
                serviceId = "service456",
                serviceName = "Physics Tutoring",
                date = "2025-09-16",
                time = "16:00",
                location = "Library",
                status = "Confirmed",
                message = "Chapter 5 and 6"
            ),
            BookService(
                bookingId = "3",
                userId = "user789",
                serviceId = "service789",
                serviceName = "Chemistry Tutoring",
                date = "2025-09-17",
                time = "10:00",
                location = "Online",
                status = "Pending",
                message = "Organic chemistry focus"
            )
        )

        adapter.updateBookings(dummyBookings)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}