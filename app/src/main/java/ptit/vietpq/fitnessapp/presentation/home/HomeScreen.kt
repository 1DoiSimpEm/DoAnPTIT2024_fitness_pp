package ptit.vietpq.fitnessapp.presentation.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramResponse
import ptit.vietpq.fitnessapp.presentation.home.component.TrainingProgramCard
import ptit.vietpq.fitnessapp.presentation.home.component.ExitDialog
import ptit.vietpq.fitnessapp.presentation.home.component.HomeFeature

@Composable
fun HomeRoute(
    onUserClicked: () -> Unit,
    onWorkoutClicked: () -> Unit,
    onSeeAllClicked: () -> Unit,
    onProgressClicked: () -> Unit,
    onNutritionClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onRecommendedItemClicked: (TrainingProgramResponse) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onUserClicked = onUserClicked,
        onWorkoutClicked = onWorkoutClicked,
        onProgressClicked = onProgressClicked,
        onNutritionClicked = onNutritionClicked,
        onSettingsClicked = onSettingsClicked,
        onSeeAllClicked = onSeeAllClicked,
        onRecommendedItemClicked =  onRecommendedItemClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSeeAllClicked: () -> Unit,
    onUserClicked: () -> Unit,
    onWorkoutClicked: () -> Unit,
    onProgressClicked: () -> Unit,
    onNutritionClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onRecommendedItemClicked: (TrainingProgramResponse) -> Unit,
    uiState: HomeUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }

    if (showExitDialog) {
        ExitDialog(
            onConfirm = {
                (context as? Activity)?.finishAffinity()
            },
            onDismiss = {
                showExitDialog = false
            }
        )
    }

    BackHandler {
        showExitDialog = true
    }

    Scaffold(
        modifier = modifier,
        containerColor = FitnessTheme.color.black,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Hi Viet!",
                            color = FitnessTheme.color.primary,
                            style = FitnessTheme.typo.innerBoldSize20LineHeight28
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Text(
                            text = stringResource(R.string.it_s_time_to_challenge_your_limits),
                            color = Color.White,
                            style = FitnessTheme.typo.innerBoldSize12Line16
                        )
                    }
                },
                actions = {
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search",
                        )
                        Spacer(modifier = Modifier.size(24.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_notification),
                            contentDescription = "Notification",
                        )
                        Spacer(modifier = Modifier.size(24.dp))
                        Image(
                            modifier = Modifier.clickable { onUserClicked() },
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = "User",
                        )
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                },
                colors = TopAppBarColors(
                    containerColor = FitnessTheme.color.black,
                    actionIconContentColor = FitnessTheme.color.limeGreen,
                    scrolledContainerColor = FitnessTheme.color.black,
                    titleContentColor = FitnessTheme.color.limeGreen,
                    navigationIconContentColor = FitnessTheme.color.limeGreen
                )
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                HomeFeature(
                    modifier = Modifier.fillMaxWidth(),
                    onFeatureSelected = {
                        when (it) {
                            0 -> {
                                onWorkoutClicked()
                            }

                            1 -> {
                                onProgressClicked()
                            }

                            2 -> {
                                onNutritionClicked()
                            }

                            3 -> {
                                onSettingsClicked()
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.recommended_for_you),
                        style = FitnessTheme.typo.body,
                        color = FitnessTheme.color.limeGreen
                    )
                    Text(
                        modifier = Modifier.clickable(onClick = onSeeAllClicked),
                        text = stringResource(R.string.see_all),
                        style = FitnessTheme.typo.body.copy(
                            textDecoration = TextDecoration.Underline,
                            fontStyle = FontStyle.Italic
                        ),
                        color = FitnessTheme.color.white
                    )
                }
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier,
                ) {
                    items(items = uiState.exercises, key = {
                        it.id
                    }) { item ->
                        TrainingProgramCard(
                            onItemClicked = onRecommendedItemClicked,
                            trainingProgram = item
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        onUserClicked = {},
        uiState = HomeUiState(),
        onWorkoutClicked = {},
        onProgressClicked = {},
        onNutritionClicked = {},
        onSeeAllClicked = {},
        onSettingsClicked = {},
        onRecommendedItemClicked = {})
}