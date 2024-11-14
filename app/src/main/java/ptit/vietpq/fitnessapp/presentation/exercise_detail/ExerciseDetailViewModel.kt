package ptit.vietpq.fitnessapp.presentation.exercise_detail

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
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.extension.getArg
import javax.inject.Inject

sealed class TimerState {
    data object Idle : TimerState()
    data class Running(val timeLeft: Long) : TimerState()
    data object Paused : TimerState()
    data object Finished : TimerState()
}

sealed class StopWatchState {
    data object Idle : StopWatchState()
    data class Running(val elapsedTime: Long) : StopWatchState()
    data class Paused(val elapsedTime: Long) : StopWatchState()
}

data class ExerciseDetailUiState(
    val exercise: ExerciseResponse,
    val isFavorite: Boolean,
    val timerState: TimerState = TimerState.Idle,
    val stopwatchState: StopWatchState = StopWatchState.Idle,
    val selectedDuration: Long = 60L
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
    private val _uiState: MutableStateFlow<ExerciseDetailUiState> = MutableStateFlow(
        ExerciseDetailUiState.initial(
            exercise = savedStateHandle.getArg()
                ?: ExerciseResponse()
        )
    )
    val uiState = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var stopwatchJob: Job? = null

    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            val startTime = _uiState.value.selectedDuration
            (startTime downTo 0).forEach { remainingSeconds ->
                _uiState.update { it.copy(timerState = TimerState.Running(remainingSeconds)) }
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

    fun setDuration(seconds: Long) {
        _uiState.update { it.copy(selectedDuration = seconds) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        stopwatchJob?.cancel()
    }

}