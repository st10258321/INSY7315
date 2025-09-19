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

    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo)
    private val userViewModel: UserViewModel by viewModels { viewModelFactory }

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


        userViewModel.getMyServiceProviderBookings()


        adapter = ProviderBookingsAdapter(
            bookings = mutableListOf(),
            onConfirmAction = { booking ->

                Log.d("ProviderBookingsFragment", "Confirming booking: ${booking.bookingId}")
                userViewModel.confirmBooking(booking.bookingId)
                userViewModel.bookingActionStatus.observe(viewLifecycleOwner) { (success, message) ->
                    if(success){
                        //send them a push notification here and change the status of the booking
                        Toast.makeText(requireContext(),"Booking confirmed", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(),"Booking confirmation failed", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onRejectAction = { booking ->
                Log.d("ProviderBookingsFragment", "Rejecting booking: ${booking.bookingId}")
                userViewModel.rejectBooking(booking.bookingId)

            }
        )
        userViewModel.bookingActionStatus.observe(viewLifecycleOwner) { (success, message) ->
            if (success) {
                //update the list of bookings
                Toast.makeText(requireContext(), message ?: "Booking action successful", Toast.LENGTH_SHORT).show()
                userViewModel.getMyServiceProviderBookings()
            } else {
                Toast.makeText(requireContext(), message?:"Booking action failed", Toast.LENGTH_SHORT).show()
            }
        }

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}