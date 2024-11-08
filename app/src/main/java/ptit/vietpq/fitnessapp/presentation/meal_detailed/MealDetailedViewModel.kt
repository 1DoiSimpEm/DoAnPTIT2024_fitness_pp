package ptit.vietpq.fitnessapp.presentation.meal_detailed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class MealDetailedUiState(
    val mealDescription: String = "",
)

@HiltViewModel
class MealDetailedViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MealDetailedUiState> =
        MutableStateFlow(MealDetailedUiState(
            MealDetailedDestination.getSavedStateRoute(stateHandle)
        ))
    val uiState = _uiState.asStateFlow()

}