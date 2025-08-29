package vcmsa.projects.wil_hustlehub.IntergrationTest


import org.junit.Assert.*
import org.junit.Before
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository
import vcmsa.projects.wil_hustlehub.ViewModel.UserViewModel

class UserIntergrationTests {

    private lateinit var userRepo: UserRepository
    private lateinit var viewModel : UserViewModel


    @Before
    fun setUp(){
        userRepo = UserRepository()
        val serviceRepo = ServiceRepository()
        val bookRepo = BookServiceRepository()
        viewModel = UserViewModel(userRepo, serviceRepo, bookRepo)

    }
}