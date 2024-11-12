package ptit.vietpq.fitnessapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.service.ExerciseService
import ptit.vietpq.fitnessapp.domain.usecase.GetExercisesUseCase
import timber.log.Timber
import javax.inject.Inject


data class HomeUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val exercises: List<ExerciseResponse> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getExercises()
    }

    fun getExercises() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = getExercisesUseCase()
            result.onSuccess {  res ->
                _uiState.update { it.copy(isLoading = false, exercises = res) }
            }
            result.onFailure { res ->
                Timber.e(res)
                _uiState.update { it.copy(isLoading = false, isError = true, errorMessage = it.errorMessage) }
            }
        }
    }

}