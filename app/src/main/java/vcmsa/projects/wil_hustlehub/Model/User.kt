package vcmsa.projects.wil_hustlehub.Model

data class User (
    var userID: String = "",
    var name: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var password : String = "",
    var aboutMe: String = "",
    var tagLine: String = "",
    var profilePhoto: String = "",
    var createdDate: String = "",
    var fcmToken : String? = null
)
