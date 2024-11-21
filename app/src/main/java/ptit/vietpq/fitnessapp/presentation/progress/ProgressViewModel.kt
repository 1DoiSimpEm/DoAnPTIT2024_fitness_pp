package ptit.vietpq.fitnessapp.presentation.progress

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ptit.vietpq.fitnessapp.data.remote.response.ProgressResponse
import javax.inject.Inject

data class ProgressUiState(
    val progressList: ImmutableList<ProgressResponse> = persistentListOf(),
    val isLoading: Boolean = false,
    val error: Throwable? = null
)

@HiltViewModel
class ProgressViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<ProgressUiState>(ProgressUiState())
    val uiState = _uiState.asStateFlow()
}