package vcmsa.projects.wil_hustlehub.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Adapters.BrowseServiceAdapter.ServiceViewHolder
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.ViewModel.ViewModelFactory
import kotlin.getValue


class ProfileServiceAdapter(
    private var services: List<Service>,
    private val onBookServiceClick: (Service) -> Unit,
    private val onLeaveReviewClick: (Service) -> Unit,
    private val isOwner : Boolean
): RecyclerView.Adapter<ProfileServiceAdapter.ProfileViewHolder>() {
    inner class ProfileViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tvServiceName : TextView = itemView.findViewById(R.id.serviceTitle)

        val tvServicePrice : TextView = itemView.findViewById(R.id.servicePrice)

        val tvDescription : TextView = itemView.findViewById(R.id.serviceDescription)
        val btnBookService : Button = itemView.findViewById(R.id.bookNowBtn)

        val btnLeaveReview : Button = itemView.findViewById(R.id.leaveReviewBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile_service, parent, false)
        return ProfileViewHolder(view)
    }
    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val service: Service = services.get(position)
        holder.tvServiceName.text = service.serviceName
        holder.tvServicePrice.text = "R${service.price}/hour"
        holder.tvDescription.text = service.description
        if(isOwner){
            holder.btnBookService.visibility = View.GONE
            holder.btnLeaveReview.visibility = View.GONE
        }else{
            holder.btnLeaveReview.visibility = View.VISIBLE
            holder.btnBookService.visibility = View.VISIBLE
            holder.btnBookService.setOnClickListener {
                onBookServiceClick(service)
            }
            holder.btnLeaveReview.setOnClickListener {
                onLeaveReviewClick(service)
            }
        }


    }
    override fun getItemCount(): Int = services.size
    fun updateServices(newServices: List<Service>) {
        services = newServices
        notifyDataSetChanged()
    }
}