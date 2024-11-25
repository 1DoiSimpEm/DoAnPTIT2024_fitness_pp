package ptit.vietpq.fitnessapp.presentation.training_program_exercise

import androidx.lifecycle.SavedStateHandle
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
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramResponse
import ptit.vietpq.fitnessapp.domain.usecase.GetTrainingExerciseByTrainingProgramIDUseCase
import ptit.vietpq.fitnessapp.extension.getArg
import javax.inject.Inject

data class TrainingProgramExerciseUiState(
    val loading: Boolean = false,
    val error: Throwable? = null,
    val trainingProgramExercises: ImmutableList<TrainingProgramExerciseResponse> = persistentListOf(),
    val trainingProgram: TrainingProgramResponse? = null,
)

@HiltViewModel
class TrainingProgramExerciseViewModel @Inject constructor(
    private val getTrainingExerciseByTrainingProgramIDUseCase: GetTrainingExerciseByTrainingProgramIDUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(TrainingProgramExerciseUiState().copy(
        trainingProgram = savedStateHandle.getArg<TrainingProgramResponse>()
    ))
    val uiState = _uiState.asStateFlow()

    init {
        getTrainingProgramExercises(
            trainingProgramId = savedStateHandle.getArg<TrainingProgramResponse>()?.id ?: -1
        )
    }

    private fun getTrainingProgramExercises(trainingProgramId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(loading = true)
            }
            getTrainingExerciseByTrainingProgramIDUseCase(trainingProgramId)
                .onSuccess { trainingProgramExercises ->
                    _uiState.update {
                        it.copy(
                            loading = false,
                            trainingProgramExercises = trainingProgramExercises.toImmutableList()
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            loading = false,
                            error = error
                        )
                    }
                }
        }

    }

}