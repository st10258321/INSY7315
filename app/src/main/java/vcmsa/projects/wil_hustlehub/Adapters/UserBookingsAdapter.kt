package vcmsa.projects.wil_hustlehub.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Adapters.BrowseServiceAdapter.ServiceViewHolder
import vcmsa.projects.wil_hustlehub.Model.BookService
import vcmsa.projects.wil_hustlehub.R

class UserBookingsAdapter(
    private val bookings: MutableList<BookService>,
    private val onStartedClick : (BookService) -> Unit,
    private val onCompletedClick : (BookService) -> Unit,
    private val onCancelClick : (BookService) -> Unit
): RecyclerView.Adapter<UserBookingsAdapter.BookingViewHolder>() {
    inner class BookingViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val serviceName : TextView = itemView.findViewById(R.id.bookingServiceTitle)
        val status : TextView = itemView.findViewById(R.id.bookingStatus)
        val bDatetime : TextView = itemView.findViewById(R.id.bookingDateTime)
        val bLocation : TextView = itemView.findViewById(R.id.bookingLocation)
        val bNotes : TextView = itemView.findViewById(R.id.bookingNotes)
        val btnStarted : Button = itemView.findViewById(R.id.btnStarted)
        val btnCompleted : Button = itemView.findViewById(R.id.btnCompleted)
        val btnCanceled : Button = itemView.findViewById(R.id.btnCancel)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(vcmsa.projects.wil_hustlehub.R.layout.item_user_bookings, parent, false)
        return BookingViewHolder(view)
    }
    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val context = holder.itemView.context
        val booking = bookings[position]
        holder.serviceName.text = booking.serviceName
        if(booking.status == "Started"){
            holder.status.background = context.getDrawable(R.drawable.status_started_background)
        }else if(booking.status == "Completed"){
            holder.status.background = context.getDrawable(R.drawable.status_confirmed_background)
            holder.btnStarted.visibility = View.GONE
            holder.btnCompleted.visibility = View.GONE
            holder.btnCanceled.visibility = View.GONE
        }else if(booking.status == "Canceled"){
            holder.status.background = context.getDrawable(R.drawable.status_rejected_background)
            holder.btnStarted.visibility = View.GONE
            holder.btnCompleted.visibility = View.GONE
            holder.btnCanceled.visibility = View.GONE
        }else if(booking.status == "Confirmed"){
            holder.status.background = context.getDrawable(R.drawable.status_confirmed_background)
        }else if (booking.status == "Rejected"){
            holder.status.background = context.getDrawable(R.drawable.status_rejected_background)
            holder.btnStarted.visibility = View.GONE
            holder.btnCompleted.visibility = View.GONE
            holder.btnCanceled.visibility = View.GONE
        }
        else{
            holder.status.background = context.getDrawable(R.drawable.status_pending_background)
            holder.btnStarted.visibility = View.VISIBLE
            holder.btnCompleted.visibility = View.VISIBLE
            holder.btnCanceled.visibility = View.VISIBLE
        }


        holder.status.text = booking.status
        holder.bDatetime.text = booking.date
        holder.bLocation.text = booking.location
        holder.bNotes.text = booking.message
        holder.btnStarted.setOnClickListener {
            onStartedClick(booking)
            updateBookingStatus(booking.bookingId)
        }
        holder.btnCompleted.setOnClickListener {
            onCompletedClick(booking)

            updateBookingStatus(booking.bookingId)
        }
        holder.btnCanceled.setOnClickListener {
            onCancelClick(booking)
            updateBookingStatus(booking.bookingId)
        }
    }
    fun getUserBookings(bookings : List<BookService>){
        this.bookings.clear()
        this.bookings.addAll(bookings)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return bookings.size
    }
    fun updateBookingStatus(bookingId: String){
        notifyItemChanged(bookings.indexOfFirst { it.bookingId == bookingId })
    }


}