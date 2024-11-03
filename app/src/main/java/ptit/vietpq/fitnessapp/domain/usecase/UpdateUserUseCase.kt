package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.runSuspendCatching
import ptit.vietpq.fitnessapp.core.safeApiCall
import ptit.vietpq.fitnessapp.data.remote.request.UserInfoRequest
import ptit.vietpq.fitnessapp.data.remote.service.AuthService
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val authService: AuthService,
    private val dispatchers: AppCoroutineDispatchers,
) {
    suspend operator fun invoke(
        username: String,
        height: Int,
        weight: Int,
        age: Int,
        gender: String,
        profilePicture: String,
    ) = withContext(dispatchers.io) {
        safeApiCall {
            authService.updateUser(
                username = username,
                userInfo = UserInfoRequest(
                    height = height,
                    weight = weight,
                    age = age,
                    gender = gender,
                    profilePicture = profilePicture
                )
            )
        }
    }

}