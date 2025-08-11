package vcmsa.projects.wil_hustlehub.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vcmsa.projects.wil_hustlehub.Model.User
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import javax.inject.Inject

class UserViewModel @Inject constructor(private val userRepo: UserRepository): ViewModel() {

    val registrationStat = MutableLiveData<Triple<Boolean, String?,User?>>()

    fun register(user: User)  {
        // Call the register method from the user repository.
        userRepo.register(user.name,user.email, user.phoneNumber, user.password) { success, message,registeredUser ->
            // Post the result to LiveData for observers
            registrationStat.postValue(Triple(success, message,registeredUser))
        }
    }
    //modify to match the Repository function
    fun login(email: String, phoneNumber: String, callback: (User?, String?) -> Unit) {
        /*
            Delegates the login operation to the repository.
            The repository will invoke the callback with the login result.
       */
        userRepo.login(email, phoneNumber, callback)
    }

    fun getUserData(userID: String): LiveData<User?> {
        // Fetches user data for a given userID from the repository.
       // return userRepo.getUserData(userID)
        return MutableLiveData()
    }
}