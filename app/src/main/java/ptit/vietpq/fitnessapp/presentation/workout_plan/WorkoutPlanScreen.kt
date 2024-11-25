package ptit.vietpq.fitnessapp.presentation.workout_plan

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.WorkoutPlanResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import ptit.vietpq.fitnessapp.domain.model.WorkoutPlan
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.extension.toLocalDate
import ptit.vietpq.fitnessapp.extension.withUrl
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun WorkoutCalendarRoute(
    onBackPressed: () -> Unit,
    onWorkoutPlanClicked: (WorkoutPlanResponse) -> Unit,
    viewModel: WorkoutPlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    WorkoutCalendarScreen(
        uiState = uiState,
        onBackPressed = onBackPressed,
        onWorkoutPlanClicked = onWorkoutPlanClicked,
        onCreateWorkoutPlan = viewModel::createWorkoutPlans,
        onDateClicked = viewModel::getWorkoutPlanByDate
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutCalendarScreen(
    uiState: WorkoutPlanUiState,
    onBackPressed: () -> Unit,
    onWorkoutPlanClicked: (WorkoutPlanResponse) -> Unit,
    onDateClicked: (String) -> Unit,
    onCreateWorkoutPlan: (WorkoutPlan) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(selectedDate) {
        onDateClicked(
            selectedDate.format(
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
            )
        )
    }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FitnessTheme.color.black,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Workout Calendar",
                        color = FitnessTheme.color.primary,
                        style = FitnessTheme.typo.innerBoldSize20LineHeight28
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
                actions = {
                    IconButton(onClick = { showCreateDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Plan",
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
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CalendarHeader(
                selectedDate = selectedDate,
                onPreviousMonth = { selectedDate = selectedDate.minusMonths(1) },
                onNextMonth = { selectedDate = selectedDate.plusMonths(1) }
            )

            CalendarGrid(
                selectedDate = selectedDate,
                workoutPlans = uiState.workoutPlans.toImmutableList(),
                onDateSelected = { date ->
                    selectedDate = date
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            WorkoutPlansList(
                isLoading = uiState.isLoading,
                workoutPlans = uiState.workoutPlans.filter {
                    it.scheduledDate.toLocalDate() == selectedDate
                }.toImmutableList(),
                onWorkoutPlanClicked = onWorkoutPlanClicked
            )
        }

        if (showCreateDialog) {
            CreateWorkoutPlanDialog(
                onDismiss = { showCreateDialog = false },
                onCreatePlan = { plan ->
                    onCreateWorkoutPlan(plan)
                    showCreateDialog = false
                },
                trainingExercises = uiState.trainingExercises.toImmutableList(),
                selectedTime = selectedDate
            )
        }
    }
}

@Composable
private fun CalendarHeader(
    selectedDate: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Previous Month",
                tint = FitnessTheme.color.limeGreen
            )
        }

        Text(
            text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
            style = FitnessTheme.typo.innerBoldSize16LineHeight24,
            color = FitnessTheme.color.limeGreen
        )

        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Next Month",
                tint = FitnessTheme.color.limeGreen
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    selectedDate: LocalDate,
    workoutPlans: ImmutableList<WorkoutPlanResponse>,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysInMonth = selectedDate.lengthOfMonth()
    val firstDayOfMonth = selectedDate.withDayOfMonth(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(
                stringResource(R.string.sun),
                stringResource(R.string.mon),
                stringResource(R.string.tue), stringResource(R.string.wed),
                stringResource(R.string.thu), stringResource(R.string.fri),
                stringResource(R.string.sat)
            ).forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    style = FitnessTheme.typo.caption
                )
            }
        }

        // Calendar days
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {
            // Empty cells before first day of month
            items(firstDayOfWeek) {
                CalendarDay(
                    date = null,
                    isSelected = false,
                    hasWorkout = false,
                    onClick = {
                        /* */
                    }
                )
            }

            // Days of the month
            items(daysInMonth) { day ->
                val date = selectedDate.withDayOfMonth(day + 1)
                val hasWorkout = workoutPlans.any { it.scheduledDate.toLocalDate() == date }

                CalendarDay(
                    date = date,
                    isSelected = date == selectedDate,
                    hasWorkout = hasWorkout,
                    onClick = { onDateSelected(date) }
                )
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate?,
    isSelected: Boolean,
    hasWorkout: Boolean,
    onClick: (data: LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelectedColorAnimate by animateColorAsState(
        targetValue = when {
            isSelected -> FitnessTheme.color.limeGreen
            hasWorkout -> FitnessTheme.color.limeGreen.copy(alpha = 0.3f)
            else -> Color.Transparent
        },
        animationSpec = tween(durationMillis = 600),
        label = "Selected Color"
    )
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                isSelectedColorAnimate
            )
            .clickable(enabled = date != null, onClick = {
                onClick(date!!)
            }),
        contentAlignment = Alignment.Center
    ) {
        if (date != null) {
            Text(
                text = date.dayOfMonth.toString(),
                color = when {
                    isSelected -> FitnessTheme.color.black
                    else -> Color.White
                },
                style = FitnessTheme.typo.body
            )
        }
    }
}

@Composable
private fun WorkoutPlansList(
    isLoading: Boolean,
    workoutPlans: ImmutableList<WorkoutPlanResponse>,
    onWorkoutPlanClicked: (WorkoutPlanResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Workouts for ${
                if (workoutPlans.isEmpty()) "this day"
                else workoutPlans.first().scheduledDate.format(DateTimeFormatter.ofPattern("MMMM d"))
            }",
            style = FitnessTheme.typo.innerBoldSize16LineHeight24,
            color = FitnessTheme.color.limeGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = FitnessTheme.color.limeGreen
                    )
                }
            }

            workoutPlans.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No workouts scheduled",
                        style = FitnessTheme.typo.body,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(workoutPlans) { plan ->
                        WorkoutPlanCard(
                            workoutPlan = plan,
                            onWorkoutPlanClicked = onWorkoutPlanClicked
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutPlanCard(
    workoutPlan: WorkoutPlanResponse,
    onWorkoutPlanClicked: (WorkoutPlanResponse) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onWorkoutPlanClicked(workoutPlan)
            },
        colors = CardDefaults.cardColors(
            containerColor = FitnessTheme.color.background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = workoutPlan.name,
                    style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                    color = Color.Black
                )
                Text(
                    text = "${workoutPlan.trainingProgramExercise.exercise.name} • ${workoutPlan.trainingProgramExercise.duration}min",
                    style = FitnessTheme.typo.caption,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateWorkoutPlanDialog(
    trainingExercises: ImmutableList<TrainingProgramExerciseResponse>,
    onDismiss: () -> Unit,
    onCreatePlan: (WorkoutPlan) -> Unit,
    selectedTime: LocalDate = LocalDate.now(),
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("Beginner") }
    var duration by remember { mutableStateOf("30") }
    var selectedExercises by remember {
        mutableStateOf<List<TrainingProgramExerciseResponse>>(
            emptyList()
        )
    }
    var showExerciseSelection by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF282c34)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Create Workout Plan",
                    style = FitnessTheme.typo.innerBoldSize20LineHeight28,
                    color = FitnessTheme.color.limeGreen
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.plan_name), color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = FitnessTheme.color.limeGreen,
                        focusedLabelColor = FitnessTheme.color.limeGreen,
                        unfocusedLabelColor = Color.White,
                        errorBorderColor = Color.White,
                        disabledBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = FitnessTheme.color.limeGreen,
                        focusedLabelColor = FitnessTheme.color.limeGreen,
                        unfocusedLabelColor = Color.White,
                        errorBorderColor = Color.White,
                        disabledBorderColor = Color.White,
                        unfocusedBorderColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    var isExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = it },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = difficulty,
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text(
                                    stringResource(R.string.difficulty),
                                    color = Color.White
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.ArrowDropDown,
                                    null,
                                    tint = Color.White
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.White,
                                focusedBorderColor = FitnessTheme.color.limeGreen,
                                focusedLabelColor = FitnessTheme.color.limeGreen,
                                unfocusedLabelColor = Color.White,
                                errorBorderColor = Color.White,
                                disabledBorderColor = Color.White,
                                unfocusedBorderColor = Color.White
                            ),
                            modifier = Modifier.menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }
                        ) {
                            listOf("Beginner", "Intermediate", "Advanced").forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        difficulty = option
                                        isExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = {
                            Text(
                                stringResource(R.string.duration_min),
                                color = Color.White
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            focusedBorderColor = FitnessTheme.color.limeGreen,
                            focusedLabelColor = FitnessTheme.color.limeGreen,
                            unfocusedLabelColor = Color.White,
                            errorBorderColor = Color.White,
                            disabledBorderColor = Color.White,
                            unfocusedBorderColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Exercise Selection Section
                Text(
                    text = "Selected Exercises (${selectedExercises.size})",
                    style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                    color = Color.White
                )

                Button(
                    onClick = { showExerciseSelection = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FitnessTheme.color.limeGreen,
                        contentColor = FitnessTheme.color.black
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.select_exercises))
                }

                LazyColumn(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                ) {
                    items(selectedExercises) { exercise ->
                        ExerciseSelectionItem(
                            exercise = exercise,
                            onRemove = {
                                selectedExercises = selectedExercises.filter { it != exercise }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White,
                            containerColor = Color.Red
                        ),
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = 12.dp
                            ), text = "Cancel"
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            onCreatePlan(
                                WorkoutPlan(
                                    name = name,
                                    exercises = selectedExercises,
                                    scheduledDate = selectedTime,
                                )
                            )
                        },
                        enabled = name.isNotBlank() && selectedExercises.isNotEmpty(),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = FitnessTheme.color.limeGreen,
                            contentColor = FitnessTheme.color.black,
                            disabledContainerColor = FitnessTheme.color.primaryDisable
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = 12.dp
                            ), text = stringResource(R.string.create), color = Color.White
                        )
                    }
                }
            }
        }
    }

    if (showExerciseSelection) {
        ExerciseSelectionDialog(
            availableExercises = trainingExercises,
            selectedExercises = selectedExercises.toImmutableList(),
            onDismiss = { showExerciseSelection = false },
            onExercisesSelected = { exercises ->
                selectedExercises = exercises
                showExerciseSelection = false
            }
        )
    }
}

