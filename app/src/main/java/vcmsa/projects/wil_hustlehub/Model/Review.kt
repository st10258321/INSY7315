package vcmsa.projects.wil_hustlehub.Model

data class Review(
    val reviewId: String = "",
    val userId: String = "", // ID of user who wrote the review
    val serviceId: String = "", // ID of service being reviewed
    val reviewerName: String = "", // Name of user who wrote the review
    val stars: Int = 0, // Rating out of 5
    val reviewText: String = "",
    val reviewDate: String = ""
)

