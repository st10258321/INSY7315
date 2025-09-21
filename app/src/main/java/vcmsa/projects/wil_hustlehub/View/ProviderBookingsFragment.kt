package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Adapters.ProviderBookingsAdapter
import vcmsa.projects.wil_hustlehub.Model.BookService
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentBookServiceBinding
import vcmsa.projects.wil_hustlehub.databinding.FragmentProviderBookingsBinding
import kotlin.getValue


class ProviderBookingsFragment : Fragment() {
    private var _binding: FragmentProviderBookingsBinding? = null
    private lateinit var adapter: ProviderBookingsAdapter


    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment_profile.xml layout.
        _binding = FragmentProviderBookingsBinding.inflate(inflater, container, false)
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

        //first intializing the adapter with an empty list
        adapter = ProviderBookingsAdapter(
            bookings = mutableListOf(),
            onConfirmAction = { booking ->
                userViewModel.confirmBooking(booking.bookingId)
            },
            onRejectAction = { booking ->
                userViewModel.rejectBooking(booking.bookingId)
            }
        )

        binding.recyclerViewBookings.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBookings.adapter = adapter

        userViewModel.bookingsForMyServices.observe(viewLifecycleOwner) { bookings ->
            adapter.updateBookings(bookings)
            Toast.makeText(requireContext(), "Bookings fetched successfully:${bookings?.size}", Toast.LENGTH_SHORT).show()
        }
        userViewModel.getBookingsForMyServices()
        }

    }

