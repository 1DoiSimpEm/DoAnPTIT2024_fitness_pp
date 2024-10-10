package ptit.vietpq.fitnessapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ptit.vietpq.fitnessapp.domain.usecase.LoginUseCase
import ptit.vietpq.fitnessapp.domain.usecase.RegisterUseCase
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            _uiState.emit(LoginUiState.Loading)
            loginUseCase(userName, password).fold(
                onSuccess = {
                    _uiState.emit(LoginUiState.LoginSuccess(it))
                },
                onFailure = {
                    _uiState.emit(LoginUiState.Error(it.message ?: "Unknown error"))
                }
            )
        }
    }

    fun register(userName: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.emit(LoginUiState.Loading)
            registerUseCase(userName, email, password).fold(
                onSuccess = {
                    _uiState.emit(LoginUiState.RegisterSuccess(it))
                },
                onFailure = {
                    _uiState.emit(LoginUiState.Error(it.message ?: "Unknown error"))
                }
            )
        }
    }

}