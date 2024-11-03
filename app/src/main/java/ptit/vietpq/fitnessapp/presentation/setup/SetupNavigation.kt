package ptit.vietpq.fitnessapp.presentation.setup

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object SetupDestination : FitnessNavigationDestination {
    override val route: String = "setup_route"
    override val destination: String = "setup_destination"
}

fun NavGraphBuilder.setupGraph(
    onSetupCompleted : () -> Unit
) =
    composable(route = SetupDestination.route) {
        SetupRoute(onSetupComplete = onSetupCompleted)
    }