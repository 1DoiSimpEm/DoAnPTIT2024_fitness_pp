package ptit.vietpq.fitnessapp.presentation.login

import ptit.vietpq.fitnessapp.domain.model.LoginModel
import ptit.vietpq.fitnessapp.domain.model.RegisterModel

sealed interface LoginUiState {
    data object Loading : LoginUiState
    data object Empty : LoginUiState
    data class Error(val message: String) : LoginUiState
    data class LoginSuccess(
        val loginModel: LoginModel
    ) : LoginUiState
    data class RegisterSuccess(
        val loginModel: RegisterModel
    ) : LoginUiState
}