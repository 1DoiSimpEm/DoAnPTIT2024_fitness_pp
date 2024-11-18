package ptit.vietpq.fitnessapp.presentation.training_program_exercise_detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExercise


fun NavGraphBuilder.trainingProgramExerciseDetailRoute(
    onBackPressed: () -> Unit,
) =
    composable<TrainingProgramExercise> {
        TrainingProgramExerciseDetailRoute(
            onBackPressed = onBackPressed,
        )
    }