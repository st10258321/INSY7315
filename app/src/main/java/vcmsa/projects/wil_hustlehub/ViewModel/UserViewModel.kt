package vcmsa.projects.wil_hustlehub.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vcmsa.projects.wil_hustlehub.Model.BookService
import vcmsa.projects.wil_hustlehub.Model.Chat
import vcmsa.projects.wil_hustlehub.Model.Review
import vcmsa.projects.wil_hustlehub.Model.Service
import vcmsa.projects.wil_hustlehub.Model.User
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ChatRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val serviceRepo: ServiceRepository,
    private val bookRepo: BookServiceRepository,
    private val reviewRepo: ReviewRepository,
    private val chatRepo: ChatRepository
) : ViewModel() {

    // ========== EXISTING LIVE DATA ==========
    val allUsers = MutableLiveData<List<User>?>()
    val registrationStat = MutableLiveData<Triple<Boolean, String?, User?>>()
    val loginStat = MutableLiveData<Pair<Boolean, String?>>()
    val currentUserData = MutableLiveData<User?>()

    val userServices = MutableLiveData<List<Service>>()
    val allServices = MutableLiveData<List<Service>?>()
    val serviceStatus = MutableLiveData<Pair<Boolean, String?>>()

    val bookingActionStatus = MutableLiveData<Pair<Boolean, String?>>()
    val userBookings = MutableLiveData<List<BookService>?>()
    val bookingsForMyServices = MutableLiveData<List<BookService>?>()

    // ========== NEW LIVE DATA ==========
    val reviewStatus = MutableLiveData<Pair<Boolean, String?>>()
    val serviceProviderReviews = MutableLiveData<List<Review>?>()
    val averageRating = MutableLiveData<Pair<Double?, Int?>>() // avg rating + total reviews

    val chatStatus = MutableLiveData<Pair<Boolean, String?>>()
    val userChats = MutableLiveData<List<Chat>?>()
    val singleChat = MutableLiveData<Chat?>()
    val messageStatus = MutableLiveData<Pair<Boolean, String?>>()

    // ------------------ USERS ------------------
    fun register(user: User) {
        userRepo.register(user.name, user.email, user.phoneNumber, user.password) { success, message, registeredUser ->
            registrationStat.postValue(Triple(success, message, registeredUser))
        }
    }

    fun login(email: String, password: String) {
        userRepo.login(email, password) { success, message, loggedInUser ->
            if (success) {
                currentUserData.postValue(loggedInUser)
                loginStat.postValue(Pair(true, null))
            } else {
                loginStat.postValue(Pair(false, message))
                currentUserData.postValue(null)
            }
        }
    }

    fun getUserData(uid: String): LiveData<User?> {
        val liveData = MutableLiveData<User?>()
        userRepo.getUserData(uid) { user ->
            liveData.postValue(user)
        }
        return liveData
    }

    fun getAllUsers() {
        userRepo.getUsers { users ->
            allUsers.postValue(users)
        }
    }

    fun logout() {
        userRepo.logout()
        currentUserData.postValue(null)
        loginStat.postValue(Pair(false, "Logged out successfully"))
    }

    // ------------------ SERVICES ------------------
    fun addService(
        serviceName: String,
        category: String,
        description: String,
        price: Double,
        image: String,
        availabileDay: List<String>,
        availabileTime: List<String>,
        location: String
    ) {
        serviceRepo.addService(serviceName, category, description, price, image, availabileDay, availabileTime, location) { success, message, _ ->
            serviceStatus.postValue(Pair(success, message))
        }
    }

    fun getAllServices() {
        serviceRepo.getAllServices { success, message, services ->
            if (success) {
                allServices.postValue(services)
            } else {
                allServices.postValue(null)
                serviceStatus.postValue(Pair(false, message))
            }
        }
    }

    fun getUserServices() {
        serviceRepo.getUserServices { success, message, services ->
            if (services != null) {
                if (success) {
                    userServices.postValue(services)
                } else {
                    serviceStatus.postValue(Pair(false, message))
                }
            }
        }
    }

    fun deleteService(serviceId: String) {
        serviceRepo.deleteService(serviceId) { success, message ->
            serviceStatus.postValue(Pair(success, message))
        }
    }

    fun getServiceById(serviceId: String): LiveData<Service?> {
        val liveData = MutableLiveData<Service?>()
        serviceRepo.getServiceById(serviceId) { success, message, service ->
            if (success) {
                liveData.postValue(service)
            } else {
                liveData.postValue(null)
                serviceStatus.postValue(Pair(false, message))
            }
        }
        return liveData
    }

    // ------------------ BOOKINGS ------------------
    fun createBooking(serviceId: String, date: String, time: String, location: String, message: String) {
        bookRepo.createBookService(serviceId, date, time, location, message) { success, msg, _ ->
            bookingActionStatus.postValue(Pair(success, msg))
        }
    }

    fun getUserBookings() {
        bookRepo.getUserBookServices { success, message, bookings ->
            if (success) {
                userBookings.postValue(bookings)
            } else {
                userBookings.postValue(null)
                bookingActionStatus.postValue(Pair(false, message))
            }
        }
    }

    fun getBookingsForMyServices() {
        bookRepo.getBookingsForMyServices { success, message, bookings ->
            if (success) {
                bookingsForMyServices.postValue(bookings)
            } else {
                bookingsForMyServices.postValue(null)
                bookingActionStatus.postValue(Pair(false, message))
            }
        }
    }

    fun confirmBooking(bookingId: String) {
        bookRepo.confirmBooking(bookingId) { success, message, _ ->
            bookingActionStatus.postValue(Pair(success, message))
        }
    }

    fun rejectBooking(bookingId: String) {
        bookRepo.rejectBooking(bookingId) { success, message, _ ->
            bookingActionStatus.postValue(Pair(success, message))
        }
    }

    fun deleteBooking(bookingId: String) {
        bookRepo.deleteBookService(bookingId) { success, message ->
            bookingActionStatus.postValue(Pair(success, message))
        }
    }

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

    fun sendMessage(chatId: String, message: String) {
        chatRepo.sendMessage(chatId, message) { success, error, _ ->
            messageStatus.postValue(Pair(success, error))
        }
    }
}