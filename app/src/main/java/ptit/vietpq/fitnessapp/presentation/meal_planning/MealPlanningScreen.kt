package ptit.vietpq.fitnessapp.presentation.meal_planning

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.extension.toast
import ptit.vietpq.fitnessapp.ui.theme.FitnessAppTheme

@Composable
fun MealPlanningRoute(
    viewModel: MealPlanningViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.eventFlow.collectAsStateWithLifecycle(initialValue = MealPlanningState.Idle)

    when(val event = state){
        is MealPlanningState.Error -> {
            context.toast(event.message)
        }
        is MealPlanningState.Idle -> Unit
        is MealPlanningState.Loading ->{

        }
        is MealPlanningState.Success -> {
            context.toast(event.response)
        }
    }

    MealPlanningScreen(
        onCreateButtonClicked = viewModel::fetchChatResponse
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MealPlanningScreen(
    onCreateButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedDietaryPreference by remember { mutableStateOf("") }
    var selectedAllergies by remember { mutableStateOf(setOf<String>()) }
    var selectedMealTypes by remember { mutableStateOf(setOf<String>()) }
    var selectedCalorieGoal by remember { mutableStateOf("") }
    var selectedCookingTime by remember { mutableStateOf("") }
    var selectedServings by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.background(Color(0xFF1E1E1E)),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "Meal Planning",
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
                            .clickable {

                            },
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = null
                    )
                }
            )
        },
        contentColor = FitnessTheme.color.primary,
        containerColor = Color(0xFF1E1E1E),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp,
                    )
            ) {

                Spacer(modifier = Modifier.height(24.dp))

                // Dietary Preferences Section
                Section(title = "Dietary Preferences") {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SelectableChip(
                            "Vegetarian",
                            selectedDietaryPreference
                        ) { selectedDietaryPreference = it }
                        SelectableChip("Vegan", selectedDietaryPreference) {
                            selectedDietaryPreference = it
                        }
                        SelectableChip(
                            "Gluten-Free",
                            selectedDietaryPreference
                        ) { selectedDietaryPreference = it }
                        SelectableChip("Keto", selectedDietaryPreference) {
                            selectedDietaryPreference = it
                        }
                        SelectableChip("Paleo", selectedDietaryPreference) {
                            selectedDietaryPreference = it
                        }
                        SelectableChip(
                            "No preferences",
                            selectedDietaryPreference
                        ) { selectedDietaryPreference = it }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Allergies Section
                Section(title = "Allergies") {
                    Text(
                        "Do you have any food allergies we should know about?",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MultiSelectableChip("Nuts", selectedAllergies) { selectedAllergies = it }
                        MultiSelectableChip("Dairy", selectedAllergies) { selectedAllergies = it }
                        MultiSelectableChip("Shellfish", selectedAllergies) {
                            selectedAllergies = it
                        }
                        MultiSelectableChip("Eggs", selectedAllergies) { selectedAllergies = it }
                        MultiSelectableChip("No allergies", selectedAllergies) {
                            selectedAllergies = it
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Meal Types Section
                Section(title = "Meal Types") {
                    Text(
                        "Which meals do you want to plan?",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MultiSelectableChip("Breakfast", selectedMealTypes) {
                            selectedMealTypes = it
                        }
                        MultiSelectableChip("Lunch", selectedMealTypes) { selectedMealTypes = it }
                        MultiSelectableChip("Dinner", selectedMealTypes) { selectedMealTypes = it }
                        MultiSelectableChip("Snacks", selectedMealTypes) { selectedMealTypes = it }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Caloric Goal Section
                Section(title = "Caloric Goal") {
                    Text(
                        "What is your daily caloric intake goal?",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SelectableChip(
                            "Less than 1500 calories",
                            selectedCalorieGoal
                        ) { selectedCalorieGoal = it }
                        SelectableChip("1500-2000 calories", selectedCalorieGoal) {
                            selectedCalorieGoal = it
                        }
                        SelectableChip(
                            "More than 2000 calories",
                            selectedCalorieGoal
                        ) { selectedCalorieGoal = it }
                        SelectableChip(
                            "Not sure/Don't have a goal",
                            selectedCalorieGoal
                        ) { selectedCalorieGoal = it }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Cooking Time Preference Section
                Section(title = "Cooking Time Preference") {
                    Text(
                        "How much time are you willing to spend cooking each meal?",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SelectableChip("Less than 15 minutes", selectedCookingTime) {
                            selectedCookingTime = it
                        }
                        SelectableChip("15-30 minutes", selectedCookingTime) {
                            selectedCookingTime = it
                        }
                        SelectableChip("More than 30 minutes", selectedCookingTime) {
                            selectedCookingTime = it
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Number of Servings Section
                Section(title = "Number Of Servings") {
                    Text(
                        "How many servings do you need per meal?",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SelectableChip("1", selectedServings) { selectedServings = it }
                        SelectableChip("2", selectedServings) { selectedServings = it }
                        SelectableChip("3-4", selectedServings) { selectedServings = it }
                        SelectableChip("More than 4", selectedServings) { selectedServings = it }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Create Button
                Button(
                    onClick = {
                        onCreateButtonClicked(
                            "Dietary Preference: $selectedDietaryPreference\n" +
                                    "Allergies: $selectedAllergies\n" +
                                    "Meal Types: $selectedMealTypes\n" +
                                    "Caloric Goal: $selectedCalorieGoal\n" +
                                    "Cooking Time: $selectedCookingTime\n" +
                                    "Servings: $selectedServings" +
                                    "Make a diet plan for me"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Create", color = Color.White)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    )


}

@Composable
private fun Section(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFCCFF00)
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun SelectableChip(
    text: String,
    selectedOption: String,
    onSelectionChanged: (String) -> Unit
) {
    val isSelected = text == selectedOption
    SuggestionChip(
        onClick = { onSelectionChanged(text) },
        label = { Text(text) },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = if (isSelected) Color(0xFF6200EE) else Color(0xFF1F1F1F),
            labelColor = if (isSelected) Color.White else Color.Gray
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Color(0xFF6200EE) else Color.Gray
        )
//        SuggestionChipDefaults.suggestionChipBorder(
//            borderColor = if (isSelected) Color(0xFF6200EE) else Color.Gray
//        )
    )
}

@Composable
private fun MultiSelectableChip(
    text: String,
    selectedOptions: Set<String>,
    onSelectionChanged: (Set<String>) -> Unit
) {
    val isSelected = selectedOptions.contains(text)
    SuggestionChip(
        onClick = {
            if (isSelected) {
                onSelectionChanged(selectedOptions - text)
            } else {
                onSelectionChanged(selectedOptions + text)
            }
        },
        label = { Text(text) },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = if (isSelected) Color(0xFF6200EE) else Color(0xFF1F1F1F),
            labelColor = if (isSelected) Color.White else Color.Gray
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Color(0xFF6200EE) else Color.Gray
        )
    )
}

@Preview
@Composable
private fun PreviewMealPlanningScreen() {
    FitnessAppTheme {
        MealPlanningScreen({})
    }
}