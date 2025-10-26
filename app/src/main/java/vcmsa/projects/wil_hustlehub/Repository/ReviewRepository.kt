package vcmsa.projects.wil_hustlehub.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vcmsa.projects.wil_hustlehub.Model.Review
import vcmsa.projects.wil_hustlehub.Model.User

class ReviewRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    // using specific format
    private val dateFormat =
        java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())

    //caching the user data
    private val userCache = mutableMapOf<String, String>()

    // adding/ creating a new review
    fun addReview(
        serviceId: String,
        stars: Int,
        reviewText: String,
        callback: (Boolean, String?, Review?) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not logged in", null)
            return
        }

        val userId = currentUser.uid
        val reviewId = database.child("Reviews").push().key ?: ""
        val reviewDate = dateFormat.format(java.util.Date())

        // OPTIMIZATION: Check cache first to avoid duplicate user queries
        if (userCache.containsKey(userId)) {
            val reviewerName = userCache[userId] ?: "Anonymous"
            saveReview(reviewId, userId, serviceId, reviewerName, stars, reviewText, reviewDate, callback)
        } else {
            // Get the current user's name first
            database.child("users").child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(userSnapshot: DataSnapshot) {
                        val user = userSnapshot.getValue(User::class.java)
                        val reviewerName = user?.name ?: "Anonymous"

                        // Cache the username
                        userCache[userId] = reviewerName

                        saveReview(reviewId, userId, serviceId, reviewerName, stars, reviewText, reviewDate, callback)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback(false, error.message, null)
                    }
                })
        }
    }

    private fun saveReview(
        reviewId: String,
        userId: String,
        serviceId: String,
        reviewerName: String,
        stars: Int,
        reviewText: String,
        reviewDate: String,
        callback: (Boolean, String?, Review?) -> Unit
    ) {
        val review = Review(
            reviewId = reviewId,
            userId = userId,
            serviceId = serviceId,
            reviewerName = reviewerName,
            stars = stars,
            reviewText = reviewText,
            reviewDate = reviewDate
        )

        database.child("Reviews").child(reviewId).setValue(review)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null, review)
                } else {
                    callback(false, task.exception?.message, null)
                }
            }
    }

    // getting all the reviews for a specific service provider, using indexing
    fun getReviewsForServiceProvider(providerId: String, callback: (Boolean, String?, List<Review>?) -> Unit) {
        // Step 1: Get all services belonging to this provider
        database.child("Services")
            .orderByChild("userId")
            .equalTo(providerId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(servicesSnapshot: DataSnapshot) {
                    val serviceIds = servicesSnapshot.children.mapNotNull {
                        it.key // or it.child("serviceId").value?.toString()
                    }

                    if (serviceIds.isEmpty()) {
                        callback(true, "No services found for provider", emptyList())
                        return
                    }

                    // Step 2: Get all reviews for those serviceIds
                    database.child("Reviews")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(reviewsSnapshot: DataSnapshot) {
                                val reviews = mutableListOf<Review>()

                                for (reviewChild in reviewsSnapshot.children) {
                                    val review = reviewChild.getValue(Review::class.java)
                                    if (review != null && serviceIds.contains(review.serviceId)) {
                                        reviews.add(review)
                                    }
                                }

                                val sortedReviews = reviews.sortedByDescending {
                                    try {
                                        dateFormat.parse(it.reviewDate)?.time ?: 0L
                                    } catch (e: Exception) {
                                        0L
                                    }
                                }

                                callback(true, null, sortedReviews)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                callback(false, error.message, null)
                            }
                        })
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message, null)
                }
            })
    }


    // this function calculates the overall rating for the service provider
    fun getServiceProviderAverageRating(userID: String, callback: (Boolean, String?, Double?, Int?) -> Unit
    ) {

        getReviewsForServiceProvider(userID) { success, error, reviews ->
            if (success && reviews != null) {
                if (reviews.isNotEmpty()) {
                    // Use sumOf directly
                    val totalStars = reviews.sumOf { it.stars }
                    val averageRating = totalStars.toDouble() / reviews.size
                    val totalReviews = reviews.size
                    callback(true, null, averageRating, totalReviews)
                } else {

                    callback(true, null, 0.0, 0)
                }
            } else {
                callback(false, error, null, null)
            }
        }
    }



    // when the user that created the review wants to delete it
    fun deleteReview(reviewId: String, callback: (Boolean, String?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            callback(false, "User not logged in")
            return
        }

        val userId = currentUser.uid

        // checking if review exists and belongs to current user
        database.child("Reviews").child(reviewId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val review = snapshot.getValue(Review::class.java)
                    if (review == null) {
                        callback(false, "Review not found")
                        return
                    }

                    if (review.userId != userId) {
                        callback(false, "You can only delete your own reviews")
                        return
                    }

                    // deleting the review
                    database.child("Reviews").child(reviewId).removeValue()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                callback(true, null)
                            } else {
                                callback(false, task.exception?.message)
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(false, error.message)
                }
            })
    }

}