package ptit.vietpq.fitnessapp.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ptit.vietpq.fitnessapp.core.EventChannel
import ptit.vietpq.fitnessapp.core.HasEventFlow
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.domain.usecase.UpdateUserUseCase
import javax.inject.Inject

sealed interface SetupState {
    data object Loading : SetupState
    data object Success : SetupState
    data object Error : SetupState
    data object Idle : SetupState
}

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val sharePreferenceProvider: SharePreferenceProvider,
    private val eventChannel: EventChannel<SetupState>,
) : ViewModel(), HasEventFlow<SetupState> by eventChannel {

    fun updateUser(
        height: Int,
        weight: Int,
        age: Int,
        gender: String,
        profilePicture: String,
    ) {
        viewModelScope.launch {
            busEvent(SetupState.Loading)
            val result = updateUserUseCase(
                username = sharePreferenceProvider.get(SharePreferenceProvider.USER_NAME,"").toString(),
                height = height,
                weight = weight,
                age = age,
                gender = gender,
                profilePicture = profilePicture
            )
            result.onSuccess {
                busEvent(SetupState.Success)
            }
            result.onFailure {
                busEvent(SetupState.Error)
            }
        }
    }


    private fun busEvent(state: SetupState) {
        viewModelScope.launch {
            eventChannel.send(state)
        }
    }

}