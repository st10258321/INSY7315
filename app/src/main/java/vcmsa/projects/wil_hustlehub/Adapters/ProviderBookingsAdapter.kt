package vcmsa.projects.wil_hustlehub.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Model.BookService
import vcmsa.projects.wil_hustlehub.R
import vcmsa.projects.wil_hustlehub.databinding.ItemProviderBookingBinding

class ProviderBookingsAdapter(
    private val bookings: MutableList<BookService>,
    private val onConfirmAction: (BookService) -> Unit,
    private val onRejectAction: (BookService) -> Unit
): RecyclerView.Adapter<ProviderBookingsAdapter.BookingViewHolder>(){

    inner class BookingViewHolder(val binding: ItemProviderBookingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProviderBookingBinding.inflate(inflater, parent, false)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        val context = holder.itemView.context
//just for git purposes, didnt change anything
        holder.binding.bookingServiceTitle.text = booking.serviceName
        holder.binding.bookingStatus.text = booking.status
        if(booking.status == "Confirmed"){
            holder.binding.btnRejectBooking.isEnabled = false
            holder.binding.btnConfirmBooking.isEnabled = false
            holder.binding.bookingStatus.background = context.getDrawable(R.drawable.status_confirmed_background)
        }else if(booking.status == "Rejected"){
            //service provider can still accept the booking.
            holder.binding.btnRejectBooking.isEnabled = false
            holder.binding.bookingStatus.background = context.getDrawable(R.drawable.status_rejected_background)
        }else if (booking.status == "Pending"){
            holder.binding.bookingStatus.background = context.getDrawable(R.drawable.status_pending_background)
            holder.binding.btnRejectBooking.isEnabled = true
            holder.binding.btnConfirmBooking.isEnabled = true
        }else if (booking.status == "Cancelled"){
            holder.binding.bookingStatus.background = context.getDrawable(R.drawable.status_rejected_background)
            holder.binding.btnRejectBooking.isEnabled = false
            holder.binding.btnConfirmBooking.isEnabled = false
        }else if(booking.status == "Completed"){
            holder.binding.bookingStatus.background = context.getDrawable(R.drawable.status_confirmed_background)
            holder.binding.btnRejectBooking.isEnabled = false
            holder.binding.btnConfirmBooking.isEnabled = false
        }else{
            holder.binding.bookingStatus.background = context.getDrawable(R.drawable.status_pending_background)
            holder.binding.btnRejectBooking.isEnabled = true
            holder.binding.btnConfirmBooking.isEnabled = true
        }
        holder.binding.bookingCustomerName.text = "Customer Name: ${booking.userName}"
        holder.binding.bookingDateTime.text = "Date and Time: ${booking.date} - ${booking.time}"
        holder.binding.bookingLocation.text = "Location: ${booking.location}"
        holder.binding.bookingNotes.text = "Notes: ${booking.message}"

        holder.binding.btnRejectBooking.setOnClickListener {
            onRejectAction(booking)
        }

        holder.binding.btnConfirmBooking.setOnClickListener {
            onConfirmAction(booking)
        }
    }

    override fun getItemCount(): Int = bookings.size

    fun updateBookings(newBookings: List<BookService>?) {
        bookings.clear()
        newBookings?.let(bookings::addAll)
        notifyDataSetChanged()
    }
    fun updateBookingStatus(bookingId: String, newStatus: String) {
        val index = bookings.indexOfFirst { it.bookingId == bookingId }
        if(index != 1){
            bookings[index].status = newStatus
            notifyItemChanged(index)
        }
    }
}
