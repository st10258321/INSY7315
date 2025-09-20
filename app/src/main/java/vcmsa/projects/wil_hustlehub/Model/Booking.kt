package vcmsa.projects.wil_hustlehub.Model

data class Booking(
    val serviceTitle: String,
    val customerName: String,
    val dateTime: String,
    val location: String,
    val notes: String,
    val status: String // e.g., "Pending" or "Confirmed"
)
