package ptit.vietpq.fitnessapp.presentation.meal_planning

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
import ptit.vietpq.fitnessapp.data.remote.request.MealPlanRequest
import ptit.vietpq.fitnessapp.domain.usecase.CreateMealPlanUseCase
import javax.inject.Inject

sealed interface MealPlanningState {
    data object Idle : MealPlanningState
    data class Success(val response: String) : MealPlanningState
    data class Error(val message: String) : MealPlanningState
}

data class MealPlanningUiState(
    val isLoading: Boolean = false,
    val languageCode : String = "en"
)

@HiltViewModel
class MealPlanningViewModel @Inject constructor(
    private val createMealPlanUseCase: CreateMealPlanUseCase,
    private val sharePreferenceProvider: SharePreferenceProvider,
    private val eventChannel: EventChannel<MealPlanningState>,
) : ViewModel(), HasEventFlow<MealPlanningState> by eventChannel {

    private val _uiState: MutableStateFlow<MealPlanningUiState> =
        MutableStateFlow(MealPlanningUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(languageCode = sharePreferenceProvider.get(SharePreferenceProvider.LANGUAGE, "en") ?: "en")
        }
    }

    fun fetchChatResponse(prompt: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = createMealPlanUseCase(
                MealPlanRequest(
                    sharePreferenceProvider.userId, prompt
                )
            )
            result.onSuccess { res ->
                eventChannel.send(MealPlanningState.Success(res.data.description))
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
            result.onFailure {
                eventChannel.send(MealPlanningState.Error(it.message ?: "Error"))
                _uiState.update { state ->
                    state.copy(isLoading = false)
                }
            }
        }
    }
}