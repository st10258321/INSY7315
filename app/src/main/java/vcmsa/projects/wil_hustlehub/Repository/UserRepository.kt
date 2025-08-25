package vcmsa.projects.wil_hustlehub.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vcmsa.projects.wil_hustlehub.Model.User


class UserRepository {
    //Where all the firebase is being done
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    //making the date and time readable in the database
    val createdDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    val createdDate = createdDateFormat.format(java.util.Date())

    //function that registers the user
// creates a new user and which returns if the user has successfully registered or not
    fun register(name: String, email: String, phone : String, password: String, callback: (Boolean, String?, User?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val user = User( uid, name, email, phone, password, createdDate)

                    database.child("users").child(uid).setValue(user)
                        .addOnCompleteListener { saveTask ->
                            if (saveTask.isSuccessful) {
                                callback(true, null, user)
                            } else {
                                callback(false, saveTask.exception?.message, null)
                            }
                        }
                } else {
                    callback(false, task.exception?.message, null)
                }
            }
    }
    fun getUsers(callback: (List<User>?) -> Unit) {
        database.child("users").get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
                callback(users)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    //this function gets the users information using their id, which will be used for when the user logs in
    private fun getUserData(uid: String, callback: (User?) -> Unit) {
        database.child("users").child(uid).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                callback(user)
            }
            .addOnFailureListener {
                callback(null)
            }
    }
    //this function logs the user in
    fun login(email: String, password: String, callback: (Boolean, String?, User?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    getUserData(uid) { user ->
                        callback(true, null, user)
                    }
                } else {
                    callback(false, task.exception?.message, null)
                }
            }
    }

    //this function is called when the user wants to log out.
    fun logout() {
        auth.signOut()
    }

    // getting the user/service provider profile
    fun getUserProfile(userId: String, callback: (Boolean, String?, User?) -> Unit) {
        database.child("users").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        callback(true, null, user)
                    } else {
                        callback(false, "User not found", null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    // updating the users about me
    fun updateAboutMe(aboutMe: String, callback: (Boolean, String?, User?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not logged in", null)
            return
        }

        val userId = currentUser.uid

        // getting the current user data first
        getUserProfile(userId) { success, error, user ->
            if (success && user != null) {
                val updatedUser = user.copy(aboutMe = aboutMe)

                database.child("users").child(userId).setValue(updatedUser)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            callback(true, null, updatedUser)
                        } else {
                            callback(false, task.exception?.message, null)
                        }
                    }
            } else {
                callback(false, error ?: "Failed to get user profile", null)
            }
        }
    }

    // when user wants to update the their profile
    fun updateUserProfile(name: String, email: String, phoneNumber: String, aboutMe: String, tagLine: String , profilePhoto: String, callback: (Boolean, String?, User?) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not logged in", null)
            return
        }

        val userId = currentUser.uid

        // Get current user data first
        getUserProfile(userId) { success, error, user ->
            if (success && user != null) {
                val updatedUser = user.copy(
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber,
                    aboutMe = aboutMe,
                    tagLine = tagLine,
                    profilePhoto = profilePhoto
                )

                database.child("users").child(userId).setValue(updatedUser)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            callback(true, null, updatedUser)
                        } else {
                            callback(false, task.exception?.message, null)
                        }
                    }
            } else {
                callback(false, error ?: "Failed to get user profile", null)
            }
        }
    }

}