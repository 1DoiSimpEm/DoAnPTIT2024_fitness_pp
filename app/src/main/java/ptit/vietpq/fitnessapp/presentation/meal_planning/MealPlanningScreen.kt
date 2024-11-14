package ptit.vietpq.fitnessapp.presentation.meal_planning

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.extension.toast
import ptit.vietpq.fitnessapp.ui.common.LoadingDialog

@Composable
fun MealPlanningRoute(
    onBackPressed: () -> Unit,
    onMealDetailedNavigating: (String) -> Unit,
    viewModel: MealPlanningViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.eventFlow.collectAsStateWithLifecycle(initialValue = MealPlanningState.Idle)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        when (val event = state) {
            is MealPlanningState.Error -> {
                context.toast(event.message)
            }

            is MealPlanningState.Idle -> Unit

            is MealPlanningState.Success -> {
                onMealDetailedNavigating(event.response)
            }
        }
    }
    MealPlanningScreen(
        onBackPressed = onBackPressed,
        uiState = uiState,
        onCreateButtonClicked = viewModel::fetchChatResponse,
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MealPlanningScreen(
    onBackPressed: () -> Unit,
    onCreateButtonClicked: (String) -> Unit,
    uiState: MealPlanningUiState,
    modifier: Modifier = Modifier,
) {
    var selectedDietaryPreference by remember { mutableStateOf("") }
    var customDietaryPreference by remember { mutableStateOf("") }
    var selectedAllergies by remember { mutableStateOf(setOf<String>()) }
    var customAllergies by remember { mutableStateOf("") }
    var selectedMealTypes by remember { mutableStateOf(setOf<String>()) }
    var customMealTypes by remember { mutableStateOf("") }
    var selectedCalorieGoal by remember { mutableStateOf("") }
    var customCalorieGoal by remember { mutableStateOf("") }
    var selectedCookingTime by remember { mutableStateOf("") }
    var customCookingTime by remember { mutableStateOf("") }
    var selectedServings by remember { mutableStateOf("") }
    var customServings by remember { mutableStateOf("") }

    LoadingDialog(isLoading = uiState.isLoading)

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
                            .clickable(onClick = onBackPressed),
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

                Section(title = stringResource(R.string.dietary_preferences)) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SelectableChip(
                            stringResource(R.string.vegetarian),
                            selectedDietaryPreference
                        ) { selectedDietaryPreference = it }
                        SelectableChip(stringResource(R.string.vegan), selectedDietaryPreference) {
                            selectedDietaryPreference = it
                        }
                        SelectableChip(
                            stringResource(R.string.gluten_free),
                            selectedDietaryPreference
                        ) { selectedDietaryPreference = it }
                        SelectableChip(stringResource(R.string.keto), selectedDietaryPreference) {
                            selectedDietaryPreference = it
                        }
                        SelectableChip(stringResource(R.string.paleo), selectedDietaryPreference) {
                            selectedDietaryPreference = it
                        }
                        SelectableChip(
                            stringResource(R.string.no_preferences),
                            selectedDietaryPreference
                        ) { selectedDietaryPreference = it }
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomInputField(
                            value = customDietaryPreference,
                            onValueChange = {
                                customDietaryPreference = it; selectedDietaryPreference = ""
                            },
                            label = stringResource(R.string.other_dietary_preferences)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Allergies Section
                Section(title = stringResource(R.string.allergies)) {
                    Text(
                        stringResource(R.string.do_you_have_any_food_allergies_we_should_know_about),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MultiSelectableChip(
                            stringResource(R.string.nuts),
                            selectedAllergies
                        ) { selectedAllergies = it }
                        MultiSelectableChip(
                            stringResource(R.string.dairy),
                            selectedAllergies
                        ) { selectedAllergies = it }
                        MultiSelectableChip(stringResource(R.string.shellfish), selectedAllergies) {
                            selectedAllergies = it
                        }
                        MultiSelectableChip(
                            stringResource(R.string.eggs),
                            selectedAllergies
                        ) { selectedAllergies = it }
                        MultiSelectableChip(
                            stringResource(R.string.no_allergies),
                            selectedAllergies
                        ) {
                            selectedAllergies = it
                        }
                        CustomInputField(
                            value = customAllergies,
                            onValueChange = {
                                customAllergies = it; selectedAllergies = setOf()
                            },
                            label = stringResource(R.string.other_allergies),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Meal Types Section
                Section(title = stringResource(R.string.meal_types)) {
                    Text(
                        text = stringResource(R.string.which_meals_do_you_want_to_plan),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MultiSelectableChip(stringResource(R.string.breakfast), selectedMealTypes) {
                            selectedMealTypes = it
                        }
                        MultiSelectableChip(
                            stringResource(R.string.lunch),
                            selectedMealTypes
                        ) { selectedMealTypes = it }
                        MultiSelectableChip(
                            stringResource(R.string.dinner),
                            selectedMealTypes
                        ) { selectedMealTypes = it }
                        MultiSelectableChip(
                            stringResource(R.string.snacks),
                            selectedMealTypes
                        ) { selectedMealTypes = it }
                        CustomInputField(
                            value = customMealTypes,
                            onValueChange = {
                                customMealTypes = it; selectedMealTypes = setOf()
                            },
                            label = stringResource(R.string.another_meal_type)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Caloric Goal Section
                Section(title = stringResource(R.string.caloric_goal)) {
                    Text(
                        text = stringResource(R.string.what_is_your_daily_caloric_intake_goal),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SelectableChip(
                            stringResource(R.string.less_than_1500_calories),
                            selectedCalorieGoal
                        ) { selectedCalorieGoal = it }
                        SelectableChip(
                            stringResource(R.string._1500_2000_calories),
                            selectedCalorieGoal
                        ) {
                            selectedCalorieGoal = it
                        }
                        SelectableChip(
                            stringResource(R.string.more_than_2000_calories),
                            selectedCalorieGoal
                        ) { selectedCalorieGoal = it }
                        SelectableChip(
                            stringResource(R.string.not_sure_don_t_have_a_goal),
                            selectedCalorieGoal
                        ) { selectedCalorieGoal = it }
                        CustomInputField(
                            value = customCalorieGoal,
                            onValueChange = {
                                customCalorieGoal = it; selectedCalorieGoal = ""
                            },
                            label = stringResource(R.string.another_goal)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Cooking Time Preference Section
                Section(title = stringResource(R.string.cooking_time_preference)) {
                    Text(
                        stringResource(R.string.how_much_time_are_you_willing_to_spend_cooking_each_meal),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SelectableChip(
                            stringResource(R.string.less_than_15_minutes),
                            selectedCookingTime
                        ) {
                            selectedCookingTime = it
                        }
                        SelectableChip(
                            stringResource(R.string._15_30_minutes),
                            selectedCookingTime
                        ) {
                            selectedCookingTime = it
                        }
                        SelectableChip(
                            stringResource(R.string.more_than_30_minutes),
                            selectedCookingTime
                        ) {
                            selectedCookingTime = it
                        }
                        CustomInputField(
                            value = customCookingTime,
                            onValueChange = {
                                customCookingTime = it; selectedCookingTime = ""
                            },
                            label = stringResource(R.string.another_cooking_time)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Number of Servings Section
                Section(title = stringResource(R.string.number_of_servings)) {
                    Text(
                        stringResource(R.string.how_many_servings_do_you_need_per_meal),
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
                        SelectableChip(
                            stringResource(R.string.more_than_4),
                            selectedServings
                        ) { selectedServings = it }
                        CustomInputField(
                            value = customServings,
                            onValueChange = {
                                customServings = it; selectedServings = ""
                            },
                            label = stringResource(R.string.another_number_of_servings)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Create Button
                Button(
                    onClick = {
                        val dietaryPref = customDietaryPreference.ifEmpty {
                            selectedDietaryPreference
                        }

                        val allergiesList = buildString {
                            append(selectedAllergies.joinToString(", "))
                            if (customAllergies.isNotEmpty()) {
                                if (isNotEmpty()) append(", ")
                                append(customAllergies)
                            }
                        }

                        onCreateButtonClicked(
                            """
                            Please create a personalized meal plan based on the following preferences:
                            
                            Dietary Preference: $dietaryPref
                            Allergies: $allergiesList
                            Meal Types: ${selectedMealTypes.joinToString(", ")}${if (customMealTypes.isNotEmpty()) ", $customMealTypes" else ""}
                            Caloric Goal: ${customCalorieGoal.ifEmpty { selectedCalorieGoal }}
                            Cooking Time: ${customCookingTime.ifEmpty { selectedCookingTime }}
                            Servings: ${customServings.ifEmpty { selectedServings }}
                            
                            Please provide a detailed meal plan that takes into account all these preferences and restrictions. 
                            Include specific recipes, nutritional information, and preparation instructions for each meal.
                            """.trimIndent()
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
                    Text(stringResource(R.string.create), color = Color.White)
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

@Composable
private fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(Color.Transparent),
        label = { Text(label) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xFF1F1F1F),
            textColor = Color.White,
            cursorColor = Color(0xFF6200EE),
            focusedLabelColor = Color(0xFF6200EE),
            unfocusedLabelColor = Color.Gray,
        ),
        singleLine = true
    )
}


@Preview
@Composable
private fun PreviewMealPlanningScreen() {
    MealPlanningScreen(
        onBackPressed = {},
        onCreateButtonClicked = {},
        uiState = MealPlanningUiState()
    )
}