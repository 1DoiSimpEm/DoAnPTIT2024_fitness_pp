package ptit.vietpq.fitnessapp.presentation.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R

@Composable
fun SettingsRoute(
    onBackPressed: () -> Unit,
    onNotificationSettingClicked: () -> Unit,
    onPasswordSettingClicked: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val isVietnamese by viewModel.isVietnameseState.collectAsStateWithLifecycle()

    SettingsScreen(
        onBackPressed = onBackPressed,
        onNotificationSettingClicked = onNotificationSettingClicked,
        onPasswordSettingClicked = onPasswordSettingClicked,
        onLanguageChanged = viewModel::updateLanguage,
        isVietnamese = isVietnamese,
        onDeleteAccountClicked = {
            /*
            * Handle delete account clicked
             */
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    onNotificationSettingClicked: () -> Unit,
    onPasswordSettingClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onLanguageChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isVietnamese: Boolean = false,
) {
    Scaffold(
        modifier = modifier.background(Color(0xFF1E1E1E)),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.setting),
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                SettingsItemWithSwitch(
                    imageVector = Icons.Default.Language,
                    title = stringResource(R.string.language),
                    subtitle = if (isVietnamese) "Tiếng Việt" else "English",
                    isChecked = isVietnamese,
                    onCheckedChange = onLanguageChanged,
                    iconTint = Color(0xFF8B80F8)
                )

                SettingsItem(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_notification),
                    title = stringResource(R.string.notification_setting),
                    onClick = onNotificationSettingClicked,
                    iconTint = Color(0xFF8B80F8)
                )

                SettingsItem(
                    imageVector = Icons.Default.Password,
                    title = stringResource(R.string.password_setting),
                    onClick = onPasswordSettingClicked,
                    iconTint = Color(0xFF8B80F8)
                )

                SettingsItem(
                    imageVector = Icons.Default.DeleteForever,
                    title = stringResource(R.string.delete_account),
                    onClick = onDeleteAccountClicked,
                    iconTint = Color(0xFF8B80F8)
                )
            }
        }
    }
}

@Composable
fun SettingsItem(
    imageVector: ImageVector,
    title: String,
    onClick: () -> Unit,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = iconTint.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SettingsItemWithSwitch(
    imageVector: ImageVector,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = iconTint.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = Color.Gray
                )
            }
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
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
private fun SettingScreenPreview() {
    SettingsScreen(
        onBackPressed = {},
        onPasswordSettingClicked = {},
        onNotificationSettingClicked = {},
        onDeleteAccountClicked = {},
        onLanguageChanged = {}
    )
}