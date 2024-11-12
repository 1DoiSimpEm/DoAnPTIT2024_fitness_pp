package ptit.vietpq.fitnessapp.presentation.notification_setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object NotificationSettingsDestination : FitnessNavigationDestination {
    override val route: String = "notification_settings_route"
    override val destination: String = "notification_settings_destination"
}

fun NavGraphBuilder.notificationSettingsGraph(
    onBackPressed: () -> Unit,
) =
    composable(route = NotificationSettingsDestination.route) {
        NotificationsSettingsRoute(
            onBackPressed = onBackPressed
        )
    }
