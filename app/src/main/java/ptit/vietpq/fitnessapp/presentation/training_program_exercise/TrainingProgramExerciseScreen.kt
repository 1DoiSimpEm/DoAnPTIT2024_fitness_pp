package ptit.vietpq.fitnessapp.presentation.training_program_exercise

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.extension.withUrl
import ptit.vietpq.fitnessapp.ui.common.LoadingDialog

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
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { uiState.trainingProgramExercises.size }
    )

    LoadingDialog(uiState.loading)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(FitnessTheme.color.black),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = onBackPressed,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = FitnessTheme.color.limeGreen
                            )
                        }
                        AnimatedContent(
                            targetState = pagerState.currentPage,
                            transitionSpec = {
                                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                    slideOutHorizontally { width -> -width } + fadeOut())
                            }, label = ""
                        ) { page ->
                            Text(
                                text = "${page + 1}/${uiState.trainingProgramExercises.size}",
                                style = FitnessTheme.typo.innerBoldSize20LineHeight28,
                                color = FitnessTheme.color.limeGreen
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(
                                R.string.exercise
                            ),
                            style = FitnessTheme.typo.innerBoldSize20LineHeight28,
                            color = FitnessTheme.color.limeGreen
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
            Text(
                text = stringResource(R.string.find_your_next_workout),
                style = FitnessTheme.typo.caption,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 16.dp,
                modifier = Modifier.weight(1f)
            ) { page ->
                val exercise = uiState.trainingProgramExercises[page]
                ExerciseCard(
                    exercise = exercise,
                    isSelected = pagerState.currentPage == page,
                    onExerciseSelected = onExerciseSelected
                )
            }

            PagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 24.dp)
            )

            StatisticsRow(
                exercises = uiState.trainingProgramExercises,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun ExerciseCard(
    exercise: TrainingProgramExerciseResponse,
    isSelected: Boolean,
    onExerciseSelected: (TrainingProgramExerciseResponse) -> Unit
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.6f,
        animationSpec = tween(durationMillis = 300), label = ""
    )
    val animatedHeight by animateDpAsState(
        targetValue = if (isSelected) 260.dp else 200.dp,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(
                animatedAlpha
            )
            .graphicsLayer {
                this.alpha = animatedAlpha
                scaleX = 0.95f + (0.05f * animatedAlpha)
                scaleY = 0.95f + (0.05f * animatedAlpha)
            }
            .clickable { onExerciseSelected(exercise) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            hoveredElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(animatedHeight)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = exercise.exercise.image.withUrl(),
                    contentDescription = exercise.exercise.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Exercise",
                        tint = FitnessTheme.color.limeGreen,
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                            .padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = exercise.exercise.name,
                style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                color = FitnessTheme.color.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exercise.exercise.description,
                style = FitnessTheme.typo.body4,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            MetricsRow(exercise)
        }
    }
}

@Composable
private fun MetricsRow(exercise: TrainingProgramExerciseResponse) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MetricItem(
            icon = Icons.Default.FitnessCenter,
            value = exercise.sets.toString(),
            label = stringResource(R.string.sets)
        )
        MetricItem(
            icon = Icons.Default.Refresh,
            value = exercise.reps.toString(),
            label = stringResource(R.string.reps)
        )
        MetricItem(
            icon = Icons.Default.Timer,
            value = stringResource(R.string.min, exercise.duration),
            label = stringResource(R.string.duration)
        )
        MetricItem(
            icon = Icons.Default.Timer,
            value = stringResource(R.string.sec, exercise.restTime),
            label = stringResource(R.string.rest)
        )
    }
}

@Composable
private fun MetricItem(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(72.dp)
            .background(
                color = FitnessTheme.color.black.copy(alpha = 0.05f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = FitnessTheme.color.limeGreen,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            style = FitnessTheme.typo.innerBoldSize16LineHeight24,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Text(
            text = label,
            style = FitnessTheme.typo.body4,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { index ->
            val width by animateDpAsState(
                targetValue = if (pagerState.currentPage == index) 24.dp else 8.dp,
                label = "indicatorWidth"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .width(width)
                    .height(8.dp)
                    .background(
                        color = if (pagerState.currentPage == index)
                            FitnessTheme.color.limeGreen
                        else
                            FitnessTheme.color.limeGreen.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable { /* Add smooth scroll to page */ }
            )
        }
    }
}

@Composable
private fun StatisticsRow(
    exercises: ImmutableList<TrainingProgramExerciseResponse>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatisticItem(
            value = exercises.size.toString(),
            label = stringResource(R.string.exercises),
            icon = Icons.Default.FitnessCenter
        )
        StatisticItem(
            value = stringResource(
                R.string.min,
                exercises.sumOf { it.duration }
            ),
            label = stringResource(R.string.total_time),
            icon = Icons.Default.Timer
        )
        StatisticItem(
            value = exercises.sumOf { it.sets }.toString(),
            label = stringResource(R.string.total_sets),
            icon = Icons.Default.Refresh
        )
    }
}

@Composable
private fun StatisticItem(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = FitnessTheme.color.limeGreen,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            style = FitnessTheme.typo.innerBoldSize16LineHeight24,
            color = Color.Black
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
private fun TrainingProgramExerciseScreenPreview() {
    val execises = persistentListOf(
        TrainingProgramExerciseResponse(
            id = 1,
            trainingProgramId = 1,
            exerciseId = 1,
            sets = 3,
            reps = 10,
            duration = 90,
            restTime = 30,
            order = 1,
            exercise = ExerciseResponse(
                id = 1,
                name = "Push Up",
                description = "Push up is a great exercise for your chest and triceps",
                image = "https://media.tenor.com/6DiM1V23hkwAAAAe/two-black-people.png"
            )
        ),
        TrainingProgramExerciseResponse(
            id = 1,
            trainingProgramId = 1,
            exerciseId = 1,
            sets = 3,
            reps = 10,
            duration = 90,
            restTime = 30,
            order = 1,
            exercise = ExerciseResponse(
                id = 1,
                name = "Push Up",
                description = "Push up is a great exercise for your chest and triceps",
                image = "https://media.tenor.com/6DiM1V23hkwAAAAe/two-black-people.png"
            )
        ),
        TrainingProgramExerciseResponse(
            id = 1,
            trainingProgramId = 1,
            exerciseId = 1,
            sets = 3,
            reps = 10,
            duration = 90,
            restTime = 30,
            order = 1,
            exercise = ExerciseResponse(
                id = 1,
                name = "Push Up",
                description = "Push up is a great exercise for your chest and triceps",
                image = "https://media.tenor.com/6DiM1V23hkwAAAAe/two-black-people.png"
            )
        ),
    )


    TrainingProgramExerciseScreen(
        uiState = TrainingProgramExerciseUiState(
            trainingProgramExercises = execises.toImmutableList()
        ),
        onExerciseSelected = {},
        onBackPressed = {}
    )
}