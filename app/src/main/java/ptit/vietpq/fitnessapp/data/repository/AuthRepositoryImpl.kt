package ptit.vietpq.fitnessapp.data.repository

import ptit.vietpq.fitnessapp.core.runSuspendCatching
import ptit.vietpq.fitnessapp.data.mapper.asModel
import ptit.vietpq.fitnessapp.data.remote.service.AuthService
import ptit.vietpq.fitnessapp.domain.model.LoginModel
import ptit.vietpq.fitnessapp.domain.model.RegisterModel
import ptit.vietpq.fitnessapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
) : AuthRepository {
    override suspend fun register(userName: String, password: String): Result<RegisterModel> = runSuspendCatching {
            authService.register(
                username = userName,
                password = password
            ).asModel()
        }

    override suspend fun login(userName: String, password: String): Result<LoginModel> =
        runSuspendCatching {
            authService.login(
                username = userName,
                password = password
            ).asModel()
        }
}