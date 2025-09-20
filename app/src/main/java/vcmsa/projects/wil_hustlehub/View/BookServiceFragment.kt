package vcmsa.projects.wil_hustlehub.View

import android.R
import android.content.Context
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
import vcmsa.projects.wil_hustlehub.Model.CombinedData
import vcmsa.projects.wil_hustlehub.Network.PushApiClient

class BookServiceFragment: Fragment() {
    // Declare the binding variable
    private var _binding: FragmentBookServiceBinding? = null
    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo)
    private val userViewModel: UserViewModel by viewModels { viewModelFactory }
    private var spFcmToken :String? = null
    private var serviceID :String? = null
    private var serviceName :String? = null
    private var userName :String? = null
    private lateinit var combinedData: MediatorLiveData<CombinedData>


    private var serviceProviderId :String = ""
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


//        userViewModel.currentUserData.observe(viewLifecycleOwner){cUser ->
//            userName = cUser?.name
//        }
        val sharedPref = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val uName = sharedPref.getString("uid", "")
        if(!uName.isNullOrEmpty())
            userName = uName

        userViewModel.getServiceById(serviceID!!).observe(viewLifecycleOwner){service ->
            if(service != null){
                binding.selectedServiceTitle.text = service.serviceName
                serviceName = service.serviceName
                serviceProviderId = service.userId
                Toast.makeText(requireContext(), "userName: ${userName}", Toast.LENGTH_SHORT).show()

                if(serviceProviderId.isNotEmpty()){
                    userViewModel.getUserData(serviceProviderId).observe(viewLifecycleOwner){provider->
                        spFcmToken = provider?.fcmToken
                        Toast.makeText(requireContext(), "spFcmToken: ${spFcmToken}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

//        // Set background color programmatically
//        binding.bookingCalendar.backgroundTintList =
//            ContextCompat.getColorStateList(requireContext(), R.color.white)



            //displaying the calendar
            binding.btnSelectDate.setOnClickListener {
                binding.bookingCalendar.visibility = View.VISIBLE

            }
            //hiding the time Calendar when the user selects a Time
            binding.btnSelectTime.setOnClickListener {
                binding.bookingCalendar.visibility = View.GONE
                binding.timeslotSpinner?.visibility = View.VISIBLE
                binding.timeslotSpinner?.setSelection(0)  //default selection being the first option
            }
            var selectedDate  = ""
            binding.bookingCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
                selectedDate  = "$dayOfMonth/${month + 1}/$year"
                binding.selectedDate?.text = selectedDate
            }


            binding.btnConfirmBooking.setOnClickListener{
                checkAndEnableBooking(selectedDate)
            }

        }
    fun checkAndEnableBooking(selectedDate : String){



        //sending the information to the database
            val selectedTime = binding.timeslotSpinner?.selectedItemPosition?.let {
                if(it > 0)
                    binding.timeslotSpinner?.selectedItem.toString()
                else{
                    null
                }
            }
            val location = if (binding.radioOnline.isChecked) "Online" else "On Campus"
            val additionalNotes = binding.additionalNotes.text.toString()
            if (selectedDate.isNullOrEmpty() || selectedTime.isNullOrEmpty() || location.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please select a date, time or location",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                userViewModel.createBooking(
                    serviceID!!,
                    selectedDate,
                    selectedTime,
                    location,
                    additionalNotes
                )

                userViewModel.bookingActionStatus.observe(viewLifecycleOwner) { (success, message) ->

                    if (success) {
                        Toast.makeText(
                            requireContext(),
                            "Booking created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        //navigate to the home page or the page where the user can see their pending bookings
                        //send notification to service provider
                        try {
                            PushApiClient.sendBookingNotification(
                                requireContext(),
                                spFcmToken,
                                userName,
                                serviceName
                            )
                        } catch (e: Exception) {
                            Log.d(
                                "API-TEST!!!!",
                                "${spFcmToken} -- ${userName} -- ${serviceName}"
                            )
                            Toast.makeText(
                                requireContext(),
                                "Failed to send notification",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to create booking",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

    }
}