package vcmsa.projects.wil_hustlehub.View
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Adapter.ReportedUsersAdapter
import vcmsa.projects.wil_hustlehub.Model.Report
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.*
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory

class UserManagementActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportedUsersAdapter
    private val reports = mutableListOf<Report>()

    private val userRepo = UserRepository()
    private val serviceRepo = ServiceRepository()
    private val bookRepo = BookServiceRepository()
    private val reviewRepo = ReviewRepository()
    private val chatRepo = ChatRepository()

    private val viewModelFactory = ViewModelFactory(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)
    private val userViewModel: UserViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        recyclerView = findViewById(R.id.reportedUsersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        adapter = ReportedUsersAdapter(
            reports,
            onSuspendClick = { report, position -> handleSuspend(report, position) },
            onDeleteClick = { report, position -> handleDelete(report, position) }
        )
        recyclerView.adapter = adapter
        userViewModel.loadReportedUser()
        userViewModel.reportedUsers.observe(this) { loadedReports ->
            adapter.setData(loadedReports.third?.toList() ?: emptyList())
        }
    }

    private fun handleSuspend(report: Report, position: Int) {
        val updatedReport = report.copy(status = "Suspended")
        userViewModel.updateReportStatus(updatedReport){ success , message ->
            if(success){
                adapter.updateUserStatus(position, updatedReport)
                Toast.makeText(this, "${report.serviceProviderId} suspended.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Error suspending user.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun handleDelete(report: Report, position: Int) {
       // userViewModel.deleteReportedUser(report.serviceProviderId)
        adapter.removeUser(position)
        Toast.makeText(this, "${report.serviceProviderId} deleted.", Toast.LENGTH_SHORT).show()
    }
}


