package ptit.vietpq.fitnessapp.presentation.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object ProfileDestination : FitnessNavigationDestination {
    override val route: String = "profile_route"
    override val destination: String = "profile_destination"
}

fun NavGraphBuilder.profileGraph(
    onLoginNavigate: () -> Unit,
    onSettingNavigate: () -> Unit,
    onBackPressed: () -> Unit,
) =
    composable(route = ProfileDestination.route) {
        ProfileRoute(onBackPressed = onBackPressed, onLoginNavigate = onLoginNavigate, onSettingNavigate = onSettingNavigate)
    }