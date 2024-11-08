package ptit.vietpq.fitnessapp.presentation.meal_planning

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object MealPlanningDestination : FitnessNavigationDestination {
    override val route: String = "meal_planning_route"
    override val destination: String = "meal_planning_destination"
}

fun NavGraphBuilder.mealPlanningRoute(
    onBackPressed: () -> Unit,
    onMealDetailedNavigating: (String) -> Unit,
) =
    composable(route = MealPlanningDestination.route) {
        MealPlanningRoute(
            onMealDetailedNavigating = onMealDetailedNavigating,
            onBackPressed = onBackPressed
        )
    }