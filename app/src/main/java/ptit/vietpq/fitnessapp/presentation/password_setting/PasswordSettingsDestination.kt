package ptit.vietpq.fitnessapp.presentation.password_setting

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object PasswordSettingDestination : FitnessNavigationDestination {
    override val route: String = "password_setting_route"
    override val destination: String = "password_setting_destination"
}

fun NavGraphBuilder.passwordSettingGraph(
    onBackPressed: () -> Unit,
) =
    composable(route = PasswordSettingDestination.route) {
        PasswordSettingsRoute(
            onBackPressed = onBackPressed,
        )
    }
