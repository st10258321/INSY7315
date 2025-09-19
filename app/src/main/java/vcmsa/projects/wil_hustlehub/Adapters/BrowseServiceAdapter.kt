package vcmsa.projects.wil_hustlehub.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.R

class BrowseServiceAdapter (
    private val services : List<Service>,
    private val getUserName: (String) -> String,
    private val onViewProfileClick: (Service) -> Unit,
    private val onBookServiceClick: (Service) -> Unit
): RecyclerView.Adapter<BrowseServiceAdapter.ServiceViewHolder>(){
    inner class ServiceViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val tvServiceName : TextView = itemView.findViewById(R.id.serviceTitle)
        val tvName : TextView = itemView.findViewById(R.id.providerName)
        val tvServicePrice : TextView = itemView.findViewById(R.id.servicePrice)
        val tvDescription : TextView = itemView.findViewById(R.id.serviceDescription)
        val btnViewProfile : Button = itemView.findViewById(R.id.btnViewProfile)
        val btnBookService : Button = itemView.findViewById(R.id.btnBookSer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service_card, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service: Service = services.get(position)
        holder.tvServiceName.text = service.serviceName
        holder.tvName.text = getUserName(service.userId)
        holder.tvServicePrice.text = "R${service.price}"
        holder.tvDescription.text = service.description
        holder.btnViewProfile.setOnClickListener {
            onViewProfileClick(service)
        }
        holder.btnBookService.setOnClickListener {
            onBookServiceClick(service)
        }
    }

    override fun getItemCount(): Int = services.size
}