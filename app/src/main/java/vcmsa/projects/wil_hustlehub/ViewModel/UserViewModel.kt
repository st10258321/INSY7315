package vcmsa.projects.wil_hustlehub.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vcmsa.projects.wil_hustlehub.Model.BookService
import vcmsa.projects.wil_hustlehub.Model.BookingActionResult
import vcmsa.projects.wil_hustlehub.Model.Chat
import vcmsa.projects.wil_hustlehub.Model.Message
import vcmsa.projects.wil_hustlehub.Model.Review
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.Model.User
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import javax.inject.Inject
import kotlin.math.log

class UserViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val serviceRepo: ServiceRepository,
    private val bookRepo: BookServiceRepository,
    private val reviewRepo: ReviewRepository,
    private val chatRepo: ChatRepository
): ViewModel() {

    // LiveData to observe all users
        val allUsers = MutableLiveData<List<User>?>()

    // LiveData to observe registration status
    val registrationStat = MutableLiveData<Triple<Boolean, String?, User?>>()
    // LiveData to observe login status and user data
    val loginStat = MutableLiveData<Pair<Boolean, String?>>()

    // LiveData to expose the currently logged-in user's data
    val currentUserData = MutableLiveData<User?>()

    // LiveData for the list of services created by the current user
    val userServices = MutableLiveData<List<Service>?>()

    // LiveData for the list of all services in the database
    val allServices = MutableLiveData<List<Service>?>()

    // LiveData to track the status of adding, deleting, or getting a single service
    val serviceStatus = MutableLiveData<Pair<Boolean, String?>>()

    // LiveData to observe the status of booking actions (create, delete, confirm, reject)
    val bookingActionStatus = MutableLiveData<BookingActionResult>()

    // LiveData for the list of bookings made by the current user
    val userBookings = MutableLiveData<List<BookService>?>()

    // LiveData for the list of bookings for the services owned by the current user
    val bookingsForMyServices = MutableLiveData<List<BookService>?>()

    val reviewStatus = MutableLiveData<Pair<Boolean, String?>>()
    val serviceProviderReviews = MutableLiveData<List<Review>?>()
    val averageRating = MutableLiveData<Pair<Double?, Int?>>() // avg rating + total reviews

    val chatStatus = MutableLiveData<Pair<Boolean, String?>>()
    val userChats = MutableLiveData<List<Chat>?>()
    val singleChat = MutableLiveData<Chat?>()
    val messageStatus = MutableLiveData<Pair<Boolean, String?>>()
    val messageList = MutableLiveData<Triple<Boolean, String?, List<Message>?>>()

    val reportResult:LiveData<Pair<Boolean, String?>> = MutableLiveData()

    val combinedData = MediatorLiveData<Pair<Map<String, String>?, List<Service>?>>().apply{
        val usersMap = MutableLiveData<Map<String, String>?>()
        val servicesList = MutableLiveData<List<Service>?>()


        addSource(allUsers){ users ->
            usersMap.value = users?.associate { user ->
                (user.userID to user.name) as Pair<String, String> }
            value  = usersMap.value to servicesList.value

        }
        addSource(allServices){ services ->
            servicesList.value = services
            value = usersMap.value to servicesList.value

        }
    }

    // Function to handle user registration
    fun register(user: User) {
        userRepo.register(user.name, user.email, user.phoneNumber, user.password) { success, message, registeredUser ->
            registrationStat.postValue(Triple(success, message, registeredUser))
        }
    }

    // Connect this to the UserRepository's login function
    fun login(email: String, password: String) {
        // Call the login method from the user repository.
        userRepo.login(email, password) { success, message, loggedInUser ->
            if (success) {
                // Update the LiveData for user data and login status
                currentUserData.postValue(loggedInUser)
                Log.d("checking--","${loggedInUser?.userID}")
                loginStat.postValue(Pair(true, email))
            } else {
                // Update LiveData with the error message
                loginStat.postValue(Pair(false, message))
                currentUserData.postValue(null)
            }
        }
    }
    fun googleLogin(idToken: String){
        if(!idToken.isEmpty()){
            loginStat.postValue(Pair(true,idToken))
        }else{
            loginStat.postValue(Pair(false,"Google Login Failed"))
        }
    }
    // Getting specific user data
    fun getUserData(uid: String): LiveData<User?> {
        val liveData = MutableLiveData<User?>()
        userRepo.getUserData(uid) { user ->
            liveData.postValue(user)
        }
        return liveData
    }
    fun getAllUsers(){
        userRepo.getUsers { users ->
            allUsers.postValue(users)
        }
    }


    // Connect this to the UserRepository's logout function
    fun logout() {
        userRepo.logout()
        // Clear LiveData to reflect the logged-out state
        currentUserData.postValue(null)
        loginStat.postValue(Pair(false, "Logged out successfully"))
    }

    // Function to add a new service
    fun addService(serviceName: String, category: String, description: String, price: Double, image: String, availability: List<String>, availableTime : List<String>, location: String) {
        serviceRepo.addService(serviceName, category, description, price, image, availability,availableTime, location) { success, message, service ->
            // Update UI state based on the result
            serviceStatus.postValue(Pair(success, message))
        }
    }
    // Functions to get all services
    fun getAllServices(){
        serviceRepo.getAllServices { success, message, services ->
            if (success) {
                allServices.postValue(services)
            } else {
                allServices.postValue(null)
                serviceStatus.postValue(Pair(false, message))
            }
        }
    }

    // Function to get all services created by the current user
    fun getUserServices() {
        serviceRepo.getUserServices { success, message, services ->
            if(services != null) {
                if (success) {
                    userServices.postValue(services) // Update the LiveData with the list of services
                } else {
                    // Handle the error, maybe by setting the list to null and updating a status message

                    serviceStatus.postValue(Pair(false, message))
                }
            }
        }
    }

    // Function to delete a service
    fun deleteService(serviceId: String) {
        serviceRepo.deleteService(serviceId) { success, message ->
            serviceStatus.postValue(Pair(success, message))
        }
    }

    // Function to get a single service
    fun getServiceById(serviceId: String): LiveData<Service?> {
        val liveData = MutableLiveData<Service?>()
        serviceRepo.getServiceById(serviceId) { success, message, service ->
            if (success) {
                liveData.postValue(service)
            } else {
                // Handle error
                liveData.postValue(null)
                serviceStatus.postValue(Pair(false, message))
            }
        }
        return liveData
    }
    fun getMyServices(serviceProviderId  : String){
        if(serviceProviderId.isNotEmpty()) {
            serviceRepo.getServicesByUserId(serviceProviderId) { success, message, services ->
                if (success) {
                    userServices.postValue(services)
                } else {
                    userServices.postValue(null)
                    serviceStatus.postValue(Pair(false, message))
                }
            }
        }
    }

    // Function to create a new booking
    fun createBooking(serviceId: String, date: String, time: String, location: String, message: String) {
        bookRepo.createBookService(serviceId, date, time, location, message) { success, message, bookService ->
            bookingActionStatus.postValue(BookingActionResult(success, message, bookService?.bookingId ?: "", ""))
        }
    }

    // Function to get all bookings made by the current user
    fun getUserBookings() {
        bookRepo.getUserBookServices { success, message, bookings ->
            if (success) {
                userBookings.postValue(bookings)
            } else {
                userBookings.postValue(null)
                bookingActionStatus.postValue(BookingActionResult(false, message, "", ""))
            }
        }
    }

    // Function to get bookings for the services owned by the current user
    fun getBookingsForMyServices() {
        bookRepo.getUserBookServices { success, message, bookings ->
            if (success) {
                bookingsForMyServices.postValue(bookings)
            } else {
                bookingsForMyServices.postValue(null)
                bookingActionStatus.postValue(BookingActionResult(false, message, "", ""))
            }
        }
    }
    //use this at provider_bookings_fragment
    fun getMyServiceProviderBookings() {
        serviceRepo.getUserServices { success, message, services ->
            if (success) {
                bookRepo.getAllBookServices { success, message, bookings ->
                    if (success) {
                        val myServiceIds = services?.map { it.serviceId } ?: emptyList()
                        val filteredBookings =
                            bookings?.filter { myServiceIds.contains(it.serviceId) }
                        bookingsForMyServices.postValue(filteredBookings)
                    }
                }
            }
        }
    }

        // Function to confirm a booking
        fun confirmBooking(bookingId: String) {
            bookRepo.confirmBooking(bookingId) { success, message, updatedBooking ->
                bookingActionStatus.postValue(BookingActionResult(true,"Booking confirmed", bookingId, "Confirmed"))
            }
        }

        // Function to reject a booking
        fun rejectBooking(bookingId: String) {
            bookRepo.rejectBooking(bookingId) { success, message, updatedBooking ->
                bookingActionStatus.postValue(BookingActionResult(true,"Booking rejected", bookingId, "Rejected"))
            }
        }

        // Function to delete a booking
        fun deleteBooking(bookingId: String) {
            bookRepo.deleteBookService(bookingId) { success, message ->
                bookingActionStatus.postValue(BookingActionResult(true,"Booking deleted", bookingId, ""))
            }
        }
//    fun reportServiceProvider(serviceProviderId: String, serviceId: String, reportedIssue: String, additionalNotes: String, images: String) {
//        serviceRepo.reportServiceProvider(serviceProviderId, serviceId, reportedIssue, additionalNotes, images) { success, message ->
//            serviceStatus.postValue(Pair(success, message))
//        }
//
//    }
    // ------------------ REVIEWS ------------------
    fun addReview(serviceId: String, stars: Int, reviewText: String) {
        reviewRepo.addReview(serviceId, stars, reviewText) { success, message, review ->
            if (success) {
                reviewStatus.postValue(Pair(true, "Review added successfully"))
            } else {
                reviewStatus.postValue(Pair(false, message))
            }
        }
    }

    fun getReviewsForServiceProvider(userId: String) {
        reviewRepo.getReviewsForServiceProvider(userId) { success, message, reviews ->
            if (success) {
                serviceProviderReviews.postValue(reviews)
            } else {
                serviceProviderReviews.postValue(null)
                reviewStatus.postValue(Pair(false, message))
            }
        }
    }

    fun getServiceProviderAverageRating(userId: String) {
        reviewRepo.getServiceProviderAverageRating(userId) { success, message, avg, total ->
            if (success) {
                averageRating.postValue(Pair(avg, total))
            } else {
                averageRating.postValue(Pair(null, null))
                reviewStatus.postValue(Pair(false, message))
            }
        }
    }

    fun deleteReview(reviewId: String) {
        reviewRepo.deleteReview(reviewId) { success, message ->
            reviewStatus.postValue(Pair(success, message))
        }
    }

    // ------------------ CHATS ------------------
    fun createChat(serviceId: String) {
        chatRepo.createChat(serviceId) { success, message, chat ->
            if (success) {
                singleChat.postValue(chat)
            } else {
                chatStatus.postValue(Pair(false, message))
            }
        }
    }

    fun getUserChats() {
        chatRepo.getUserChats { success, message, chats ->
            if (success) {
                userChats.postValue(chats)
            } else {
                userChats.postValue(null)
                chatStatus.postValue(Pair(false, message))
            }
        }
    }

    fun getChatById(chatId: String) {
        chatRepo.getChatById(chatId) { success, message, chat ->
            if (success) {
                singleChat.postValue(chat)
            } else {
                singleChat.postValue(null)
                chatStatus.postValue(Pair(false, message))
            }
        }
    }
    fun loadMessages(chatId : String){
        chatRepo.loadMessages(chatId){ success, message , messages ->
            messageList.postValue(Triple(success,message,messages))
        }
    }
    fun sendMessage(chatId: String, message: String) {
        chatRepo.sendMessage(chatId, message) { success, error, _ ->
            messageStatus.postValue(Pair(success, error))
        }
    }
    fun reportServiceProvider(serviceProviderId:String, serviceId :String, reportedIssue : String,additionalNotes :String, images : String){
        serviceRepo.reportServiceProvider(serviceProviderId, serviceId, reportedIssue, additionalNotes, images) { success, message ->
                if(success){
                    reviewStatus.postValue(Pair(true,message))
                }else{
                    reviewStatus.postValue(Pair(false,message))
                }
        }
    }

}