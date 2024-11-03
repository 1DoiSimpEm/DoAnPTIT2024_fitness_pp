package ptit.vietpq.fitnessapp.data.repository

import ptit.vietpq.fitnessapp.core.runSuspendCatching
import ptit.vietpq.fitnessapp.data.mapper.asModel
import ptit.vietpq.fitnessapp.data.remote.request.RegisterRequest
import ptit.vietpq.fitnessapp.data.remote.service.AuthService
import ptit.vietpq.fitnessapp.domain.model.LoginModel
import ptit.vietpq.fitnessapp.domain.model.RegisterModel
import ptit.vietpq.fitnessapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
) : AuthRepository {
    override suspend fun register(
        userName: String,
        email: String,
        password: String
    ): Result<RegisterModel> =
        runSuspendCatching {
            authService.register(
                RegisterRequest(
                    username = userName,
                    email = email,
                    password = password
                )
            ).asModel()
        }

    override suspend fun login(
        userName: String,
        password: String
    ): Result<LoginModel> =
        runSuspendCatching {
            authService.login(
                userName,
                password
            ).asModel()
        }
}