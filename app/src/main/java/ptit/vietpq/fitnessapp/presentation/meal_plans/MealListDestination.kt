package ptit.vietpq.fitnessapp.presentation.meal_plans

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.data.remote.response.MealPlanResponse
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object MealListDestination : FitnessNavigationDestination {
    override val route: String = "meal_list_route"
    override val destination: String = "meal_list_destination"
}

fun NavGraphBuilder.mealListGraph(
    onBackPressed: () -> Unit,
    onMealPlanClick: (MealPlanResponse) -> Unit,
    ) =
    composable(route = MealListDestination.route) {
        MealListRoute(onBackPressed = onBackPressed, onMealPlanClick = onMealPlanClick)
    }