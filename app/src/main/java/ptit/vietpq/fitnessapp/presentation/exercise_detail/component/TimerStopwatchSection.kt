package ptit.vietpq.fitnessapp.presentation.exercise_detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.presentation.exercise_detail.StopWatchState
import ptit.vietpq.fitnessapp.presentation.exercise_detail.TimerState

@Composable
fun TimerStopwatchSection(
    timerState: TimerState,
    stopwatchState: StopWatchState,
    selectedDuration: Long,
    onTimerStart: () -> Unit,
    onTimerPause: () -> Unit,
    onTimerReset: () -> Unit,
    onStopwatchStart: () -> Unit,
    onStopwatchPause: () -> Unit,
    onStopwatchReset: () -> Unit,
    modifier: Modifier = Modifier,
    onDurationSelected: (Long) -> Unit = {},

    ) {
    var showTimerPicker by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = FitnessTheme.color.white.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Timer Section
            Text(
                text = stringResource(R.string.timer),
                style = MaterialTheme.typography.titleMedium,
                color = FitnessTheme.color.primary
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (timerState) {
                        is TimerState.Running -> formatTime(timerState.timeLeft)
                        is TimerState.Idle -> formatTime(selectedDuration)
                        is TimerState.Paused -> stringResource(R.string.paused)
                        is TimerState.Finished -> stringResource(R.string.time_s_up)
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    color = FitnessTheme.color.primary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { showTimerPicker = true }) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = stringResource(R.string.set_timer),
                            tint = FitnessTheme.color.primary
                        )
                    }

                    IconButton(
                        onClick = when (timerState) {
                            is TimerState.Running -> onTimerPause
                            else -> onTimerStart
                        }
                    ) {
                        Icon(
                            imageVector = if (timerState is TimerState.Running)
                                Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (timerState is TimerState.Running)
                                "Pause" else "Start",
                            tint = FitnessTheme.color.primary
                        )
                    }

                    IconButton(onClick = onTimerReset) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Reset",
                            tint = FitnessTheme.color.primary
                        )
                    }
                }
            }

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = FitnessTheme.color.primary.copy(alpha = 0.2f)
            )

            // Stopwatch Section
            Text(
                text = stringResource(R.string.stopwatch),
                style = MaterialTheme.typography.titleMedium,
                color = FitnessTheme.color.primary
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (stopwatchState) {
                        is StopWatchState.Running -> formatTime(stopwatchState.elapsedTime)
                        is StopWatchState.Paused -> formatTime(stopwatchState.elapsedTime)
                        is StopWatchState.Idle -> "00:00"
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    color = FitnessTheme.color.primary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = when (stopwatchState) {
                            is StopWatchState.Running -> onStopwatchPause
                            else -> onStopwatchStart
                        }
                    ) {
                        Icon(
                            imageVector = if (stopwatchState is StopWatchState.Running)
                                Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (stopwatchState is StopWatchState.Running)
                                "Pause" else "Start",
                            tint = FitnessTheme.color.primary
                        )
                    }

                    IconButton(onClick = onStopwatchReset) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Reset",
                            tint = FitnessTheme.color.primary
                        )
                    }
                }
            }
        }
    }

    if (showTimerPicker) {
        AlertDialog(
            onDismissRequest = { showTimerPicker = false },
            title = { Text(stringResource(R.string.set_timer_duration)) },
            text = {
                Column {
                    listOf(30L, 60L, 90L, 120L, 180L, 300L).forEach { seconds ->
                        TextButton(
                            onClick = {
                                onDurationSelected(seconds)
                                showTimerPicker = false
                            }
                        ) {
                            Text(formatTime(seconds))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTimerPicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

private fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}


@Preview
@Composable
private fun TimerStopwatchSectionPreview() {
    TimerStopwatchSection(
        timerState = TimerState.Idle,
        stopwatchState = StopWatchState.Idle,
        selectedDuration = 90L,
        onTimerStart = {},
        onTimerPause = {},
        onTimerReset = {},
        onStopwatchStart = {},
        onStopwatchPause = {},
        onStopwatchReset = {},
        onDurationSelected = {}
    )
}