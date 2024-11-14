package ptit.vietpq.fitnessapp.presentation.training_program_exercise

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramResponse

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