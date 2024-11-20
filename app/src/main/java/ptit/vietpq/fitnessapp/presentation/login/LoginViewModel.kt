package ptit.vietpq.fitnessapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ptit.vietpq.fitnessapp.core.EventChannel
import ptit.vietpq.fitnessapp.core.HasEventFlow
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.domain.usecase.LoginUseCase
import ptit.vietpq.fitnessapp.domain.usecase.RegisterUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val eventChannel: EventChannel<LoginState>,
    private val sharePreferenceProvider: SharePreferenceProvider,
) : ViewModel(), HasEventFlow<LoginState> by eventChannel {

    var accessToken: String = ""
        set(value) {
            sharePreferenceProvider.accessToken = value
            field = value
        }
        get() {
            return sharePreferenceProvider.accessToken
        }

    private var userName = ""
        set(value) {
            sharePreferenceProvider.userName = value
            field = value
        }
        get() {
            return sharePreferenceProvider.userName
        }

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            busEvent(LoginState.Loading)
            loginUseCase(userName, password).fold(
                onSuccess = {
                    busEvent(LoginState.LoginSuccess)
                    accessToken = it.accessToken
                    sharePreferenceProvider.userId = it.id
                    this@LoginViewModel.userName = it.userName
                },
                onFailure = {
                    busEvent(LoginState.Error(it.message ?: "Unknown error"))
                }
            )
        }
    }

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