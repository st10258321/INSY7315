package vcmsa.projects.wil_hustlehub.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vcmsa.projects.wil_hustlehub.Model.User
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import javax.inject.Inject

class UserViewModel @Inject constructor(private val userRepo: UserRepository) : ViewModel() {

    private val registrationStat = MutableLiveData<Pair<Boolean, String?>>()

    // Register a new user
    fun register(user: User) {
        userRepo.register(user.name, user.email, user.phone, user.password) { success, message, _ ->
            // Post the result to LiveData for observers.
            registrationStat.postValue(Pair(success, message))
        }
    }

    // Login the user
    fun login(email: String, password: String, callback: (User?, String?) -> Unit) {
        userRepo.login(email, password) { success, message, user ->
            if (success) {
                callback(user, null)
            } else {
                callback(null, message)
            }
        }
    }

    // Get user data (returns LiveData for observation)
    fun getUserData(userID: String): LiveData<User?> {
        val liveData = MutableLiveData<User?>()
        userRepo.getUserData(userID) { user ->
            liveData.postValue(user)
        }
        return liveData
    }
}
