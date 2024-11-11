package ptit.vietpq.fitnessapp.presentation.exercise_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.extension.getArg
import javax.inject.Inject

data class ExerciseDetailUiState(
    val exercise: ExerciseResponse,
    val isFavorite: Boolean,
) {
    companion object {
        fun initial(
            exercise: ExerciseResponse,
        ) = ExerciseDetailUiState(
            exercise = exercise,
            isFavorite = false,
        )
    }
}

@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState : MutableStateFlow<ExerciseDetailUiState> = MutableStateFlow(
        ExerciseDetailUiState.initial(
            exercise = savedStateHandle.getArg() ?: throw IllegalArgumentException("ExerciseResponse is required")
        )
    )
    val uiState = _uiState.asStateFlow()

}