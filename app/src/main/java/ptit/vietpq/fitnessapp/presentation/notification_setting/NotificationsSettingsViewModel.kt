package ptit.vietpq.fitnessapp.presentation.notification_setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import javax.inject.Inject

data class NotificationsSettingsUiState(
    val generalNotification: Boolean = true,
    val sound: Boolean = true,
    val dndMode: Boolean = true,
    val vibrate: Boolean = false,
    val lockScreen: Boolean = false,
    val reminders: Boolean = true
)

@HiltViewModel
class NotificationsSettingsViewModel @Inject constructor(
    private val sharePreferenceProvider: SharePreferenceProvider,
) : ViewModel() {

    private val _uiState: MutableStateFlow<NotificationsSettingsUiState> =
        MutableStateFlow(NotificationsSettingsUiState(
            generalNotification = sharePreferenceProvider.get(SharePreferenceProvider.GENERAL_NOTIFICATION, false) ?: false,
            sound = sharePreferenceProvider.get(SharePreferenceProvider.SOUND, false) ?: false,
            dndMode = sharePreferenceProvider.get(SharePreferenceProvider.DND_MODE, false) ?: false,
            vibrate = sharePreferenceProvider.get(SharePreferenceProvider.VIBRATE, false) ?: false,
            lockScreen = sharePreferenceProvider.get(SharePreferenceProvider.LOCK_SCREEN, false) ?: false,
            reminders = sharePreferenceProvider.get(SharePreferenceProvider.REMINDERS, false) ?: false

        ))
    val uiState = _uiState.asStateFlow()

    fun onSettingChanged(setting: NotificationSetting, value: Boolean) {
        _uiState.update { currentState ->
            when (setting) {
                NotificationSetting.SOUND -> {
                    sharePreferenceProvider.save(SharePreferenceProvider.SOUND, value)
                    currentState.copy(sound = value)
                }
                NotificationSetting.VIBRATE -> {
                    sharePreferenceProvider.save(SharePreferenceProvider.VIBRATE, value)
                    currentState.copy(vibrate = value)
                }
                NotificationSetting.LOCK_SCREEN -> {
                    sharePreferenceProvider.save(SharePreferenceProvider.LOCK_SCREEN, value)
                    currentState.copy(lockScreen = value)
                }
                NotificationSetting.REMINDERS -> {
                    sharePreferenceProvider.save(SharePreferenceProvider.REMINDERS, value)
                    currentState.copy(reminders = value)
                }
                NotificationSetting.GENERAL -> {
                    sharePreferenceProvider.save(SharePreferenceProvider.GENERAL_NOTIFICATION, value)
                    currentState.copy(generalNotification = value)
                }
                NotificationSetting.DND -> {
                    sharePreferenceProvider.save(SharePreferenceProvider.DND_MODE, value)
                    currentState.copy(dndMode = value)
                }
            }
        }
    }
}