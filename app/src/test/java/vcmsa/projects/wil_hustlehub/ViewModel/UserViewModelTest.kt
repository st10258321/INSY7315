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
import vcmsa.projects.wil_hustlehub.Model.User
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository

class UserViewModelTest {

    // Makes LiveData updates run instantly in unit tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Dependencies (mocked)
    private lateinit var userRepo: UserRepository
    private lateinit var serviceRepo: ServiceRepository
    private lateinit var bookRepo: BookServiceRepository

    // Class under test
    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        userRepo = mock<UserRepository>()
        serviceRepo = mock<ServiceRepository>()
        bookRepo = mock<BookServiceRepository>()
        viewModel = UserViewModel(userRepo, serviceRepo, bookRepo)
    }

    @Test
    fun `register success updates registrationStat`() {
        val fakeUser = User(
            "uid123", "Alice", "alice@test.com", "12345",
            "pass", "about", "tag", "photo", "date"
        )

        // Stub repo response
        doAnswer {
            val callback = it.arguments[4] as (Boolean, String?, User?) -> Unit
            callback(true, null, fakeUser)
            null
        }.`when`(userRepo).register(any(), any(), any(), any(), any())

        val observer = mock<Observer<Triple<Boolean, String?, User?>>>()
        viewModel.registrationStat.observeForever(observer)

        // Act
        viewModel.register(fakeUser)

        // Assert
        verify(observer).onChanged(Triple(true, null, fakeUser))
    }

    @Test
    fun `login failure updates loginStat`() {
        // Stub repo response
        doAnswer {
            val callback = it.arguments[2] as (Boolean, String?, User?) -> Unit
            callback(false, "Invalid credentials", null)
            null
        }.`when`(userRepo).login(any(), any(), any())

        val observer = mock<Observer<Pair<Boolean, String?>>>()
        viewModel.loginStat.observeForever(observer)

        // Act
        viewModel.login("wrong@test.com", "badpass")

        // Assert
        verify(observer).onChanged(Pair(false, "Invalid credentials"))
    }

    @Test
    fun `logout clears currentUserData and updates loginStat`() {
        val observer = mock<Observer<Pair<Boolean, String?>>>()
        viewModel.loginStat.observeForever(observer)

        // Act
        viewModel.logout()

        // Assert
        assertNull(viewModel.currentUserData.value)
        verify(observer).onChanged(Pair(false, "Logged out successfully"))
    }
}
