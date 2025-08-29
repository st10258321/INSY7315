package vcmsa.projects.wil_hustlehub.IntergrationTest


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
import vcmsa.projects.wil_hustlehub.IntergrationTest.MockUserRepo

@RunWith(AndroidJUnit4::class)
class UserIntergrationTests {

    private lateinit var userRepo: MockUserRepo
    private lateinit var viewModel : UserViewModel


    @Before
    fun setUp(){
        userRepo = MockUserRepo()
        val serviceRepo = ServiceRepository()
        val bookRepo = BookServiceRepository()
        viewModel = UserViewModel(userRepo, serviceRepo, bookRepo)

    }
    @Test
    fun registerAndLoginFlow() = runBlocking{
        val testUser = User(
            userID = "",
            name = "Tester",
            email = "tester@gmail.com",
            phoneNumber = "0712345678",
            password ="password123"
        )
        viewModel.register(testUser)
        val registrationResult = getOrAwaitValue(viewModel.registrationStat)
        assertTrue(registrationResult.first)

        viewModel.login("tester@gmail.com","password123")
        val loginResult = getOrAwaitValue(viewModel.loginStat)
        assertTrue(loginResult.first)

        var userIntergrationTest1 : Boolean = false
        if(loginResult.first && registrationResult.first){
            userIntergrationTest1 = true
            assertEquals(true,userIntergrationTest1)
        }
    }
}