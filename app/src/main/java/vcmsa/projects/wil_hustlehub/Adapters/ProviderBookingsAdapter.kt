package vcmsa.projects.wil_hustlehub.Adapters

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.wil_hustlehub.Model.BookService
import android.widget.TextView
import vcmsa.projects.wil_hustlehub.databinding.ItemProviderBookingBinding

class ProviderBookingsAdapter(
    val bookings: MutableList<BookService>,
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
        holder.binding.bookingServiceTitle.text = booking.serviceName
        holder.binding.bookingStatus.text = booking.status
        holder.binding.bookingCustomerName.text = "Customer: ${booking.userId}"
        holder.binding.bookingDateTime.text = "Date: ${booking.date}, Time: ${booking.time}"
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

   fun updateBookings(newBookings:List<BookService>?){
       bookings.clear()
        newBookings?.let{bookings.addAll(it)}
        notifyDataSetChanged()
   }
}
