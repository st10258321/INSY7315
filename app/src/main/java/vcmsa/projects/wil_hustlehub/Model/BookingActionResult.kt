package vcmsa.projects.wil_hustlehub.Model

data class BookingActionResult (
    val success : Boolean,
    val message : String?,
    val bookingId : String,
    val newStatus: String
)