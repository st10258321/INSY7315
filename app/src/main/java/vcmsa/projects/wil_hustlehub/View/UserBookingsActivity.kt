package vcmsa.projects.wil_hustlehub.View

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import vcmsa.projects.wil_hustlehub.Adapters.UserBookingsAdapter
import vcmsa.projects.wil_hustlehub.Model.BookService
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import vcmsa.projects.wil_hustlehub.databinding.ActivityUserBookingsBinding
import kotlin.getValue

class UserBookingsActivity : AppCompatActivity() {
    private var _binding : ActivityUserBookingsBinding? = null
    private val binding get() = _binding!!
    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    private val chatRepo = ChatRepository()
    private lateinit var adapter: UserBookingsAdapter
    private val bookings = mutableListOf<BookService>()

    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)
    private val userViewModel: UserViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityUserBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userid = sharedPreferences.getString("uid", null)
        binding.addPetBackButton.setOnClickListener {
            finish()
        }
        val userBookingRecycler = binding.userBookingsRecycler
        userBookingRecycler.layoutManager = LinearLayoutManager(this)

         adapter = UserBookingsAdapter(bookings,
            onStartedClick = { booking ->
                booking.status = "Started"
                userViewModel.updateBooking(booking)
                adapter.updateBookingStatus(booking.bookingId)
            },
            onCompletedClick ={ booking ->
                booking.status = "Completed"
                userViewModel.updateBooking(booking)
                adapter.updateBookingStatus(booking.bookingId)
            },
            onCancelClick ={ booking->
                booking.status = "Canceled"
                userViewModel.updateBooking(booking)
                adapter.updateBookingStatus(booking.bookingId)
            }
        )
        userViewModel.getUserBookings()
        userViewModel.userBookings.observe(this) { bookings ->
            if (bookings != null) {
                adapter.getUserBookings(bookings)
                userBookingRecycler.adapter = adapter
            }
        }
    }
}