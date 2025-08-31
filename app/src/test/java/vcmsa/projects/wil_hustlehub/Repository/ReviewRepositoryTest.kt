package vcmsa.projects.wil_hustlehub.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import vcmsa.projects.wil_hustlehub.Model.Review
import vcmsa.projects.wil_hustlehub.Model.User
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

//Testing fuctions of ReviewRepository Class
class ReviewRepositoryTest {

    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockUser: FirebaseUser
    private lateinit var mockDatabase: DatabaseReference
    private lateinit var repository: ReviewRepository

    @Before
    fun setUp() {
        mockAuth = mockk()
        mockUser = mockk()
        mockDatabase = mockk(relaxed = true)

        // Mock static Firebase methods
        mockkStatic(FirebaseAuth::class)
        mockkStatic(FirebaseDatabase::class)

        every { FirebaseAuth.getInstance() } returns mockAuth
        every { FirebaseDatabase.getInstance().reference } returns mockDatabase
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns "testUserId"

        repository = ReviewRepository()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `addReview returns error when user not logged in`() {
        every { mockAuth.currentUser } returns null

        repository.addReview("serviceId", 5, "Great service!") { success, error, review ->
            assertFalse(success)
            assertEquals("User not logged in", error)
            assertNull(review)
        }
    }

    @Test
    fun `addReview succeeds when user is logged in`() {
        val userSnapshot = mockk<DataSnapshot>()
        val reviewTask = mockk<Task<Void>>()
        every { reviewTask.isSuccessful } returns true

        val user = User(userID = "testUserId", name = "John Doe")
        every { userSnapshot.getValue(User::class.java) } returns user

        val userRef = mockk<DatabaseReference>()
        every { mockDatabase.child("users").child("testUserId") } returns userRef

        // Simulate retrieving user from database
        every { userRef.addListenerForSingleValueEvent(any()) } answers {
            val listener = arg<ValueEventListener>(0)
            listener.onDataChange(userSnapshot)
        }

        val reviewRef = mockk<DatabaseReference>()
        every { mockDatabase.child("Reviews").child(any()) } returns reviewRef

        // Simulate setting review value
        every { reviewRef.setValue(any()).addOnCompleteListener(any<OnCompleteListener<Void>>()) } answers {
            val listener = arg<OnCompleteListener<Void>>(0)
            listener.onComplete(reviewTask)
            reviewTask
        }

        repository.addReview("serviceId", 5, "Great service!") { success, error, review ->
            assertTrue(success)
            assertNull(error)
            assertEquals("John Doe", review?.reviewerName)
            assertEquals(5, review?.stars)
        }
    }

    @Test
    fun `getReviewsForServiceProvider returns sorted reviews`() {
        val review = Review(
            reviewId = "r1",
            userId = "testUserId",
            serviceId = "service1",
            reviewerName = "John",
            stars = 5,
            reviewText = "Good",
            reviewDate = "2025-08-31 12:00:00"
        )

        val childSnapshot = mockk<DataSnapshot>()
        every { childSnapshot.getValue(Review::class.java) } returns review

        val reviewsSnapshot = mockk<DataSnapshot>()
        every { reviewsSnapshot.children } returns listOf(childSnapshot)

        val serviceRef = mockk<DatabaseReference>()
        every { mockDatabase.child("Services").orderByChild("userId").equalTo("testUserId") } returns serviceRef

        // Simulate fetching reviews from database
        every { serviceRef.addListenerForSingleValueEvent(any()) } answers {
            val listener = arg<ValueEventListener>(0)
            listener.onDataChange(reviewsSnapshot)
        }

        repository.getReviewsForServiceProvider("testUserId") { success, error, reviews ->
            assertTrue(success)
            assertNull(error)
            assertEquals(1, reviews?.size)
            assertEquals("r1", reviews?.first()?.reviewId)
        }
    }

    @Test
    fun `deleteReview returns error when user not logged in`() {
        every { mockAuth.currentUser } returns null

        repository.deleteReview("reviewId") { success, error ->
            assertFalse(success)
            assertEquals("User not logged in", error)
        }
    }

    @Test
    fun `deleteReview succeeds when user owns review`() {
        val reviewSnapshot = mockk<DataSnapshot>()
        val review = Review(reviewId = "reviewId", userId = "testUserId")
        every { reviewSnapshot.getValue(Review::class.java) } returns review

        val reviewRef = mockk<DatabaseReference>()
        every { mockDatabase.child("Reviews").child("reviewId") } returns reviewRef

        // Simulate fetching review to verify ownership
        every { reviewRef.addListenerForSingleValueEvent(any()) } answers {
            val listener = arg<ValueEventListener>(0)
            listener.onDataChange(reviewSnapshot)
        }

        val removeTask = mockk<Task<Void>>()
        every { removeTask.isSuccessful } returns true

        // Simulate deletion task completion
        every { reviewRef.removeValue().addOnCompleteListener(any<OnCompleteListener<Void>>()) } answers {
            val listener = arg<OnCompleteListener<Void>>(0)
            listener.onComplete(removeTask)
            removeTask
        }

        repository.deleteReview("reviewId") { success, error ->
            assertTrue(success)
            assertNull(error)
        }
    }
}
