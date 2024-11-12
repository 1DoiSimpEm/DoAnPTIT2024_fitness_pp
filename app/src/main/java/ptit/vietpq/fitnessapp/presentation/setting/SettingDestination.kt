package ptit.vietpq.fitnessapp.presentation.setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object SettingDestination : FitnessNavigationDestination {
    override val route: String = "setting_route"
    override val destination: String = "setting_destination"
}

fun NavGraphBuilder.settingGraph(
    onBackPressed: () -> Unit,
    onNotificationSettingClicked: () -> Unit,
    onPasswordSettingClicked: () -> Unit,
) =
    composable(route = SettingDestination.route) {
        SettingsRoute(
            onBackPressed = onBackPressed,
            onNotificationSettingClicked = onNotificationSettingClicked,
            onPasswordSettingClicked = onPasswordSettingClicked,
        )
    }