package ptit.vietpq.fitnessapp.data.mapper

import ptit.vietpq.fitnessapp.data.remote.response.LoginResponse
import ptit.vietpq.fitnessapp.data.remote.response.RegisterResponse
import ptit.vietpq.fitnessapp.domain.model.LoginModel
import ptit.vietpq.fitnessapp.domain.model.RegisterModel

fun LoginResponse.asModel() = LoginModel(
    status = this.status,
    message = this.message,
    tokenType = this.tokenType,
    accessToken = this.accessToken,
    userName = this.userName
)

fun RegisterResponse.asModel() = RegisterModel(
    status = this.status,
    message = this.message,
    accessToken = this.accessToken,
    tokenType = this.tokenType
)