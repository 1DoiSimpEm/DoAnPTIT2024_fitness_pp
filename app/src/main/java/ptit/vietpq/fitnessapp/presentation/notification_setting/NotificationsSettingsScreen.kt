package ptit.vietpq.fitnessapp.presentation.notification_setting

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R

@Composable
fun NotificationsSettingsRoute(
    onBackPressed: () -> Unit,
    viewModel: NotificationsSettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NotificationsSettingsScreen(
        uiState = uiState,
        onBackPressed = onBackPressed,
        onSettingChanged = viewModel::onSettingChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun NotificationsSettingsScreen(
    uiState: NotificationsSettingsUiState,
    onBackPressed: () -> Unit,
    onSettingChanged: (NotificationSetting, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            android.Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = {
                onSettingChanged(NotificationSetting.GENERAL, it)
            }
        )
    } else {
        null
    }

    Scaffold(
        modifier = modifier.background(Color(0xFF1E1E1E)),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "Notifications Settings",
                        style = FitnessTheme.typo.innerBoldSize20LineHeight28,
                        color = Color(0xFF8B80F8)
                    )
                },
                colors = TopAppBarColors(
                    containerColor = FitnessTheme.color.black,
                    actionIconContentColor = Color(0xFF8B80F8),
                    scrolledContainerColor = FitnessTheme.color.black,
                    titleContentColor = Color(0xFF8B80F8),
                    navigationIconContentColor = Color(0xFF8B80F8)
                ),
                navigationIcon = {
                    Image(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable(onClick = onBackPressed),
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = "Back"
                    )
                }
            )
        },
        contentColor = FitnessTheme.color.primary,
        containerColor = Color(0xFF1E1E1E)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                NotificationSettingItem(
                    title = stringResource(R.string.general_notification),
                    isEnabled = uiState.generalNotification,
                    onSettingChanged = {
                        if(notificationPermission == null){
                            onSettingChanged(NotificationSetting.GENERAL, it)
                        } else {
                            notificationPermission.launchPermissionRequest()
                        }
                    }
                )

                NotificationSettingItem(
                    title = stringResource(R.string.sound),
                    isEnabled = uiState.sound,
                    onSettingChanged = { onSettingChanged(NotificationSetting.SOUND, it) }
                )

                NotificationSettingItem(
                    title = stringResource(R.string.don_t_disturb_mode),
                    isEnabled = uiState.dndMode,
                    onSettingChanged = { onSettingChanged(NotificationSetting.DND, it) }
                )

                NotificationSettingItem(
                    title = stringResource(R.string.vibrate),
                    isEnabled = uiState.vibrate,
                    onSettingChanged = { onSettingChanged(NotificationSetting.VIBRATE, it) }
                )

                NotificationSettingItem(
                    title = stringResource(R.string.lock_screen),
                    isEnabled = uiState.lockScreen,
                    onSettingChanged = { onSettingChanged(NotificationSetting.LOCK_SCREEN, it) }
                )

                NotificationSettingItem(
                    title = stringResource(R.string.reminders),
                    isEnabled = uiState.reminders,
                    onSettingChanged = { onSettingChanged(NotificationSetting.REMINDERS, it) }
                )
            }
        }
    }
}

@Composable
fun NotificationSettingItem(
    title: String,
    isEnabled: Boolean,
    onSettingChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            color = Color.White
        )

        Switch(
            checked = isEnabled,
            onCheckedChange = onSettingChanged,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF8B80F8),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray.copy(alpha = 0.6f)
            )
        )
    }
}

@Preview
@Composable
private fun PreviewNotificationScreen() {
    val uiState = NotificationsSettingsUiState()
    NotificationsSettingsScreen(
        uiState = uiState,
        onBackPressed = {},
        onSettingChanged = { _, _ -> }
    )
}

enum class NotificationSetting {
    GENERAL,
    SOUND,
    DND,
    VIBRATE,
    LOCK_SCREEN,
    REMINDERS
}