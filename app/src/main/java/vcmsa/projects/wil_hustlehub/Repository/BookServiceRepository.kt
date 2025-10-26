package vcmsa.projects.wil_hustlehub.Repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vcmsa.projects.wil_hustlehub.Model.BookService
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.Model.User
import java.util.Date

class BookServiceRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    //making the date and time readable in the database by using specific format
    private val dateFormat = java.text.SimpleDateFormat("yyyy/MM/dd", java.util.Locale.getDefault())
    private val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())

    private val createdDateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    private val createdDate = createdDateFormat.format(java.util.Date())

    //created a cache to temporarily store service details instead of retrieving them from firebase every time.
    private val serviceCache = mutableMapOf<String, Service>()
    private val userRepo = UserRepository()

    //the service details expires after 5 minutes.
    private val cacheExpiry = 5 * 60 * 1000L

    /*this function takes the information like servicename,
     date and time and saves it to the database
     and the current userId of the user that is logged in using indexing and denormalization*/
    fun createBookService(serviceId: String, date: String, time: String, location: String, message: String, callback: (Boolean, String?, BookService?) -> Unit
    ) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(false, "User not logged in", null)
            return
        }



        // checking cache first. looks for the service inside the cache.
        val cachedService = serviceCache[serviceId]
        //checks if service exists in the cache. if it exists it creates the booking immediately.
        if (cachedService != null) {
            createBookingWithService(userId, serviceId, cachedService, date, time, location, message, callback)
            return
        }

        // if the service didnt exist in the cache it now fetches the service from the database.
        // also reads the service details once in the database.
        database.child("Services").child(serviceId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(serviceSnapshot: DataSnapshot) {
                    val service = serviceSnapshot.getValue(Service::class.java)
                    if (service == null) {
                        callback(false, "Service not found", null)
                        return
                    }

                    // now we then cache the service details and store them temporarily
                    serviceCache[serviceId] = service
                    //and them creates the booking.
                    createBookingWithService(userId, serviceId, service, date, time, location, message, callback)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }

    /*this method does the actually saving to the database after getting the cached service*/
    fun createBookingWithService(userId: String, serviceId: String ,service: Service, date: String, time: String, location: String, message: String, callback: (Boolean, String?, BookService?) -> Unit
    ) {

        // Create the booking service ID
        val bookServiceId = database.child("Book_Service").push().key ?: ""

        //makes sure the date and time are always in the correct format
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
        val userName = auth.currentUser?.displayName

        Log.d("--booking-name-","$userName")
        // denormalizing so that it stores the service provider id for easier queries
        val bookService = BookService(
            bookingId = bookServiceId,
            userId = userId,
            serviceId = serviceId,
            serviceName = service.serviceName,
            serviceProviderId = service.userId,
            date = finalDate,
            time = finalTime,
            location = location,
            status = "Pending",
            message = message,
            userName = userName ?: ""
        )

        //updating to two different paths in the database at the same time
        val updates = hashMapOf<String, Any>(
            "/Book_Service/$bookServiceId" to bookService,
            // Optional: Create index for service owner's bookings
            // "ServiceOwnerBookings" creates an index inside the database to store the bookings that belong to a specific service provider
            "/ServiceProviderBookings/${service.userId}/$bookServiceId" to true
        )

        //saving to the database
        database.updateChildren(updates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null, bookService)
                } else {
                    callback(false, task.exception?.message, null)
                }
            }

   }

    /* with this function we are getting all the bookings that are inside the database
     that belong to the user that is logged in and returns then as a list, using indexing and limiting the amount of booked services that is shown from the database */
    fun getUserBookServices(limit: Int = 50, callback: (Boolean, String?, List<BookService>?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(false, "User not logged in", null)
            return
        }

        database.child("Book_Service")
            .orderByChild("userId")
            .equalTo(userId)
            .limitToFirst(limit)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bookServices = snapshot.children.mapNotNull {
                        it.getValue(BookService::class.java)
                    }
                    callback(true, null, bookServices)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }


    /* this function is for the admin portal where it gets all the bookings that are saved inside
     the databaseThis function retrieves every single booking in the entire database from all users,
     */
    fun getAllBookServices(limit: Int = 100, startAfter: String? = null, callback: (Boolean, String?, List<BookService>?) -> Unit) {
        var query = database.child("Book_Service")
            .orderByChild("createdDate")
            .limitToFirst(limit)

        startAfter?.toDoubleOrNull()?.let { startAfterValue ->
            query = query.startAfter(startAfterValue)
        }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookServices = snapshot.children.mapNotNull {
                    it.getValue(BookService::class.java)
                }
                callback(true, null, bookServices)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }

    /*this function searches for a booking and returns specific booking details by using the
      bookingId */
    fun getBookServiceById(bookServiceId: String, callback: (Boolean, String?, BookService?) -> Unit) {
        database.child("Book_Service").child(bookServiceId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bookService = snapshot.getValue(BookService::class.java)
                    if (bookService != null) {
                        callback(true, null, bookService)
                    } else {
                        callback(false, "Booking not found", null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }


    //  getting all bookings for users that are service providers, optimized the code by using the denormalized serviceProviderId
    fun getBookingsForMyServices(callback: (Boolean, String?, List<BookService>?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(false, "User not logged in", null)
            return
        }

        database.child("Book_Service")
            .orderByChild("serviceProviderId")
            .equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bookServices = snapshot.children.mapNotNull {
                        it.getValue(BookService::class.java)
                    }
                    callback(true, null, bookServices)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
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
                //updating the paths for the booking
                val updates = hashMapOf<String, Any?>(
                    "/Book_Service/$bookServiceId" to null,
                    "/ServiceProviderBookings/${bookService.serviceProviderId}/$bookServiceId" to null
                )

                database.updateChildren(updates)
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


    /* to confirm a booking this can only be done by the person who registered/owns the service, by using the serviceProviderId for validation */
    fun confirmBooking(bookServiceId: String, callback: (Boolean, String?, BookService?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(false, "User not logged in", null)
            return
        }

        // check if the booking exists and the current user is the service provider
        getBookServiceById(bookServiceId) { success, error, bookService ->
            if (success && bookService != null) {
                // using denormalized serviceProviderId
                if (bookService.serviceProviderId == userId) {
                    val updates = hashMapOf<String, Any>(
                        "/Book_Service/$bookServiceId/status" to "Confirmed"
                    )

                    database.updateChildren(updates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val updatedBookService = bookService.copy(status = "Confirmed")
                                callback(true, null, updatedBookService)
                            } else {
                                callback(false, task.exception?.message, null)
                            }
                        }
                } else {
                    callback(false, "You can only confirm bookings for your own services", null)
                }
            } else {
                callback(false, error ?: "Booking not found", null)
            }
        }
    }

    /* to reject a booking this can only be done by the person who registered/owns the service */
    fun rejectBooking(bookServiceId: String, callback: (Boolean, String?, BookService?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback(false, "User not logged in", null)
            return
        }

        // check if the booking exists and the current user is the service provider
        getBookServiceById(bookServiceId) { success, error, bookService ->
            if (success && bookService != null) {
                if (bookService.serviceProviderId == userId) {
                    val updates = hashMapOf<String, Any>(
                        "/Book_Service/$bookServiceId/status" to "Rejected"
                    )

                    database.updateChildren(updates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val updatedBookService = bookService.copy(status = "Rejected")
                                callback(true, null, updatedBookService)
                            } else {
                                callback(false, task.exception?.message, null)
                            }
                        }
                } else {
                    callback(false, "You can only reject bookings for your own services", null)
                }
            } else {
                callback(false, error ?: "Booking not found", null)
            }
        }
    }
    fun updateBooking(bookService : BookService, callback: (Boolean, String?, BookService?) -> Unit){
        database.child("Book_Service").child(bookService.bookingId).setValue(bookService)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    callback(true, null, bookService)
                }else{
                    callback(false, task.exception?.message, null)
                }
            }
        Log.d("--checking","${bookService.status}")
    }

    //clear the cache when done with the operations
    fun clearCache() {
        serviceCache.clear()
    }

}