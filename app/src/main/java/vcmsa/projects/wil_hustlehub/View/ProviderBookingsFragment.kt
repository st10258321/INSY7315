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
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
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
        val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo)
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
            Log.d("ProviderBookingsFragment", "Bookings received: ${bookings?.size}")
            adapter.updateBookings(bookings)

            if (bookings.isNullOrEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.toast_no_bookings), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.toast_bookings_fetched, bookings.size), Toast.LENGTH_SHORT).show()
            }
        }

        userViewModel.getBookingsForMyServices()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}