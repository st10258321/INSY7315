package vcmsa.projects.wil_hustlehub.Model

data class User (
var userID: String? = null ,
var name: String? = null ,
var email: String? = null  ,
var phoneNumber: String ? = null ,
var password : String? = null ,
var createdDate: String? = null
){
    constructor() : this(null, null, null, null, null)
}
