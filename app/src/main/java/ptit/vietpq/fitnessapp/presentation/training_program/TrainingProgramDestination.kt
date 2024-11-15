package ptit.vietpq.fitnessapp.presentation.training_program

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramResponse
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object TrainingProgramDestination : FitnessNavigationDestination {
    override val route: String = "training_program_route"
    override val destination: String = "training_program_destination"
}

fun NavGraphBuilder.trainingProgramGraph(
    onBackPressed: () -> Unit,
    onProgramClicked: (TrainingProgramResponse) -> Unit
) =
    composable(route = TrainingProgramDestination.route) {
        TrainingProgramRoute(
            onBackPressed = onBackPressed,
            onProgramClicked = onProgramClicked
        )
    }