package ptit.vietpq.fitnessapp.presentation.password_setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class PasswordSettingsUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class PasswordSettingsViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(PasswordSettingsUiState())
    val uiState = _uiState.asStateFlow()

    fun updatePassword(field: PasswordField, value: String) {
        _uiState.update {
            when (field) {
                PasswordField.CURRENT -> it.copy(currentPassword = value)
                PasswordField.NEW -> it.copy(newPassword = value)
                PasswordField.CONFIRM -> it.copy(confirmPassword = value)
            }
        }
    }

    fun changePassword() {

    }
}