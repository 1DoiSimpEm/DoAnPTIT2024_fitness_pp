package ptit.vietpq.fitnessapp.presentation.training_program


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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Schedule
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import kotlinx.collections.immutable.persistentListOf
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.remote.response.TrainingCategoryResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramResponse
import ptit.vietpq.fitnessapp.ui.common.LoadingAnimation

@Composable
fun TrainingProgramRoute(
    onBackPressed: () -> Unit,
    onProgramClicked: (TrainingProgramResponse) -> Unit,
    viewModel: TrainingProgramViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TrainingProgramScreen(
        uiState = uiState,
        onBackPressed = onBackPressed,
        onProgramClicked = onProgramClicked,
        onCategorySelected = viewModel::updateSelectedCategory
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingProgramScreen(
    uiState: TrainingProgramUiState,
    onBackPressed: () -> Unit,
    onCategorySelected: (Int) -> Unit,
    onProgramClicked: (TrainingProgramResponse) -> Unit,
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
                        items = uiState.programs,
                        key = {
                            it.id
                        }) { program ->
                        TrainingProgramCard(program = program, onProgramClicked = onProgramClicked)
                    }
                }
            }
        }
    }
}


@Composable
private fun CategoryChip(
    categoryResponse: TrainingCategoryResponse,
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
private fun TrainingProgramCard(
    program: TrainingProgramResponse,
    onProgramClicked: (TrainingProgramResponse) -> Unit
) {
    val context = LocalContext.current
    var isImageLoading by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onProgramClicked(program) },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    model = ImageRequest.Builder(context)
                        .data(program.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Fit,
                    onLoading = {
                        isImageLoading = true
                    },
                    onSuccess = {
                        isImageLoading = false
                    },
                    onError =  {
                        isImageLoading = false
                    }
                )

                LoadingAnimation(showAnim = isImageLoading)

                // Difficulty Badge
                Surface(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart),
                    color = when (program.difficultyLevel.lowercase()) {
                        "beginner" -> Color(0xFF4CAF50)
                        "intermediate" -> Color(0xFFFFA000)
                        else -> Color(0xFFE53935)
                    },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = program.difficultyLevel,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = FitnessTheme.typo.caption.copy(color = Color.White)
                    )
                }
            }

            // Content Section
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
                    Text(
                        text = program.name,
                        style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_star),
//                        contentDescription = "Favorite",
//                        modifier = Modifier.size(24.dp),
//                        tint = FitnessTheme.color.limeGreen
//                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = program.description,
                    style = FitnessTheme.typo.body4,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Program Details Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Duration
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = FitnessTheme.color.limeGreen
                        )
                        Text(
                            text = "${program.durationWeeks} weeks",
                            style = FitnessTheme.typo.caption,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTrainingProgramScreen() {
    TrainingProgramScreen(
        uiState = TrainingProgramUiState(
            categories = persistentListOf(
                TrainingCategoryResponse(1, "lmao", "Beginner"),
                TrainingCategoryResponse(2, "Intermediate", "Intermediate"),
            ),
            programs = persistentListOf(
                TrainingProgramResponse(
                    1,
                    "Beginner Program",
                    "https://via.placeholder.com/150",
                    1,
                    "This is a beginner program",
                    4,
                    "11"
                ),

                ),
            selectedCategory = "Beginner",
            isLoading = false
        ),
        onBackPressed = {},
        onCategorySelected = {},
        onProgramClicked = {}
    )
}