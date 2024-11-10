package ptit.vietpq.fitnessapp.presentation.meal_plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ptit.vietpq.fitnessapp.data.remote.response.MealPlanResponse
import ptit.vietpq.fitnessapp.domain.usecase.GetMealPlansByUserUseCase
import javax.inject.Inject

data class MealListUiState(
    val isLoading: Boolean = true,
    val mealList: ImmutableList<MealPlanResponse> = persistentListOf(),
    val error: String = "",
)

@HiltViewModel
class MealListViewModel @Inject constructor(
    private val getMealPlanByUserUseCase: GetMealPlansByUserUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MealListUiState> = MutableStateFlow(MealListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getMealPlans()
    }

    private fun getMealPlans() {
        viewModelScope.launch {
            val result = getMealPlanByUserUseCase()
            result.onSuccess { res ->
                _uiState.update {
                    it.copy(mealList = res.toImmutableList(), isLoading = false)
                }
            }
            result.onFailure { res ->
                _uiState.update {
                    it.copy(error = res.message ?: "Error", isLoading = false)
                }
            }
        }
    }

}