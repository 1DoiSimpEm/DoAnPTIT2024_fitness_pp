package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.domain.model.LoginModel
import ptit.vietpq.fitnessapp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
) {
    suspend operator fun invoke (userName: String, password: String): Result<LoginModel> = withContext(appCoroutineDispatchers.io) {
        authRepository.login(userName, password)
    }
}