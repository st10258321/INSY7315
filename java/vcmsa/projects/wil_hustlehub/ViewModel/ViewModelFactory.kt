package vcmsa.projects.wil_hustlehub.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vcmsa.projects.wil_hustlehub.Repository.BookServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.ReviewRepository
import vcmsa.projects.wil_hustlehub.Repository.ServiceRepository
import vcmsa.projects.wil_hustlehub.Repository.UserRepository

class ViewModelFactory(
    private val userRepo: UserRepository,
    private val serviceRepo: ServiceRepository,
    private val bookRepo: BookServiceRepository,
    private val reviewRepo: ReviewRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepo, serviceRepo, bookRepo, reviewRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}