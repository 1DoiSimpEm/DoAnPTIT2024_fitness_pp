package ptit.vietpq.fitnessapp.presentation.meal_planning

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ptit.vietpq.fitnessapp.core.EventChannel
import ptit.vietpq.fitnessapp.core.HasEventFlow
import ptit.vietpq.fitnessapp.data.remote.request.MealPlanRequest
import ptit.vietpq.fitnessapp.domain.usecase.CreateMealPlanUseCase
import javax.inject.Inject

sealed interface MealPlanningState {
    data object Idle : MealPlanningState
    data object Loading : MealPlanningState
    data class Success(val response: String) : MealPlanningState
    data class Error(val message: String) : MealPlanningState
}

@HiltViewModel
class MealPlanningViewModel @Inject constructor(
    private val createMealPlanUseCase: CreateMealPlanUseCase,
    private val eventChannel: EventChannel<MealPlanningState>
) : ViewModel(), HasEventFlow<MealPlanningState> by eventChannel {

    fun fetchChatResponse(prompt: String) {
        viewModelScope.launch {
           val result = createMealPlanUseCase(MealPlanRequest(
               1,prompt
           ))
            result.onSuccess { res ->
                eventChannel.send(MealPlanningState.Success(res.data.description))
                Log.d("MealPlanningViewModel123123123", "fetchChatResponse: ${res.data.description}")
            }
            result.onFailure {
                eventChannel.send(MealPlanningState.Error(it.message ?: "Error"))
            }
        }
    }
}