package ptit.vietpq.fitnessapp.presentation.exercise_category

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
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.MuscleGroupResponse
import ptit.vietpq.fitnessapp.domain.usecase.GetExercisesByMuscleGroupUseCase
import ptit.vietpq.fitnessapp.domain.usecase.GetMuscleGroupsUseCase
import javax.inject.Inject

data class ExerciseCategoryUiState(
    val categories: ImmutableList<MuscleGroupResponse> = persistentListOf(),
    val selectedCategory: String = "",
    val exercises: ImmutableList<ExerciseResponse> = persistentListOf(),
    val isLoading: Boolean = true,
    val error: String = ""
)

@HiltViewModel
class ExerciseCategoryViewModel @Inject constructor(
    private val getMuscleGroupsUseCase: GetMuscleGroupsUseCase,
    private val getExercisesByMuscleGroupUseCase: GetExercisesByMuscleGroupUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ExerciseCategoryUiState> =
        MutableStateFlow(ExerciseCategoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initData()
    }

    private fun initData() {
        viewModelScope.launch {
            val muscleGroupResult = getMuscleGroupsUseCase()
            muscleGroupResult.fold(
                onSuccess = { res ->
                    _uiState.update { state ->
                        state.copy(
                            categories = res.toImmutableList(),
                            isLoading = false,
                            selectedCategory = res.find {
                                it.id == savedStateHandle.get<Int>("selectedCategory")
                            }?.name ?: res.first().name
                        )
                    }
                    updateSelectedCategory(
                        _uiState.value.categories.find {
                            it.id == savedStateHandle.get<Int>("selectedCategory")
                        }?.id ?: res.first().id
                    )
                },
                onFailure = { failure ->
                    _uiState.update {
                        it.copy(error = failure.message ?: "Unknown error", isLoading = false)
                    }
                }
            )
        }
    }

    fun updateSelectedCategory(id: Int) {
        viewModelScope.launch {
            val selectedCategoryName =
                _uiState.value.categories.find { it.id == id }?.name ?: return@launch

            _uiState.update { state ->
                state.copy(
                    selectedCategory = selectedCategoryName,
                    isLoading = true,
                )
            }

            getExercisesByMuscleGroupUseCase(id).fold(
                onSuccess = { res ->
                    _uiState.update { state ->
                        state.copy(
                            exercises = res.toImmutableList(),
                            isLoading = false,
                        )
                    }
                },
                onFailure = { failure ->
                    _uiState.update {
                        it.copy(error = failure.message ?: "Unknown error", isLoading = false)
                    }
                }
            )
        }
    }

}