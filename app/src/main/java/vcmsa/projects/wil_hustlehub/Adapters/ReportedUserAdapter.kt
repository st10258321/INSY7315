package vcmsa.projects.wil_hustlehub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vcmsa.projects.wil_hustlehub.Model.Report
import vcmsa.projects.wil_hustlehub.R

class ReportedUsersAdapter(
    private var reports: MutableList<Report>,
    private val onSuspendClick: (Report, Int) -> Unit,
    private val onDeleteClick: (Report, Int) -> Unit
) : RecyclerView.Adapter<ReportedUsersAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.reportedUserName)
        val reportIssue: TextView = itemView.findViewById(R.id.serviceIssueNameTxt)
        val additionalNotes: TextView = itemView.findViewById(R.id.descOfReportTxt)
        val statusText: TextView = itemView.findViewById(R.id.statusTxt)
        val reportDateText : TextView = itemView.findViewById(R.id.reportDateTxt)

        val suspendButton: Button = itemView.findViewById(R.id.btnSuspendUser)
        val deleteButton: Button = itemView.findViewById(R.id.btnDeleteUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reported_user, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]
        holder.userName.text = report.serviceProviderName ?: "Unkown User"
        holder.reportIssue.text = report.reportIssue
        holder.additionalNotes.text = report.additionalNotes
        holder.reportDateText.text = report.createdDate
        holder.statusText.text = report.status


        holder.suspendButton.setOnClickListener {
            onSuspendClick(report, position)
        }
        holder.deleteButton.setOnClickListener {
            onDeleteClick(report, position)
        }
    }

    override fun getItemCount(): Int = reports.size

    // Update only the changed item
    fun updateUserStatus(position: Int, updatedReport: Report) {
        reports[position] = updatedReport
        notifyItemChanged(position)
    }

    // Remove a single user/report
    fun removeUser(position: Int) {
        reports.removeAt(position)
        notifyItemRemoved(position)
    }

    // Update full list when data loads for the first time
    fun setData(newReports: List<Report>) {
        reports.clear()
        reports.addAll(newReports)
        notifyDataSetChanged()
    }
}
