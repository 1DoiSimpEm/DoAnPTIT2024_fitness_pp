package ptit.vietpq.fitnessapp.presentation.training_program_exercise_detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R
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


    LaunchedEffect(uiState.timerState) {
        when(uiState.timerState){
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
                    IconButton(onClick = { /* Handle profile */ }) {
                        Icon(
                            imageVector = Icons.Default.Star,
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
