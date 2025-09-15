package vcmsa.projects.wil_hustlehub.Model

data class BookService(
    val bookingId: String = "",
    val userId: String = "",
    val serviceId: String = "",
    val serviceName: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val status: String = "Pending",
    val message: String = "",
    val userName : String = ""
)
