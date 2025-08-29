package vcmsa.projects.wil_hustlehub.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import vcmsa.projects.wil_hustlehub.Model.User

open class UserRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: DatabaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().reference
) {
    // Making the date and time readable in the database
    private val createdDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    private val createdDate = createdDateFormat.format(java.util.Date())

    /**
     * Function that registers the user
     * Creates a new user and returns via callback if the user has successfully registered
     */
    open fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        callback: (Boolean, String?, User?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val user = User(uid, name, email, phone, password, createdDate)

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

    /**
     * This function gets the user's information using their id
     */
     fun getUserData(uid: String, callback: (User?) -> Unit) {
        database.child("users").child(uid).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                callback(user)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    /**
     * This function logs the user in
     */
    open fun login(
        email: String,
        password: String,
        callback: (Boolean, String?, User?) -> Unit
    ) {
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

    /**
     * This function logs the user out
     */
    fun logout() {
        auth.signOut()
    }
    /**
     * This function gets all the users in the database
     */
    fun getUsers(callback: (List<User>?) -> Unit) {
        database.child("users").get()
            .addOnSuccessListener { snapshot ->
                val users = mutableListOf<User>()
                for (child in snapshot.children) {
                    val user = child.getValue(User::class.java)
                    user?.let { users.add(it) }
                }
                callback(users)
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}
