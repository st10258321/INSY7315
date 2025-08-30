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

        holder.binding.bookingServiceTitle.text = booking.serviceName
        holder.binding.bookingStatus.text = booking.status
        holder.binding.bookingCustomerName.text =
            context.getString(R.string.label_customer, booking.userId)
        holder.binding.bookingDateTime.text =
            context.getString(R.string.label_date_time, booking.date, booking.time)
        holder.binding.bookingLocation.text =
            context.getString(R.string.label_location, booking.location)
        holder.binding.bookingNotes.text =
            context.getString(R.string.label_notes, booking.message)

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
}
