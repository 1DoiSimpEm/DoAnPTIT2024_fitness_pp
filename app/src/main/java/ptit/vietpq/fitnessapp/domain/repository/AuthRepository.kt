package ptit.vietpq.fitnessapp.domain.repository

import ptit.vietpq.fitnessapp.domain.model.LoginModel
import ptit.vietpq.fitnessapp.domain.model.RegisterModel

interface AuthRepository {
    suspend fun register(userName: String, password: String): Result<RegisterModel>
    suspend fun login(userName: String, password: String): Result<LoginModel>
}