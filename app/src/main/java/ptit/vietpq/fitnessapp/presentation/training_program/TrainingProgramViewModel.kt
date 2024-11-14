package ptit.vietpq.fitnessapp.presentation.training_program

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
import ptit.vietpq.fitnessapp.data.remote.response.TrainingCategoryResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramResponse
import ptit.vietpq.fitnessapp.domain.usecase.GetTrainingCategoryUseCase
import ptit.vietpq.fitnessapp.domain.usecase.GetTrainingProgramsByCategoryUseCase
import javax.inject.Inject

data class TrainingProgramUiState(
    val categories: ImmutableList<TrainingCategoryResponse> = persistentListOf(),
    val selectedCategory: String = "",
    val programs: ImmutableList<TrainingProgramResponse> = persistentListOf(),
    val isLoading: Boolean = true,
    val error: String = ""
)

@HiltViewModel
class TrainingProgramViewModel @Inject constructor(
    private val getTrainingCategoryUseCase: GetTrainingCategoryUseCase,
    private val getTrainingProgramByCategory: GetTrainingProgramsByCategoryUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState: MutableStateFlow<TrainingProgramUiState> =
        MutableStateFlow(TrainingProgramUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initData()
    }

    private fun initData() {
        viewModelScope.launch {
            val trainingCategoryResult = getTrainingCategoryUseCase()
            trainingCategoryResult.fold(
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

            getTrainingProgramByCategory(id).fold(
                onSuccess = { res ->
                    _uiState.update { state ->
                        state.copy(
                            programs = res.toImmutableList(),
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