@Composable
private fun ExerciseSelectionItem(
    exercise: TrainingProgramExerciseResponse,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = FitnessTheme.color.background.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.exercise.name,
                    style = FitnessTheme.typo.body,
                    color = Color.White
                )
                Text(
                    text = "${exercise.sets} sets × ${exercise.reps} reps",
                    style = FitnessTheme.typo.caption,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove Exercise",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun ExerciseSelectionDialog(
    availableExercises: ImmutableList<TrainingProgramExerciseResponse>,
    selectedExercises: ImmutableList<TrainingProgramExerciseResponse>,
    onDismiss: () -> Unit,
    onExercisesSelected: (List<TrainingProgramExerciseResponse>) -> Unit
) {
    var tempSelectedExercises by remember { mutableStateOf(selectedExercises) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(selectedExercises) {
        tempSelectedExercises = selectedExercises
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF282c34)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.select_exercises),
                    style = FitnessTheme.typo.innerBoldSize20LineHeight28,
                    color = FitnessTheme.color.limeGreen
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = {
                        Text(
                            stringResource(R.string.search_exercises),
                            color = Color.White
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = FitnessTheme.color.limeGreen,
                        focusedLabelColor = FitnessTheme.color.limeGreen,
                        unfocusedLabelColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        disabledBorderColor = Color.White,
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    val filteredExercises = availableExercises.filter {
                        it.exercise.name.contains(searchQuery, ignoreCase = true)
                    }

                    items(filteredExercises) { exercise ->
                        val isSelected = tempSelectedExercises.contains(exercise)
                        ExerciseSelectionListItem(
                            exercise = exercise,
                            isSelected = isSelected,
                            onToggle = {
                                tempSelectedExercises = if (isSelected) {
                                    tempSelectedExercises.filter { it != exercise }
                                } else {
                                    tempSelectedExercises + exercise
                                }.toImmutableList()
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onExercisesSelected(tempSelectedExercises) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FitnessTheme.color.limeGreen,
                            contentColor = FitnessTheme.color.black
                        )
                    ) {
                        Text(stringResource(R.string.confirm, tempSelectedExercises.size))
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseSelectionListItem(
    exercise: TrainingProgramExerciseResponse,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(exercise.exercise.image.withUrl())
                .crossfade(true)
                .build(),
            contentDescription = "Exercise image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(FitnessTheme.color.background.copy(alpha = 0.5f)),
            error = rememberVectorPainter(Icons.Default.FitnessCenter),
            placeholder = rememberVectorPainter(Icons.Default.FitnessCenter)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
        ) {
            Text(
                text = exercise.exercise.name,
                style = FitnessTheme.typo.body,
                color = Color.White
            )
            Text(
                text = "${exercise.sets} sets × ${exercise.reps} reps",
                style = FitnessTheme.typo.caption,
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        Checkbox(
            checked = isSelected,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = FitnessTheme.color.limeGreen,
                uncheckedColor = Color.White
            )
        )
    }
}

@Preview
@Composable
private fun WorkoutCalendarScreenPreview() {
    val workoutPlans = List(3) {
        WorkoutPlanResponse(
            name = "Workout Plan $it",
            scheduledDate = "2024-11-19",
            trainingProgramExercise = TrainingProgramExerciseResponse(
                trainingProgramId = 1,
                exerciseId = 1,
                sets = 3,
                reps = 10,
                duration = 0,
                restTime = 0,
                order = 0,
                id = 1,
                exercise = ExerciseResponse(
                    id = 1,
                    name = "Exercise",
                    description = "Description",
                    videoUrl = "",
                    image = "",
                    muscleGroupId = 1
                )
            )

        )

    }

    WorkoutCalendarScreen(
        uiState = WorkoutPlanUiState(workoutPlans = workoutPlans.toImmutableList()),
        onBackPressed = {},
        onWorkoutPlanClicked = {},
        onCreateWorkoutPlan = {},
        onDateClicked = {}
    )
}

@Preview
@Composable
private fun PreviewCreateWorkoutPlanDialog() {
    val exercises = List(5) {
        TrainingProgramExerciseResponse(
            trainingProgramId = 1,
            exerciseId = 1,
            sets = 3,
            reps = 10,
            duration = 0,
            restTime = 0,
            order = 0,
            id = 1,
            exercise = ExerciseResponse(
                id = 1,
                name = "Exercise $it",
                description = "Description",
                videoUrl = "",
                image = "",
                muscleGroupId = 1
            )
        )
    }

    CreateWorkoutPlanDialog(
        trainingExercises = exercises.toImmutableList(),
        onDismiss = {},
        onCreatePlan = {}
    )
}

@Preview
@Composable
private fun PreviewExerciseSelectionDialog() {
    val exercises = List(5) {
        TrainingProgramExerciseResponse(
            trainingProgramId = 1,
            exerciseId = 1,
            sets = 3,
            reps = 10,
            duration = 0,
            restTime = 0,
            order = 0,
            id = 1,
            exercise = ExerciseResponse(
                id = 1,
                name = "Exercise $it",
                description = "Description",
                videoUrl = "",
                image = "",
                muscleGroupId = 1
            )
        )
    }

    ExerciseSelectionDialog(
        availableExercises = exercises.toImmutableList(),
        selectedExercises = persistentListOf(),
        onDismiss = {},
        onExercisesSelected = {}
    )
}