package vcmsa.projects.wil_hustlehub.View

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import androidx.lifecycle.MediatorLiveData
import com.google.android.material.datepicker.MaterialDatePicker
import vcmsa.projects.wil_hustlehub.MainActivity
import vcmsa.projects.wil_hustlehub.Model.CombinedData
import vcmsa.projects.wil_hustlehub.Network.PushApiClient
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import java.util.Calendar

class BookServiceFragment : Fragment() {
    private var _binding: FragmentBookServiceBinding? = null
    private val binding get() = _binding!!

    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    private val chatRepo = ChatRepository()
    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo,chatRepo)
    private val userViewModel: UserViewModel by viewModels { viewModelFactory }

    private var spFcmToken: String? = null
    private var serviceID: String? = null
    private var serviceName: String? = null
    private var userName: String? = null
    private var serviceProviderId: String = ""

    // keep only one source of truth
    private var selectedDate: String = ""
    private var selectedTime: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        serviceID = arguments?.getString("serviceID")

        val sharedPref = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("uid", "")
        binding.addPetBackButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }


        userViewModel.getUserData(userId!!).observe(viewLifecycleOwner) { user ->
            userName = user?.name
        }
        // load service
        userViewModel.getServiceById(serviceID!!).observe(viewLifecycleOwner) { service ->
            if (service != null) {
                binding.selectedServiceTitle.text = service.serviceName
                serviceName = service.serviceName
                serviceProviderId = service.userId

                if (serviceProviderId.isNotEmpty()) {
                    userViewModel.getUserData(serviceProviderId).observe(viewLifecycleOwner) { provider ->
                        spFcmToken = provider?.fcmToken
                    }
                }
            }
        }


        // --- Date selection ---
        binding.btnSelectDate.setOnClickListener {
           val datePicker = MaterialDatePicker.Builder.datePicker()
               .setTitleText("Select Date")
               .build()
            datePicker.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance().apply{timeInMillis = selection}
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                selectedDate = "$day/${month + 1}/$year"
               binding.selectedDate?.text = selectedDate

            }
            datePicker.show(parentFragmentManager, "DatePicker")
            binding.timeslotSpinner?.visibility = View.GONE
            //binding.edBookingTime.visibility = View.GONE
        }


        // --- Time selection (Spinner) ---
        binding.btnSelectTime.setOnClickListener {

            binding.timeslotSpinner?.visibility = View.VISIBLE
          //  binding.edBookingTime.visibility = View.GONE
        }


        // --- Confirm Booking ---
        binding.btnConfirmBooking.setOnClickListener {
            // get time either from spinner or edittext
            selectedTime = when {
                binding.timeslotSpinner?.isVisible == true -> {
                    val pos = binding.timeslotSpinner?.selectedItemPosition ?: -1
                    if (pos > 0) binding.timeslotSpinner?.selectedItem?.toString() else null
                }
                //binding.edBookingTime.isVisible -> binding.edBookingTime.text.toString().takeIf { it.isNotEmpty() }
                else -> null
            }


            val location = if (binding.radioOnline.isChecked) "Online" else "On Campus"
            val additionalNotes = binding.additionalNotes.text.toString()

            if (selectedDate.isEmpty() || selectedTime.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please select a date and time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userViewModel.createBooking(
                serviceID!!,
                selectedDate,
                selectedTime!!,
                location,
                additionalNotes
            )

            userViewModel.bookingActionStatus.observe(viewLifecycleOwner) { (success, message) ->
                if (success) {
                    Toast.makeText(requireContext(), "Booking created successfully", Toast.LENGTH_SHORT).show()
                    try {
                        PushApiClient.sendBookingNotification(
                            requireContext(),
                            spFcmToken,
                            userName,
                            serviceName
                        )
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("BookService", "Failed to send notification", e)
                    }
                } else {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
