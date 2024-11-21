package ptit.vietpq.fitnessapp.presentation.progress

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object ProgressDestination : FitnessNavigationDestination {
    override val route: String = "progress_route"
    override val destination: String = "progress_destination"
}

fun NavGraphBuilder.progressGraph(
    onBackPressed: () -> Unit,
) =
    composable(route = ProgressDestination.route) {
        UserExerciseProgressRoute(onBackPressed = onBackPressed)
    }