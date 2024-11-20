package ptit.vietpq.fitnessapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ptit.vietpq.fitnessapp.core.EventChannel
import ptit.vietpq.fitnessapp.core.HasEventFlow
import ptit.vietpq.fitnessapp.domain.usecase.RegisterUseCase
import ptit.vietpq.fitnessapp.presentation.login.LoginState
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val eventChannel: EventChannel<LoginState>,
) : ViewModel(), HasEventFlow<LoginState> by eventChannel {

    fun register(userName: String, email: String, password: String) {
        viewModelScope.launch {
            busEvent(LoginState.Loading)
            registerUseCase(userName, email, password).fold(
                onSuccess = {
                    busEvent(LoginState.RegisterSuccess)
                },
                onFailure = {
                    busEvent(LoginState.Error(it.message ?: "Unknown error"))
                }
            )
        }
    }

    private fun busEvent(event: LoginState) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

}