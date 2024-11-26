package ptit.vietpq.fitnessapp.presentation.progress

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.data.rememberExtraLambda
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.component.TextComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.ProgressResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramResponse
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.ui.common.LoadingDialog

@Composable
fun UserExerciseProgressRoute(
    onBackPressed: () -> Unit,
    viewModel: ProgressViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UserExerciseProgressScreen(
        uiState = uiState,
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserExerciseProgressScreen(
    uiState: ProgressUiState,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Loading and error handling
    LoadingDialog(uiState.isLoading)
    uiState.error?.let {
//        ErrorDialog(
//            error = it,
//            onDismiss = { /* Handle error */ }
//        )
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(FitnessTheme.color.black),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Progress Tracking",
                        style = FitnessTheme.typo.innerBoldSize20LineHeight28,
                        color = FitnessTheme.color.limeGreen
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = FitnessTheme.color.limeGreen
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FitnessTheme.color.black,
                    titleContentColor = FitnessTheme.color.limeGreen
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FitnessTheme.color.black)
        ) {
            if (uiState.progressList.isEmpty()) {
                ProgressEmpty(
                    onCreateProgressClicked = {}
                )
            } else {
                ProgressOverviewRow(uiState.progressList)

                ProgressChart(uiState.progressList)

                ProgressDetailList(uiState.progressList)
            }

        }
    }
}

@Composable
private fun ProgressEmpty(
    modifier: Modifier = Modifier,
    onCreateProgressClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            modifier = Modifier.size(128.dp),
            tint = FitnessTheme.color.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.oops_you_haven_t_tracked_any_progress_yet),
            fontSize = 18.sp,
            color = Color.White,
            style = FitnessTheme.typo.caption
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onCreateProgressClicked,
            colors = ButtonDefaults.buttonColors(
                containerColor = FitnessTheme.color.limeGreen,
                contentColor = FitnessTheme.color.primary
            )
        ) {
            Text(
                text = stringResource(id = R.string.create_meal_prompt),
                style = FitnessTheme.typo.button,
            )
        }
    }
}

@Composable
private fun ProgressOverviewRow(
    exercises: ImmutableList<ProgressResponse>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatisticItem(
            icon = Icons.Default.FitnessCenter,
            value = exercises.size.toString(),
            label = stringResource(R.string.total_sessions)
        )
        StatisticItem(
            icon = Icons.Default.Refresh,
            value = exercises.sumOf { it.setsCompleted }.toString(),
            label = stringResource(R.string.total_sets)
        )
        StatisticItem(
            icon = Icons.Default.Timer,
            value = stringResource(R.string.sec, exercises.map { it.duration }.average().toInt()),
            label = stringResource(R.string.average_duration),
        )
    }
}

@Composable
private fun ProgressChart(
    exercises: ImmutableList<ProgressResponse>
) {
    val setsData = exercises.mapIndexed { index, exercise ->
        LineCartesianLayerModel.Entry(index, exercise.setsCompleted)
    }
    val repsData = exercises.mapIndexed { index, exercise ->
        LineCartesianLayerModel.Entry(index, exercise.repsCompleted)
    }
    val durationData = exercises.mapIndexed { index, exercise ->
        LineCartesianLayerModel.Entry(index, exercise.duration)
    }

    val cartesianLayerModel = LineCartesianLayerModel(
        series = listOf(
            setsData,
            repsData,
            durationData
        )
    )
    val chartModel = CartesianChartModel(
        models = listOf(cartesianLayerModel),
    )
    val marker = rememberDefaultCartesianMarker(
        label = TextComponent(
            color = FitnessTheme.color.limeGreen.toArgb(),
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Text(
            text = stringResource(R.string.progress_tracking),
            style = FitnessTheme.typo.innerBoldSize16LineHeight24,
            modifier = Modifier.padding(16.dp)
        )
        CartesianChartHost(
            chart =
            rememberCartesianChart(
                rememberLineCartesianLayer(
                    LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.rememberLine(
                            remember { LineCartesianLayer.LineFill.single(fill(Color(0xffa485e0))) }
                        )
                    )
                ),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis =
                HorizontalAxis.rememberBottom(
                    guideline = null,
                    itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                ),
                marker = marker,
                layerPadding = cartesianLayerPadding(scalableStart = 16.dp, scalableEnd = 16.dp),
                persistentMarkers = rememberExtraLambda(marker) { marker at 0 to "0" },
            ),
            model = chartModel,
            zoomState = rememberVicoZoomState(zoomEnabled = false),
        )
    }
}

