package ptit.vietpq.fitnessapp.presentation.workout_plan

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.data.remote.response.WorkoutPlanResponse
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object WorkoutPlanDestination : FitnessNavigationDestination {
    override val route: String = "workout_plan_route"
    override val destination: String = "workout_plan_destination"
}

fun NavGraphBuilder.workoutCalendarGraph(
    onBackPressed: () -> Unit,
    onWorkoutPlanClicked: (WorkoutPlanResponse) -> Unit,
    ) =
    composable(route = WorkoutPlanDestination.route) {
        WorkoutCalendarRoute(
            onBackPressed = onBackPressed,
            onWorkoutPlanClicked = onWorkoutPlanClicked
        )
    }