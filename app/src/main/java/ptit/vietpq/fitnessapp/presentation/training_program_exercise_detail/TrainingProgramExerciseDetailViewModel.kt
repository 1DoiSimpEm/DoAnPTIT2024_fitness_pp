package ptit.vietpq.fitnessapp.presentation.training_program_exercise_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExercise
import ptit.vietpq.fitnessapp.extension.getArg
import ptit.vietpq.fitnessapp.presentation.exercise_detail.StopWatchState
import ptit.vietpq.fitnessapp.presentation.exercise_detail.TimerState
import javax.inject.Inject


data class TrainingProgramExerciseDetailUiState(
    val exercise: TrainingProgramExercise,
    val isFavorite: Boolean,
    val timerState: TimerState = TimerState.Idle,
    val stopwatchState: StopWatchState = StopWatchState.Idle,
) {
    companion object {
        fun initial(exercise: TrainingProgramExercise) = TrainingProgramExerciseDetailUiState(
            exercise = exercise,
            isFavorite = false
        )
    }
}

@HiltViewModel
class TrainingProgramExerciseDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        TrainingProgramExerciseDetailUiState.initial(
            exercise = savedStateHandle.getArg<TrainingProgramExercise>()
                ?: throw IllegalArgumentException("TrainingProgramExerciseResponse must be provided")
        )
    )
    val uiState = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var stopwatchJob: Job? = null

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            val startTime = _uiState.value.exercise.duration
            (startTime downTo 0).forEach { remainingSeconds ->
                _uiState.update { it.copy(timerState = TimerState.Running(remainingSeconds.toLong())) }
                delay(1000)
            }
            _uiState.update { it.copy(timerState = TimerState.Finished) }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update {
            when (it.timerState) {
                is TimerState.Running -> it.copy(timerState = TimerState.Paused)
                else -> it
            }
        }
    }

    fun resetTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(timerState = TimerState.Idle) }
    }

    fun startStopwatch() {
        stopwatchJob?.cancel()
        stopwatchJob = viewModelScope.launch {
            var elapsedTime = when (val state = _uiState.value.stopwatchState) {
                is StopWatchState.Paused -> state.elapsedTime
                else -> 0L
            }
            while (true) {
                _uiState.update { it.copy(stopwatchState = StopWatchState.Running(elapsedTime)) }
                delay(1000)
                elapsedTime++
            }
        }
    }

    fun pauseStopwatch() {
        stopwatchJob?.cancel()
        _uiState.update {
            when (val state = it.stopwatchState) {
                is StopWatchState.Running -> it.copy(stopwatchState = StopWatchState.Paused(state.elapsedTime))
                else -> it
            }
        }
    }

    fun resetStopwatch() {
        stopwatchJob?.cancel()
        _uiState.update { it.copy(stopwatchState = StopWatchState.Idle) }
    }

    fun toggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        stopwatchJob?.cancel()
    }
}