package vcmsa.projects.wil_hustlehub.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import vcmsa.projects.wil_hustlehub.Model.User


class UserRepository {
    // Where all the firebase is being done
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    // Making the date and time readable in the database
    val createdDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    val createdDate = createdDateFormat.format(java.util.Date())

    /* Function that registers the user
       Creates a new user and which returns if the user has successfully registered or not */
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

    // This function gets the users information using their id, which will be used for when the user logs in
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
    // This function logs the user in
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
    // This function is called when the user wants to log out.
    fun logout() {
        auth.signOut()
    }
}