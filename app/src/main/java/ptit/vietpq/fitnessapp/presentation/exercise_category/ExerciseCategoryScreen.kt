package ptit.vietpq.fitnessapp.presentation.exercise_category

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import kotlinx.collections.immutable.persistentListOf
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.MuscleGroupResponse
import ptit.vietpq.fitnessapp.ui.common.LoadingAnimation

@Composable
fun ExerciseCategoryRoute(
    onBackPressed: () -> Unit,
    onExerciseClicked: (ExerciseResponse) -> Unit,
    viewModel: ExerciseCategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ExerciseCategoryScreen(
        uiState = uiState,
        onBackPressed = onBackPressed,
        onExerciseClicked = onExerciseClicked,
        onCategorySelected = viewModel::updateSelectedCategory
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCategoryScreen(
    uiState: ExerciseCategoryUiState,
    onBackPressed: () -> Unit,
    onCategorySelected: (Int) -> Unit,
    onExerciseClicked: (ExerciseResponse) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier.background(Color(0xFF1E1E1E)),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = uiState.selectedCategory,
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
        },
        contentColor = FitnessTheme.color.primary,
        containerColor = Color(0xFF1E1E1E)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            // Category Chips
            LazyRow(
                contentPadding = PaddingValues(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(uiState.categories) { category ->
                    CategoryChip(
                        categoryResponse = category,
                        isSelected = category.name == uiState.selectedCategory,
                        onClick = onCategorySelected
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center),
                        color = FitnessTheme.color.limeGreen
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = uiState.exercises,
                        key = {
                            it.id
                        }) { exercise ->
                        ExerciseCard(exercise = exercise, onExerciseClicked = onExerciseClicked)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    categoryResponse: MuscleGroupResponse,
    isSelected: Boolean,
    onClick: (Int) -> Unit
) {
    val selectedBackgroundColorAnimate by animateColorAsState(
        if (isSelected) FitnessTheme.color.limeGreen else Color(0xFF2C2C2C),
        label = "isSelectedBackgroundColor"
    )
    val selectedTextColorAnimate by animateColorAsState(
        if (isSelected) Color.Black else FitnessTheme.color.limeGreen,
        label = "isSelectedTextColor"
    )
    Surface(
        modifier = Modifier.clickable(onClick = {
            onClick(categoryResponse.id)
        }),
        shape = RoundedCornerShape(16.dp),
        color = selectedBackgroundColorAnimate,
        border = if (!isSelected) BorderStroke(1.dp, FitnessTheme.color.limeGreen) else null
    ) {
        Text(
            text = categoryResponse.name,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = selectedTextColorAnimate,
            style = FitnessTheme.typo.body
        )
    }
}

@Composable
private fun ExerciseCard(
    exercise: ExerciseResponse,
    onExerciseClicked: (ExerciseResponse) -> Unit
) {
    val context = LocalContext.current
    var isImageLoading by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onExerciseClicked(exercise)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight(),
                contentDescription = null,
                model = ImageRequest.Builder(context).data(
                    exercise.image
                ).build(),
                imageLoader = ImageLoader(context),
                contentScale = ContentScale.Fit,
                onLoading = {
                    isImageLoading = true
                },
                onSuccess = {
                    isImageLoading = false
                },
                onError = {
                    isImageLoading = false
                }
            )

            LoadingAnimation(
                showAnim = isImageLoading,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = exercise.name,
                    style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = exercise.description,
                    style = FitnessTheme.typo.body4,
                    color = Color.Gray
                )
            }

            // Star Icon
            Icon(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = "Favorite",
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp),
                tint = FitnessTheme.color.limeGreen
            )
        }
    }
}

@Preview
@Composable
private fun PreviewExerciseCategory() {
    val previewState = ExerciseCategoryUiState(
        isLoading = true,
        categories = persistentListOf(
            MuscleGroupResponse(
                id = 1,
                name = "Beginner",
            ),
            MuscleGroupResponse(
                id = 2,
                name = "Intermediate"
            ),
            MuscleGroupResponse(
                id = 3,
                name = "Advanced"
            )
        ),
        selectedCategory = "Beginner",
        exercises = persistentListOf(
            ExerciseResponse("1", "1", "1", "1", 1, 1)
        )
    )

    ExerciseCategoryScreen(
        uiState = previewState,
        onBackPressed = { /* Handle back navigation */ },
        onCategorySelected = {/*Handle category selected */ },
        onExerciseClicked = {/* */ }
    )
}