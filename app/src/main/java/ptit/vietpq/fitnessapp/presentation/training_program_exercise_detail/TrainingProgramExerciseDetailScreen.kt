package ptit.vietpq.fitnessapp.presentation.training_program_exercise_detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExercise
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.domain.model.ProgressData
import ptit.vietpq.fitnessapp.extension.withUrl
import ptit.vietpq.fitnessapp.presentation.exercise_detail.TimerState
import ptit.vietpq.fitnessapp.presentation.exercise_detail.component.Congrats
import ptit.vietpq.fitnessapp.presentation.exercise_detail.component.TimerStopwatchSection
import ptit.vietpq.fitnessapp.presentation.exercise_detail.component.VideoPlayer

@Composable
fun TrainingProgramExerciseDetailRoute(
    onBackPressed: () -> Unit,
    viewModel: TrainingProgramExerciseDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCongrats by remember {
        mutableStateOf(false)
    }
    var showProgressDialog by remember { mutableStateOf(false) }
    if (showProgressDialog) {
        ExerciseProgressDialog(
            exercise = uiState.exercise,
            onDismiss = { showProgressDialog = false },
            onSubmit = {
                viewModel.postProgress(it)
            }
        )
    }

    LaunchedEffect(uiState.timerState) {
        when (uiState.timerState) {
            TimerState.Finished -> {
                showCongrats = true
            }

            TimerState.Idle -> Unit
            TimerState.Paused -> Unit
            is TimerState.Running -> Unit
        }
    }

    AnimatedContent(
        targetState = showCongrats,
        transitionSpec = {
            (slideInHorizontally() + fadeIn()) togetherWith
                    (slideOutHorizontally() + fadeOut())
        },
        label = ""
    ) { onCongrats ->
        if (onCongrats) {
            Congrats()
        } else {
            TrainingProgramExerciseDetailScreen(
                uiState = uiState,
                onBackPressed = onBackPressed,
                onTimerStart = viewModel::startTimer,
                onTimerPause = viewModel::pauseTimer,
                onTimerReset = viewModel::resetTimer,
                onStopwatchStart = viewModel::startStopwatch,
                onStopwatchPause = viewModel::pauseStopwatch,
                onStopwatchReset = viewModel::resetStopwatch,
                onFinishExercise = {
                    showProgressDialog = true
                }
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingProgramExerciseDetailScreen(
    uiState: TrainingProgramExerciseDetailUiState,
    onBackPressed: () -> Unit,
    onTimerStart: () -> Unit,
    onTimerPause: () -> Unit,
    onTimerReset: () -> Unit,
    onStopwatchStart: () -> Unit,
    onStopwatchPause: () -> Unit,
    onStopwatchReset: () -> Unit,
    onFinishExercise: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chipColor =
        ChipColors(
            containerColor = FitnessTheme.color.blackBg.copy(alpha = 0.25f),
            labelColor = FitnessTheme.color.blackBg,
            leadingIconContentColor = FitnessTheme.color.blackBg,
            trailingIconContentColor = FitnessTheme.color.blackBg,
            disabledLabelColor = FitnessTheme.color.blackBg,
            disabledContainerColor = FitnessTheme.color.blackBg.copy(alpha = 0.1f),
            disabledLeadingIconContentColor = FitnessTheme.color.blackBg,
            disabledTrailingIconContentColor = FitnessTheme.color.blackBg
        )

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = FitnessTheme.color.limeGreen
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        uiState.exercise.exerciseName,
                        color = FitnessTheme.color.primary
                    )
                },
                colors = TopAppBarColors(
                    containerColor = FitnessTheme.color.blackBg,
                    scrolledContainerColor = FitnessTheme.color.blackBg,
                    navigationIconContentColor = FitnessTheme.color.primary,
                    titleContentColor = FitnessTheme.color.primary,
                    actionIconContentColor = FitnessTheme.color.primary

                ),
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = FitnessTheme.color.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onFinishExercise) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Profile",
                            tint = FitnessTheme.color.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(FitnessTheme.color.blackBg)
        ) {
            VideoPlayer(
                thumbnailUrl = uiState.exercise.exerciseImage.withUrl(),
                videoUrl = uiState.exercise.exerciseVideoUrl.withUrl(),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TimerStopwatchSection(
                timerState = uiState.timerState,
                stopwatchState = uiState.stopwatchState,
                selectedDuration = uiState.exercise.duration.toLong(),
                onTimerStart = onTimerStart,
                onTimerPause = onTimerPause,
                onTimerReset = onTimerReset,
                onStopwatchStart = onStopwatchStart,
                onStopwatchPause = onStopwatchPause,
                onStopwatchReset = onStopwatchReset,
            )


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FitnessTheme.color.limeGreen
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = uiState.exercise.exerciseName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = FitnessTheme.color.blackBg
                    )

                    Text(
                        text = uiState.exercise.exerciseDescription,
                        fontSize = 14.sp,
                        color = FitnessTheme.color.blackBg.copy(alpha = 0.7f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Chip(
                            onClick = { },
                            colors = chipColor,
                            label = {
                                Row {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(R.drawable.ic_timer),
                                        contentDescription = "Duration",
                                        tint = FitnessTheme.color.primary
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "${uiState.exercise.duration} Seconds",
                                    color = Color.White
                                )
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Chip(
                            onClick = { },
                            colors = chipColor,
                            label = {
                                Row {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(R.drawable.ic_fire),
                                        contentDescription = "Difficulty",
                                        tint = FitnessTheme.color.primary
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    stringResource(id = R.string.intermediate),
                                    color = Color.White
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
fun ExerciseProgressDialog(
    exercise: TrainingProgramExercise,
    onDismiss: () -> Unit,
    onSubmit: (ProgressData) -> Unit,
    modifier: Modifier = Modifier
) {
    var setsCompleted by remember { mutableStateOf("") }
    var repsCompleted by remember { mutableStateOf("") }
    var weightUsed by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF282c34)
            ),
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.log_progress_for, exercise.exerciseName),
                    style = FitnessTheme.typo.innerBoldSize20LineHeight28,
                    color = FitnessTheme.color.limeGreen,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Sets Completed TextField
                OutlinedTextField(
                    value = setsCompleted,
                    onValueChange = { setsCompleted = it },
                    label = {
                        Text(
                            stringResource(R.string.sets_completed),
                            color = Color.White
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = FitnessTheme.color.limeGreen,
                        focusedLabelColor = FitnessTheme.color.limeGreen,
                        unfocusedLabelColor = Color.White,
                        errorBorderColor = Color.White,
                        disabledBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Reps Completed TextField
                OutlinedTextField(
                    value = repsCompleted,
                    onValueChange = { repsCompleted = it },
                    label = {
                        Text(
                            stringResource(R.string.reps_completed),
                            color = Color.White
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = FitnessTheme.color.limeGreen,
                        focusedLabelColor = FitnessTheme.color.limeGreen,
                        unfocusedLabelColor = Color.White,
                        errorBorderColor = Color.White,
                        disabledBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Weight Used TextField
                OutlinedTextField(
                    value = weightUsed,
                    onValueChange = { weightUsed = it },
                    label = {
                        Text(
                            stringResource(R.string.weight_used_kg),
                            color = Color.White
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = FitnessTheme.color.limeGreen,
                        focusedLabelColor = FitnessTheme.color.limeGreen,
                        unfocusedLabelColor = Color.White,
                        errorBorderColor = Color.White,
                        disabledBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Notes TextField
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = {
                        Text(
                            stringResource(R.string.notes_optional),
                            color = Color.White
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = FitnessTheme.color.limeGreen,
                        focusedLabelColor = FitnessTheme.color.limeGreen,
                        unfocusedLabelColor = Color.White,
                        errorBorderColor = Color.White,
                        disabledBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // Cancel Button
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White,
                            containerColor = Color.Red
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            text = stringResource(R.string.cancel)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            onSubmit(
                                ProgressData(
                                    setsCompleted = setsCompleted.toIntOrNull() ?: 0,
                                    repsCompleted = repsCompleted.toIntOrNull() ?: 0,
                                    weightUsed = weightUsed.toIntOrNull() ?: 0,
                                    notes = notes
                                )
                            )
                            onDismiss()
                        },
                        enabled = setsCompleted.isNotBlank() && repsCompleted.isNotBlank() && weightUsed.isNotBlank(),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = FitnessTheme.color.limeGreen,
                            contentColor = FitnessTheme.color.black,
                            disabledContainerColor = FitnessTheme.color.primaryDisable
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            text = stringResource(R.string.save_progress),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewProgressDialog() {
    ExerciseProgressDialog(
        exercise = TrainingProgramExercise(
            id = 1,
            exerciseId = 1,
            exerciseName = "Bench Press",
            exerciseDescription = "Bench press is a great exercise for your chest",
            exerciseImage = "https://via.placeholder.com/150",
            exerciseVideoUrl = "https://via.placeholder.com/150",
            duration = 30,
            trainingProgramId = 1,
            sets = 1,
            restTime = 50,
            order = 1,
            exerciseMuscleGroupId = 1,
            reps = 12,
        ),
        onDismiss = { },
        onSubmit = { }
    )
}


@Composable
private fun Chip(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    colors: ChipColors
) {
    SuggestionChip(
        onClick = onClick,
        label = label,
        colors = colors,
        shape = RoundedCornerShape(50),
        border = null
    )
}
