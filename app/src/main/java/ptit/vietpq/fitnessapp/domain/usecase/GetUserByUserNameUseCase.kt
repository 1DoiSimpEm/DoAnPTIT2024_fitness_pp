package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.safeApiCall
import ptit.vietpq.fitnessapp.data.remote.service.AuthService
import javax.inject.Inject

class GetUserByUserNameUseCase @Inject constructor(
    private val authService: AuthService,
    private val dispatchers: AppCoroutineDispatchers,
) {
    suspend operator fun invoke(username: String) = withContext(dispatchers.io){
        safeApiCall {
            authService.getUser(username)
        }
    }

}