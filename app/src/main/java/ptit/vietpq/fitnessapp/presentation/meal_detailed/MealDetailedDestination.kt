package ptit.vietpq.fitnessapp.presentation.meal_detailed

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object MealDetailedDestination : FitnessNavigationDestination {
    override val route: String = "meal_detailed_route"
    override val destination: String = "meal_detailed_destination"
}

fun NavGraphBuilder.mealDetailedGraph(
    onBackPressed: () -> Unit,
) =
    composable<MealDetailedUiState> {
        MealDetailedRoute(
            onBackPressed = onBackPressed
        )
    }
