package vcmsa.projects.wil_hustlehub.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import vcmsa.projects.wil_hustlehub.Model.BookService
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

//Testing fuctions of BookServiceRepository Class
class BookServiceRepositoryTest {

    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockUser: FirebaseUser
    private lateinit var mockDatabase: DatabaseReference
    private lateinit var repository: BookServiceRepository

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

        repository = BookServiceRepository()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `createBookService returns error when user not logged in`() {
        every { mockAuth.currentUser } returns null

        repository.createBookService("serviceId", "2025/08/31", "12:00", "Location", "Message") { success, error, bookService ->
            assertFalse(success)
            assertEquals("User not logged in", error)
            assertNull(bookService)
        }
    }

    @Test
    fun `getUserBookServices returns error when user not logged in`() {
        every { mockAuth.currentUser } returns null

        repository.getUserBookServices { success, error, list ->
            assertFalse(success)
            assertEquals("User not logged in", error)
            assertNull(list)
        }
    }

    @Test
    fun `getAllBookServices returns empty list successfully`() {
        val fakeRepo = spyk(repository)

        // Mock getAllBookServices to directly call the callback with empty list
        every { fakeRepo.getAllBookServices(any()) } answers {
            val callback = it.invocation.args[0] as (Boolean, String?, List<BookService>?) -> Unit
            callback(true, null, emptyList())
        }

        fakeRepo.getAllBookServices { success, error, list ->
            assertTrue(success)
            assertNull(error)
            assertEquals(0, list?.size)
        }
    }

    @Test
    fun `getBookServiceById returns booking not found`() {
        val fakeRepo = spyk(repository)

        // Mock getBookServiceById to call the callback with failure
        every { fakeRepo.getBookServiceById("id", any()) } answers {
            val callback = it.invocation.args[1] as (Boolean, String?, BookService?) -> Unit
            callback(false, "Booking not found", null)
        }

        fakeRepo.getBookServiceById("id") { success, error, bookService ->
            assertFalse(success)
            assertEquals("Booking not found", error)
            assertNull(bookService)
        }
    }

    @Test
    fun `deleteBookService returns error when user not logged in`() {
        every { mockAuth.currentUser } returns null

        repository.deleteBookService("id") { success, error ->
            assertFalse(success)
            assertEquals("User not logged in", error)
        }
    }

    @Test
    fun `confirmBooking returns error when user not logged in`() {
        every { mockAuth.currentUser } returns null

        repository.confirmBooking("id") { success, error, bookService ->
            assertFalse(success)
            assertEquals("User not logged in", error)
            assertNull(bookService)
        }
    }

    @Test
    fun `rejectBooking returns error when user not logged in`() {
        every { mockAuth.currentUser } returns null

        repository.rejectBooking("id") { success, error, bookService ->
            assertFalse(success)
            assertEquals("User not logged in", error)
            assertNull(bookService)
        }
    }

    @Test
    fun `getBookingsForMyServices returns error when user not logged in`() {
        every { mockAuth.currentUser } returns null

        repository.getBookingsForMyServices { success, error, list ->
            assertFalse(success)
            assertEquals("User not logged in", error)
            assertNull(list)
        }
    }
}
