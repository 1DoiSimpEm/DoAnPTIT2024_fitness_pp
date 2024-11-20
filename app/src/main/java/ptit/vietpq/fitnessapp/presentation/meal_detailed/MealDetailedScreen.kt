package ptit.vietpq.fitnessapp.presentation.meal_detailed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import dev.jeziellago.compose.markdowntext.MarkdownText
import ptit.vietpq.fitnessapp.R


@Composable
fun MealDetailedRoute(
    onBackPressed: () -> Unit,
    viewModel: MealDetailedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MealPlanDisplayScreen(
        uiState = uiState,
        onBackPressed = onBackPressed
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanDisplayScreen(
    uiState: MealDetailedUiState,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.background(Color(0xFF1E1E1E)),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.your_meal_plan),
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
                .verticalScroll(rememberScrollState())
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            MarkdownText(
                style = FitnessTheme.typo.markDown,
                markdown = uiState.mealDescription,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun MealPlanSection(content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F1F)
        ),
        border = BorderStroke(1.dp, Color(0xFF6200EE)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val lines = content.split("\n")
            lines.forEach { line ->
                when {
                    // Handle Markdown headers
                    line.trim().startsWith("###") -> {
                        Text(
                            text = line.trim().replace("###", "").trim(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCCFF00),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                    // Handle bold text (wrapped in **)
                    line.trim().startsWith("**") && line.trim().endsWith("**") -> {
                        Text(
                            text = line.trim().removeSurrounding("**"),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCCFF00),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    // Handle existing special sections
                    line.trim().startsWith("Day", ignoreCase = true) ||
                            line.trim().startsWith("Breakfast", ignoreCase = true) ||
                            line.trim().startsWith("Lunch", ignoreCase = true) ||
                            line.trim().startsWith("Dinner", ignoreCase = true) -> {
                        Text(
                            text = line.trim(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCCFF00),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    // Handle nutritional information
                    line.trim().startsWith("Calories", ignoreCase = true) ||
                            line.trim().startsWith("Protein", ignoreCase = true) ||
                            line.trim().startsWith("Carbs", ignoreCase = true) ||
                            line.trim().startsWith("Fat", ignoreCase = true) ||
                            line.trim().startsWith("Approx:", ignoreCase = true) ||
                            line.trim().startsWith("If serving", ignoreCase = true) -> {
                        Text(
                            text = line.trim(),
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    // Handle regular content
                    line.isNotBlank() -> {
                        Text(
                            text = line.trim(),
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewMeal() {
    MealPlanDisplayScreen(
        MealDetailedUiState(
            """
            ### Daily Meal Plan
            
            **Breakfast:**
            Oatmeal with berries and honey
            Calories: 300
            Protein: 8g
            
            **Lunch:**
            Grilled chicken salad
            Calories: 450
            Protein: 35g
        """.trimIndent()
        ),
        onBackPressed = { /* Handle back navigation */ }
    )
}