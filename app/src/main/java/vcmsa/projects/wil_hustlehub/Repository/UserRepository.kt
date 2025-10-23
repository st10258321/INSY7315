package vcmsa.projects.wil_hustlehub.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import vcmsa.projects.wil_hustlehub.Model.User
import java.util.Date

class UserRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: DatabaseReference = com.google.firebase.database.FirebaseDatabase.getInstance().reference
) {
    //storing the users in cache
    private var cachedUser: User? = null

    // Making the date and time readable in the database
    private val createdDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    private val createdDate = createdDateFormat.format(java.util.Date())

    /**
     * Function that registers the user
     * Creates a new user and returns via callback if the user has successfully registered
     */
    fun register(
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
                    val createdDate = createdDateFormat.format(java.util.Date())


                    val user = User(uid, name, email, phone, "", createdDate = createdDate)

                    database.child("users").child(uid).setValue(user)
                        .addOnCompleteListener { saveTask ->
                            if (saveTask.isSuccessful) {
                                cachedUser = user // Cache the user
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
    fun getUserData(uid: String, forceRefresh: Boolean = false, callback: (User?) -> Unit) {
        // check if this user is cached and return it immediately
        if (cachedUser?.userID == uid) {
            callback(cachedUser)
            return
        }


        database.child("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    cachedUser = user // Cache it for next time
                    callback(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }
            })
    }

    /**
     * This function logs the user in
     */
    fun login(
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
        cachedUser = null // clear cached data
        auth.signOut()
    }

    //get the cached user for performance
    fun getCurrentUser(): User? {
        return cachedUser
    }

    //when user wants to update their profile
    fun updateUserProfile(
        name: String? = null,
        phone: String? = null,
        callback: (Boolean, String?) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            callback(false, "User not logged in")
            return
        }

        val updates = mutableMapOf<String, Any>()
        name?.let { updates["name"] = it }
        phone?.let { updates["phone"] = it }

        if (updates.isEmpty()) {
            callback(false, "Nothing to update")
            return
        }

        database.child("users").child(uid).updateChildren(updates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update cache too
                    cachedUser?.let { user ->
                        cachedUser = user.copy(
                            name = name ?: user.name,
                            phoneNumber = phone ?: user.phoneNumber
                        )
                    }
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
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
}
