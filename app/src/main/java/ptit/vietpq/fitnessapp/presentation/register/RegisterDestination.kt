package ptit.vietpq.fitnessapp.presentation.register

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object RegisterDestination : FitnessNavigationDestination {
    override val route: String = "register_route"
    override val destination: String = "register_destination"
}

fun NavGraphBuilder.registerGraph(
    onBackPressed:() -> Unit,
) =
    composable(route = RegisterDestination.route) {
        RegisterRoute(
            onBackPressed = onBackPressed
        )
    }