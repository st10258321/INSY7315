package vcmsa.projects.wil_hustlehub.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import vcmsa.projects.wil_hustlehub.Model.BookService

class BookServiceRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    //making the date and time readable in the database by using specific format
    private val dateFormat = java.text.SimpleDateFormat("yyyy/MM/dd", java.util.Locale.getDefault())
    private val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())


   /*this function takes the information like servicename,
   date and time and saves it to the database
   and the current userId of the user that is logged in.*/
    fun createBookService(serviceName: String, date: String, time: String, location: String, message: String, callback: (Boolean, String?, BookService?) -> Unit
    ) {
        // gets current user ID
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(false, "User not logged in", null)
            return
        }

        // creates the booking service id
        val bookServiceId = database.child("Book_Service").push().key ?: ""


        val finalDate = if (date.isEmpty()) {
            dateFormat.format(java.util.Date())
        } else {
            date
        }


        val finalTime = if (time.isEmpty()) {
            timeFormat.format(java.util.Date())
        } else {
            time
        }

        // creates the BookService object
        val bookService = BookService(
            bookingId = bookServiceId,
            userId = userId,
            serviceName = serviceName,
            date = finalDate,
            time = finalTime,
            location = location,
            message = message
        )

        // saves to the database
        database.child("Book_Service").child(bookServiceId).setValue(bookService)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null, bookService)
                } else {
                    callback(false, task.exception?.message, null)
                }
            }
    }

    /* with this function we are getting all the bookings that are inside the database
     that belong to the user that is logged in and returns then as a list
    */
    fun getUserBookServices(callback: (Boolean, String?, List<BookService>?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(false, "User not logged in", null)
            return
        }

        database.child("Book_Service")
            .orderByChild("userId")
            .equalTo(userId)
            .get()
            .addOnSuccessListener { snapshot ->
                val bookServices = mutableListOf<BookService>()
                for (child in snapshot.children) {
                    val bookService = child.getValue(BookService::class.java)
                    bookService?.let { bookServices.add(it) }
                }
                callback(true, null, bookServices)
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message, null)
            }
    }


    /* this function is for the admin portal where it gets all the bookings that are saved inside
     the databaseThis function retrieves every single booking in the entire database from all users,
     */
    fun getAllBookServices(callback: (Boolean, String?, List<BookService>?) -> Unit) {
        database.child("Book_Service")
            .get()
            .addOnSuccessListener { snapshot ->
                val bookServices = mutableListOf<BookService>()
                for (child in snapshot.children) {
                    val bookService = child.getValue(BookService::class.java)
                    bookService?.let { bookServices.add(it) }
                }
                callback(true, null, bookServices)
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message, null)
            }
    }

    /*this function searches for a booking and returns specific booking details by using the
      bookingId */
    fun getBookServiceById(bookServiceId: String, callback: (Boolean, String?, BookService?) -> Unit) {
        database.child("Book_Service").child(bookServiceId)
            .get()
            .addOnSuccessListener { snapshot ->
                val bookService = snapshot.getValue(BookService::class.java)
                if (bookService != null) {
                    callback(true, null, bookService)
                } else {
                    callback(false, "Booking not found", null)
                }
            }
            .addOnFailureListener { exception ->
                callback(false, exception.message, null)
            }
    }

    /* this function can be used for when the user wants to cancle/delete a booking
    // which permanently removes that booking from the database.
    */
    fun deleteBookService(bookServiceId: String, callback: (Boolean, String?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(false, "User not logged in")
            return
        }

        //  checking if the booking exists and belongs to the current user
        getBookServiceById(bookServiceId) { success, error, bookService ->
            if (success && bookService != null && bookService.userId == userId) {
                database.child("Book_Service").child(bookServiceId).removeValue()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            callback(true, null)
                        } else {
                            callback(false, task.exception?.message)
                        }
                    }
            } else {
                callback(false, "Booking not found or invalid user")
            }
        }
    }

}