@Composable
private fun ProgressDetailList(
    exercises: ImmutableList<ProgressResponse>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Text(
            text = stringResource(R.string.session_details),
            style = FitnessTheme.typo.innerBoldSize16LineHeight24,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn {
            items(items = exercises, key = { it.id }) { exercise ->
                ProgressDetailItem(exercise)
            }
        }
    }
}

@Composable
private fun ProgressDetailItem(
    exercise: ProgressResponse
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = exercise.exercise?.name.toString(),
                    style = FitnessTheme.typo.innerBoldSize16LineHeight24
                )
                Text(
                    text = exercise.completionDate,
                    style = FitnessTheme.typo.body4,
                    color = Color.Gray
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.sets, exercise.setsCompleted),
                    style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                    modifier = Modifier.padding(end = 8.dp)
                )
                IconButton(
                    onClick = {
                        isExpanded = !isExpanded
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Session Details",
                        tint = FitnessTheme.color.limeGreen
                    )
                }

            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(
                        color = Color.LightGray.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
            ) {
                DetailRow(
                    label = stringResource(R.string.reps_completed),
                    value = exercise.repsCompleted.toString()
                )
                DetailRow(
                    label = stringResource(R.string.weight_used),
                    value = "${exercise.weightUsed} kg"
                )
                DetailRow(
                    label = stringResource(R.string.duration),
                    value = stringResource(R.string.sec, exercise.duration)
                )
                DetailRow(label = stringResource(R.string.status), value = exercise.status)

                exercise.notes.let { notes ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.notes, notes),
                        style = FitnessTheme.typo.body4,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = FitnessTheme.typo.body4,
            color = Color.Gray
        )
        Text(
            text = value,
            style = FitnessTheme.typo.innerBoldSize16LineHeight24
        )
    }
}

@Composable
private fun StatisticItem(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .background(
                color = Color.White,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = FitnessTheme.color.limeGreen
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = FitnessTheme.typo.innerBoldSize16LineHeight24
        )
        Text(
            text = label,
            style = FitnessTheme.typo.body4,
            color = Color.Gray
        )
    }
}


@Preview
@Composable
private fun UserExerciseProgressScreenPreview() {
    val mockProgressResponse = ProgressResponse(
        userId = 1,
        trainingProgramId = 1,
        exerciseId = 1,
        trainingProgramExerciseId = 1,
        completionDate = "2024-11-19",
        setsCompleted = 3,
        repsCompleted = 15,
        weightUsed = 50,
        duration = 60,
        status = "Completed",
        notes = "Felt good, could increase weight next time",
        id = (1..99999).random(),
        trainingProgram = TrainingProgramResponse(
            id = 1,
            name = "Strength Training",
            description = "A program focused on building strength",
            durationWeeks = 12,
            categoryId = 0,
            difficultyLevel = "lmao",
            imageUrl = "uploads/images/1731561779_14533902968_90b28b1f4c_z.jpg"
        ),
        exercise = ExerciseResponse(
            id = 1,
            name = "Push-Up",
            description = "Classic push-up for upper body strength",
            videoUrl = "uploads/videos/1731561779_449414178_481154647732871_2746085367344298582_n.mp4",
            image = "uploads/images/1731561779_14533902968_90b28b1f4c_z.jpg",
            muscleGroupId = 1
        ),
        trainingProgramExercise = TrainingProgramExerciseResponse(
            id = 1,
            trainingProgramId = 1,
            exerciseId = 1,
            sets = 3,
            reps = 15,
            duration = 60,
            restTime = 70,
            order = 1,
            exercise = ExerciseResponse(
                id = 1,
                name = "Push-Up",
                description = "Classic push-up for upper body strength",
                videoUrl = "uploads/videos/1731561779_449414178_481154647732871_2746085367344298582_n.mp4",
                image = "uploads/images/1731561779_14533902968_90b28b1f4c_z.jpg",
                muscleGroupId = 1
            )
        )
    )

    UserExerciseProgressScreen(
        uiState = ProgressUiState(
            progressList = persistentListOf(
                mockProgressResponse,
                mockProgressResponse,
                mockProgressResponse,
                mockProgressResponse,
                mockProgressResponse
            )
        ),
        onBackPressed = {}
    )
}