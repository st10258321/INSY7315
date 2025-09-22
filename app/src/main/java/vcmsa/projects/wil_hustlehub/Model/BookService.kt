package vcmsa.projects.wil_hustlehub.Model

data class BookService(
    val bookingId: String = "",
    val userId: String = "",
    val serviceId: String = "",
    val providerId :String = "",
    val serviceName: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val status: String = "Pending",
    val message: String = "",
    val userName : String = ""
)
data class CombinedData(
    val serviceName: String? = null,
    val serviceProviderId: String? = null,
    val spFcmToken: String? = null,
    val userName: String? = null
)