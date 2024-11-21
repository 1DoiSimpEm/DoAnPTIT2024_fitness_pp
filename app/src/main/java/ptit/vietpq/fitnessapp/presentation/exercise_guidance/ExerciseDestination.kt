package ptit.vietpq.fitnessapp.presentation.exercise_guidance

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object ExerciseDestination : FitnessNavigationDestination {
    override val route: String = "exercise_route"
    override val destination: String = "exercise_destination"
}

fun NavGraphBuilder.exerciseGraph(
) =
    composable(route = ExerciseDestination.route) {
        ExerciseScreen()
    }