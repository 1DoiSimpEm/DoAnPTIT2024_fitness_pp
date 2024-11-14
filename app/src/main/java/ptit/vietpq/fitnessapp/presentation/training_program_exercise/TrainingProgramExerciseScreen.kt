package ptit.vietpq.fitnessapp.presentation.training_program_exercise

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import ptit.vietpq.fitnessapp.extension.withUrl

@Composable
fun TrainingProgramExerciseRoute(
    onExerciseSelected: (TrainingProgramExerciseResponse) -> Unit,
    onBackPressed: () -> Unit,
    viewModel: TrainingProgramExerciseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TrainingProgramExerciseScreen(
        uiState = uiState,
        onExerciseSelected = onExerciseSelected,
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingProgramExerciseScreen(
    uiState: TrainingProgramExerciseUiState,
    onExerciseSelected: (TrainingProgramExerciseResponse) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState =
        rememberPagerState(initialPage = 0, pageCount = { uiState.trainingProgramExercises.size })

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(FitnessTheme.color.black),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Training Program Exercises",
                        style = FitnessTheme.typo.innerBoldSize20LineHeight28,
                        color = FitnessTheme.color.limeGreen
                    )
                },
                colors = TopAppBarColors(
                    containerColor = FitnessTheme.color.black,
                    actionIconContentColor = FitnessTheme.color.limeGreen,
                    scrolledContainerColor = FitnessTheme.color.black,
                    titleContentColor = FitnessTheme.color.limeGreen,
                    navigationIconContentColor = FitnessTheme.color.limeGreen
                ),
                navigationIcon = {
                    Image(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable(onClick = onBackPressed),
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = "Back"
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FitnessTheme.color.black)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).background(Color.Transparent)
            ) { page ->
                val item = uiState.trainingProgramExercises[page]
                ExerciseCard(
                    exercise = item,
                    sets = item.sets,
                    reps = item.reps,
                    duration = item.duration,
                    restTime = item.restTime,
                    onExerciseSelected = onExerciseSelected
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ExerciseCard(
    exercise: TrainingProgramExerciseResponse,
    sets: Int,
    reps: Int,
    duration: Int,
    restTime: Int,
    onExerciseSelected: (TrainingProgramExerciseResponse) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onExerciseSelected(exercise) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = exercise.exercise.image.withUrl(),
                contentDescription = exercise.exercise.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = exercise.exercise.name,
                style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exercise.exercise.description,
                style = FitnessTheme.typo.body4,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Sets",
                        style = FitnessTheme.typo.body4,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = sets.toString(),
                        style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                        color = Color.Black
                    )
                }
                Column {
                    Text(
                        text = "Reps",
                        style = FitnessTheme.typo.body4,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = reps.toString(),
                        style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                        color = Color.Black
                    )
                }
                Column {
                    Text(
                        text = "Duration",
                        style = FitnessTheme.typo.body4,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$duration min",
                        style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                        color = Color.Black
                    )
                }
                Column {
                    Text(
                        text = "Rest Time",
                        style = FitnessTheme.typo.body4,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$restTime sec",
                        style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TrainingProgramExerciseScreenPreview() {
     val execises = listOf(
        TrainingProgramExerciseResponse(
            id = 1,
            trainingProgramId = 1,
            exerciseId = 1,
            sets = 3,
            reps = 10,
            duration = 30,
            restTime = 30,
            order = 1,
            exercise = ExerciseResponse(
                id = 1,
                name = "Push Up",
                description = "Push up is a great exercise for your chest and triceps",
                image = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
            )
        ),
     )


        TrainingProgramExerciseScreen(
            uiState = TrainingProgramExerciseUiState(
                trainingProgramExercises = execises
            ),
            onExerciseSelected = {},
            onBackPressed = {}
        )
}