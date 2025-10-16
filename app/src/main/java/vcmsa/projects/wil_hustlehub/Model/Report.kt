package vcmsa.projects.wil_hustlehub.Model

data class Report(
    val reportId: String = "",
    val serviceProviderId: String = "",
    val serviceProviderName : String = "",
    val userId: String = "",
    val serviceId: String = "",
    val reportIssue: String = "",
    val additionalNotes: String = "",
    val image: String = "",
    var status : String = "Pending",
    val createdDate: String = ""
)
