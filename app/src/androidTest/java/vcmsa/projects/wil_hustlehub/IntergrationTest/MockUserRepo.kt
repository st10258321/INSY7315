package vcmsa.projects.wil_hustlehub.IntergrationTest

import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.Model.User

class MockUserRepo : UserRepository() {
    override fun register(name:String, email:String, phone:String, password:String, callback: (Boolean, String?, User?) -> Unit){
        callback(true, "Registration successfull", User(name, email, phone, password))
    }
    override fun login(email:String, password:String, callback: (Boolean, String?, User?) -> Unit){
        callback(true, "Login successfull", User("Test", "Tester", "tester@gmail.com", "password123"))
    }
}