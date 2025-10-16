package vcmsa.projects.wil_hustlehub.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.R

class ServiceAdapter(
    private var serviceList: List<Service>,
    private val onDeleteClick: (Service) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
        val serviceProviderName: TextView = itemView.findViewById(R.id.serviceProviderName)
        val serviceCreatedDate: TextView = itemView.findViewById(R.id.serviceCreatedDate)
        val serviceLocation: TextView = itemView.findViewById(R.id.serviceLocation)
        val bookingNotes: TextView = itemView.findViewById(R.id.bookingNotes)
        val btnDeleteService: Button = itemView.findViewById(R.id.btnDeleteService)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service_management, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = serviceList[position]
        holder.serviceName.text = service.serviceName
        holder.serviceProviderName.text = "Provider ID: ${service.userId}"
        holder.serviceCreatedDate.text = "Date: ${service.createdDate}"
        holder.serviceLocation.text = "Location: ${service.location}"
        holder.bookingNotes.text = "Description: ${service.description}"

        holder.btnDeleteService.setOnClickListener {
            onDeleteClick(service)
        }
    }

    override fun getItemCount(): Int = serviceList.size

    fun updateList(newList: List<Service>) {
        serviceList = newList
        notifyDataSetChanged()
    }
}
