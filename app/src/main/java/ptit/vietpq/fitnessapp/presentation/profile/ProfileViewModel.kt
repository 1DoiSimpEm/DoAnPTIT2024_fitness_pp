package ptit.vietpq.fitnessapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ptit.vietpq.fitnessapp.core.EventChannel
import ptit.vietpq.fitnessapp.core.HasEventFlow
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.data.remote.response.UserResponse
import ptit.vietpq.fitnessapp.domain.usecase.GetUserByUserNameUseCase
import ptit.vietpq.fitnessapp.domain.usecase.UpdateUserUseCase
import javax.inject.Inject

data class ProfileUiState(
    val user: UserResponse?,
) {
    companion object {
        val Empty = ProfileUiState(null)
    }
}

sealed interface ProfileState {
    data object Loading : ProfileState
    data object Success : ProfileState
    data object Error : ProfileState
    data object Idle : ProfileState
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val eventChannel: EventChannel<ProfileState>,
    private val getUserByUserNameUseCase: GetUserByUserNameUseCase,
    private val updateUserInfoUseCase: UpdateUserUseCase,
    private val sharePreferenceProvider: SharePreferenceProvider,
) : ViewModel(), HasEventFlow<ProfileState> by eventChannel {
    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState.Empty)
    val uiState = _uiState.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            busEvent(ProfileState.Loading)
            val result = getUserByUserNameUseCase.invoke(sharePreferenceProvider.userName)
            result.onSuccess { res ->
                _uiState.update {
                    it.copy(
                        user = res
                    )
                }
                busEvent(ProfileState.Success)
            }.onFailure {
                busEvent(ProfileState.Error)
            }
        }
    }

    fun updateUser(
        height: Int,
        weight: Int,
        age: Int,
        gender: String,
    ) {
        viewModelScope.launch {
            busEvent(ProfileState.Loading)
            val result = updateUserInfoUseCase.invoke(
                username = sharePreferenceProvider.userName,
                height = height,
                weight = weight,
                gender = gender,
                age = age,
                profilePicture = ""
            )
            result.onSuccess {
                _uiState.update {
                    it.copy(
                        user = it.user?.copy(
                            height = height,
                            weight = weight,
                            gender = gender,
                            age = age,
                            profilePicture = ""
                        )
                    )
                }
                sharePreferenceProvider.isSetupFinished = true
                busEvent(ProfileState.Success)
            }.onFailure {
                busEvent(ProfileState.Error)
            }
        }
    }

    private fun busEvent(state: ProfileState) {
        viewModelScope.launch {
            eventChannel.send(state)
        }
    }


}