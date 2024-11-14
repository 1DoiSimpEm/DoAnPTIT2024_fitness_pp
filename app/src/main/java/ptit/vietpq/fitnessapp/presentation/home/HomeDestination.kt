package ptit.vietpq.fitnessapp.presentation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object HomeDestination : FitnessNavigationDestination {
    override val route: String = "home_route"
    override val destination: String = "home_destination"
}

fun NavGraphBuilder.homeGraph(
    onUserClicked: () -> Unit,
    onWorkoutClicked: () -> Unit,
    onProgressClicked: () -> Unit,
    onNutritionClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onSeeAllClicked: () -> Unit,
) =
    composable(route = HomeDestination.route) {
        HomeRoute(
            onUserClicked = onUserClicked, onWorkoutClicked = onWorkoutClicked,
            onProgressClicked = onProgressClicked,
            onNutritionClicked = onNutritionClicked,
            onSettingsClicked = onSettingsClicked,
            onSeeAllClicked = onSeeAllClicked
        )
    }