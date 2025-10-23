package vcmsa.projects.wil_hustlehub.Model

data class BookService(
    val bookingId: String = "",
    val userId: String = "",
    val serviceId: String = "",
    val serviceName: String = "",
    val serviceProviderId: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    var status: String = "Pending",
    val message: String = "",
    val userName: String = "",
)
data class CombinedData (
    val serviceName : String? = null,
    val serviveProviderId : String? = null,
    val spFcmToken : String? = null,
    val userName : String? = null
)
