package vcmsa.projects.wil_hustlehub.View

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import kotlin.getValue

class UserBookingsActivity : AppCompatActivity() {
    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    private val chatRepo = ChatRepository()

    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)
    private val userViewModel: UserViewModel by viewModels { viewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_bookings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userid = sharedPreferences.getString("uid", null)

        userViewModel.getUserBookings()
        userViewModel.userBookings.observe(this) { bookings ->
            Toast.makeText(this, "Bookings: ${bookings?.size}", Toast.LENGTH_SHORT).show()
        }
    }
}