package vcmsa.projects.wil_hustlehub.View


import android.content.Intent
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
import vcmsa.projects.wil_hustlehub.MainActivity
import vcmsa.projects.wil_hustlehub.Network.PushApiClient
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
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

    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    private val chatRepo = ChatRepository()
    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo,chatRepo)
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

        userViewModel.getMyServiceProviderBookings()
        binding.addPetBackButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        adapter = ProviderBookingsAdapter(
            bookings = mutableListOf(),
            onConfirmAction = { booking ->
                userViewModel.confirmBooking(booking.bookingId)
                userViewModel.bookingActionStatus.observe(viewLifecycleOwner){result ->
                    if(result.success) {
                        adapter.updateBookingStatus(result.bookingId, result.newStatus)
                        try{
                            userViewModel.getUserData(booking.serviceProviderId).observe(viewLifecycleOwner){provider->
                                if(provider?.fcmToken != null){
                                    PushApiClient.sendBookingStatusNotification(
                                        context = requireContext(),
                                        recipientToken = provider.fcmToken!!,
                                        bookingName = booking.serviceName,
                                        status = result.newStatus
                                    )
                                }
                                else{
                                    Log.d("--api","User does not have fcm token")
                                }
                            }
                        }catch(e: Exception){
                            Log.d("--checking","${e.message}")
                        }
                    }
                }

            },
            onRejectAction = { booking ->
                userViewModel.rejectBooking(booking.bookingId)
                userViewModel.bookingActionStatus.observe(viewLifecycleOwner){result ->
                    if(result.success) {
                        adapter.updateBookingStatus(result.bookingId, result.newStatus)
                        try{
                            userViewModel.getUserData(booking.serviceProviderId).observe(viewLifecycleOwner){provider->
                                if(provider?.fcmToken != null){
                                    PushApiClient.sendBookingStatusNotification(
                                        context = requireContext(),
                                        recipientToken = provider.fcmToken!!,
                                        bookingName = booking.serviceName,
                                        status = result.newStatus
                                    )
                                }
                                else{
                                    Log.d("--api","User does not have fcm token")
                                }
                            }
                        }catch(e: Exception){
                            Log.d("--checking","${e.message}")
                        }
                    }
                }
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}