package ptit.vietpq.fitnessapp.presentation.setup

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
import ptit.vietpq.fitnessapp.domain.usecase.UpdateUserUseCase
import javax.inject.Inject

sealed interface SetupState {
    data object Loading : SetupState
    data object Success : SetupState
    data object Error : SetupState
    data object Idle : SetupState
}

sealed interface SetupStep {
    data object Weight : SetupStep
    data object Height : SetupStep
    data object Age : SetupStep
    data object Gender : SetupStep
}

data class SetupScreenState(
    val currentStep: SetupStep = SetupStep.Weight,
    val weight: Int = 60,
    val height: Int = 170,
    val age: Int = 18,
    val isMale: Boolean = true,
    val isLoading: Boolean = false
)

sealed interface SetupScreenEvent {
    data class OnWeightSelected(val weight: Int) : SetupScreenEvent
    data class OnHeightSelected(val height: Int) : SetupScreenEvent
    data class OnAgeSelected(val age: Int) : SetupScreenEvent
    data class OnGenderSelected(val isMale: Boolean) : SetupScreenEvent
    data object OnBackPressed : SetupScreenEvent
}


@HiltViewModel
class SetupViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val sharePreferenceProvider: SharePreferenceProvider,
    private val eventChannel: EventChannel<SetupState>,
) : ViewModel(), HasEventFlow<SetupState> by eventChannel {

    private val _state = MutableStateFlow(SetupScreenState())
    val state = _state.asStateFlow()

    fun onEvent(event: SetupScreenEvent) {
        when (event) {
            is SetupScreenEvent.OnWeightSelected -> {
                _state.update { it.copy(
                    weight = event.weight,
                    currentStep = SetupStep.Height
                ) }
            }
            is SetupScreenEvent.OnHeightSelected -> {
                _state.update { it.copy(
                    height = event.height,
                    currentStep = SetupStep.Age
                ) }
            }
            is SetupScreenEvent.OnAgeSelected -> {
                _state.update { it.copy(
                    age = event.age,
                    currentStep = SetupStep.Gender
                ) }
            }
            is SetupScreenEvent.OnGenderSelected -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    updateUser(
                        age = state.value.age,
                        weight = state.value.weight,
                        height = state.value.height,
                        gender = if (event.isMale) "Male" else "Female",
                        profilePicture = ""
                    )
                    _state.update { it.copy(isLoading = false) }
                }
            }
            SetupScreenEvent.OnBackPressed -> {
                _state.update { state ->
                    when (state.currentStep) {
                        SetupStep.Weight -> state
                        SetupStep.Height -> state.copy(currentStep = SetupStep.Weight)
                        SetupStep.Age -> state.copy(currentStep = SetupStep.Height)
                        SetupStep.Gender -> state.copy(currentStep = SetupStep.Age)
                    }
                }
            }
        }
    }


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