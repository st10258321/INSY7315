package vcmsa.projects.wil_hustlehub.IntergrationTest


import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import vcmsa.projects.wil_hustlehub.Model.User
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel
import vcmsa.projects.wil_hustlehub.getOrAwaitValue
import vcmsa.projects.wil_hustlehub.Model.Service

@RunWith(AndroidJUnit4::class)
class UserIntergrationTests {

    private lateinit var userRepo: UserRepository
    private lateinit var viewModel : UserViewModel

    private lateinit var testEmail: String
    private lateinit var password: String
    @Before
    fun setUp(){
        userRepo = UserRepository()
        val serviceRepo = ServiceRepository()
        val bookRepo = BookServiceRepository()
        viewModel = UserViewModel(userRepo, serviceRepo, bookRepo)
        testEmail = "tester_${System.currentTimeMillis()}@gmail.com"
        password = "password123"
    }
    //this integration test proves that the components for register, login and Register Service all work together
    @Test
    fun registerAndLoginAndRegisterServiceFlow() = runBlocking{

        val testUser = User(
            userID = "",
            name = "Tester",
            email = testEmail,
            phoneNumber = "0712345678",
            password = password
        )
        viewModel.register(testUser)
        val registrationResult = getOrAwaitValue(viewModel.registrationStat)
        assertTrue("Registration Successful",registrationResult.first)

        viewModel.login(testUser.email,testUser.password)
        val loginResult = getOrAwaitValue(viewModel.loginStat)
        assertTrue("Login successful",loginResult.first)

        val testService = Service(
            serviceId = "",
            serviceName = "Test Service",
            category = "Test Category",
            description = "Test Description",
            price = 100.0,
            image = "Test Image",
            availability = "Test Availability",
            location = "Test Location"
        )
        viewModel.addService(testService.serviceName,
            testService.category,
            testService.description,
            testService.price,
            testService.image,
            testService.availability,
            testService.location)
        val serviceAdded = getOrAwaitValue(viewModel.serviceStatus)
        assertTrue("Service added successfully",serviceAdded.first)
    }
}