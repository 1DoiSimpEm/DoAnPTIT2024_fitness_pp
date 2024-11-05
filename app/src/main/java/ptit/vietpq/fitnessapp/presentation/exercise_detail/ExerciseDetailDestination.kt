package ptit.vietpq.fitnessapp.presentation.exercise_detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object ExerciseDetailDestination : FitnessNavigationDestination {
    override val route: String = "exercise_detail_route"
    override val destination: String = "exercise_detail_destination"
}

fun NavGraphBuilder.exerciseDetailRoute(
) =
    composable(route = ExerciseDetailDestination.route) {
        ExerciseDetailRoute()
    }