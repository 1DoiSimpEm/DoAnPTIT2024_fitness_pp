package ptit.vietpq.fitnessapp.domain.repository

import ptit.vietpq.fitnessapp.domain.model.LoginModel
import ptit.vietpq.fitnessapp.domain.model.RegisterModel

interface AuthRepository {
    suspend fun login(userName: String, password: String): Result<LoginModel>
    suspend fun register(userName: String, email: String, password: String): Result<RegisterModel>
}