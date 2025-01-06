package ptit.vietpq.fitnessapp.presentation.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.extension.changeLocale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharePreferenceProvider: SharePreferenceProvider,
) : ViewModel() {

    private val _isVietnameseState = MutableStateFlow(
        sharePreferenceProvider.get(SharePreferenceProvider.LANGUAGE, "en") == "vi"
    )
    val isVietnameseState = _isVietnameseState.asStateFlow()

    fun updateLanguage(isVietnamese: Boolean) {
        sharePreferenceProvider.save(
            SharePreferenceProvider.LANGUAGE,
            if (isVietnamese) "vi" else "en"
        )
        _isVietnameseState.update {
            isVietnamese
        }
    }

}