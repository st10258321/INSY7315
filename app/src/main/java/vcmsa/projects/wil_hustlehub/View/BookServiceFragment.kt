package vcmsa.projects.wil_hustlehub.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.FragmentBookServiceBinding
import kotlin.getValue
import androidx.core.view.isVisible

class BookServiceFragment: Fragment() {
    // Declare the binding variable
    private var _binding: FragmentBookServiceBinding? = null
    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo)
    private val userViewModel: UserViewModel by viewModels { viewModelFactory }

    private var serviceID :String? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment_book_service.xml layout.
        _binding = FragmentBookServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serviceID = arguments?.getString("serviceID")
        userViewModel.getServiceById(serviceID!!)
            .observe(viewLifecycleOwner) { service ->
            if(service != null) {
                binding.selectedServiceTitle.text = service.serviceName
            }
        }
        //hiding the calendar
        if(binding.bookingCalendar.isVisible)
            binding.bookingCalendar.visibility = View.GONE
        //displaying the calendar
        binding.btnSelectDate.setOnClickListener {
            binding.bookingCalendar.visibility = View.VISIBLE
        }
        //hiding the time Calendar when the user selects a Time
        binding.btnSelectTime.setOnClickListener {
            binding.bookingCalendar.visibility = View.GONE
            binding.edBookingTime.visibility = View.VISIBLE
        }

        binding.bookingCalendar.setOnDateChangeListener {
            view, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            binding.selectedDateTime.text = selectedDate
        }
        //sending the information to the database
        binding.btnConfirmBooking.setOnClickListener {
            val selectedDate = binding.selectedDateTime.text.toString()
            val selectedTime = binding.edBookingTime.text.toString()
            val location = if (binding.radioOnline.isChecked) "Online" else "On Campus"
            val additionalNotes = binding.additionalNotes.text.toString()
            if (selectedDate.isEmpty() || selectedTime.isEmpty() || location.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a date, time or location", Toast.LENGTH_SHORT).show()
            }else{
                userViewModel.createBooking(serviceID!!, selectedDate, selectedTime, location, additionalNotes)

                userViewModel.bookingActionStatus.observe(viewLifecycleOwner) { (success, message) ->

                    if (success) {
                        Toast.makeText(requireContext(), "Booking created successfully", Toast.LENGTH_SHORT).show()
                        //navigate to the home page or the page where the user can see their pending bookings
                    }else{
                        Toast.makeText(requireContext(), "Failed to create booking", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}