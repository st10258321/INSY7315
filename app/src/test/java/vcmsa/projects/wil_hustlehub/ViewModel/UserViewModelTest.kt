package vcmsa.projects.wil_hustlehub.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import vcmsa.projects.wil_hustlehub.Model.*
import vcmsa.projects.wil_hustlehub.Repository.*

class UserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var userRepo: UserRepository
    private lateinit var serviceRepo: ServiceRepository
    private lateinit var bookRepo: BookServiceRepository
    private lateinit var reviewRepo: ReviewRepository
    private lateinit var chatRepo: ChatRepository

    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        userRepo = mock()
        serviceRepo = mock()
        bookRepo = mock()
        reviewRepo = mock()
        chatRepo = mock()
        viewModel = UserViewModel(userRepo, serviceRepo, bookRepo, reviewRepo, chatRepo)
    }

    // ------------------ USERS ------------------
    @Test
    fun `register success updates registrationStat`() {
        val fakeUser = User("uid123", "Alice", "alice@test.com", "12345", "pass", "about", "tag", "photo", "date")

        doAnswer {
            val callback = it.arguments[4] as (Boolean, String?, User?) -> Unit
            callback(true, null, fakeUser)
            null
        }.`when`(userRepo).register(any(), any(), any(), any(), any())

        val observer = mock<Observer<Triple<Boolean, String?, User?>>>()
        viewModel.registrationStat.observeForever(observer)

        viewModel.register(fakeUser)

        verify(observer).onChanged(Triple(true, null, fakeUser))
    }

    @Test
    fun `login failure updates loginStat`() {
        doAnswer {
            val callback = it.arguments[2] as (Boolean, String?, User?) -> Unit
            callback(false, "Invalid credentials", null)
            null
        }.`when`(userRepo).login(any(), any(), any())

        val observer = mock<Observer<Pair<Boolean, String?>>>()
        viewModel.loginStat.observeForever(observer)

        viewModel.login("wrong@test.com", "badpass")

        verify(observer).onChanged(Pair(false, "Invalid credentials"))
    }

    @Test
    fun `logout clears currentUserData and updates loginStat`() {
        val observer = mock<Observer<Pair<Boolean, String?>>>()
        viewModel.loginStat.observeForever(observer)

        viewModel.logout()

        assertNull(viewModel.currentUserData.value)
        verify(observer).onChanged(Pair(false, "Logged out successfully"))
    }

    // ------------------ REVIEWS ------------------
    @Test
    fun `addReview success updates reviewStatus`() {
        val observer = mock<Observer<Pair<Boolean, String?>>>()
        viewModel.reviewStatus.observeForever(observer)

        doAnswer {
            val callback = it.arguments[3] as (Boolean, String?, Review?) -> Unit
            callback(true, null, Review("r1", "u1", "s1", "Alice", 5, "Great!", "date"))
            null
        }.`when`(reviewRepo).addReview(any(), any(), any(), any())

        viewModel.addReview("s1", 5, "Great!")

        verify(observer).onChanged(Pair(true, "Review added successfully"))
    }
}
