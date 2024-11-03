package ptit.vietpq.fitnessapp.presentation.login

import ptit.vietpq.fitnessapp.domain.model.LoginModel
import ptit.vietpq.fitnessapp.domain.model.RegisterModel

sealed interface LoginState {
    data object Loading : LoginState
    data object Empty : LoginState
    data class Error(val message: String) : LoginState
    data object LoginSuccess : LoginState
    data object RegisterSuccess : LoginState
}

