package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.domain.model.RegisterModel
import ptit.vietpq.fitnessapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
) {
    suspend operator fun invoke(userName: String, password: String): Result<RegisterModel> =
        withContext(appCoroutineDispatchers.io) {
            authRepository.register(userName, password)
        }
}