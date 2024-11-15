package ptit.vietpq.fitnessapp.presentation.training_program_exercise

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramResponse
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

fun NavGraphBuilder.trainingProgramExerciseRoute(
    onBackPressed: () -> Unit,
    onExerciseSelected: (TrainingProgramExerciseResponse) -> Unit,
) =
    composable<TrainingProgramResponse> {
        TrainingProgramExerciseRoute(
            onBackPressed = onBackPressed,
            onExerciseSelected = onExerciseSelected
        )
    }


//data object LmaoDestination : FitnessNavigationDestination {
//    override val route: String = "lmao"
//    override val destination: String = "lmao2"
//}
//
//
//fun NavGraphBuilder.trainingProgramExerciseRoute(
//    onBackPressed: () -> Unit,
//    onExerciseSelected: (TrainingProgramExerciseResponse) -> Unit,
//) =
//    composable(LmaoDestination.route) {
//        TrainingProgramExerciseRoute(
//            onBackPressed = onBackPressed,
//            onExerciseSelected = onExerciseSelected
//        )
//    }