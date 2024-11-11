package ptit.vietpq.fitnessapp.presentation.exercise_category

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object ExerciseCategoryDestination : FitnessNavigationDestination {
    override val route: String = "exercise_category_route"
    override val destination: String = "exercise_category_destination"
}

fun NavGraphBuilder.exerciseCategoryGraph(
    onExerciseClicked: (ExerciseResponse) -> Unit,
    onBackPressed: () -> Unit,
) =
    composable(route = ExerciseCategoryDestination.route) {
        ExerciseCategoryRoute(
            onBackPressed = onBackPressed,
            onExerciseClicked = onExerciseClicked
        )
    }