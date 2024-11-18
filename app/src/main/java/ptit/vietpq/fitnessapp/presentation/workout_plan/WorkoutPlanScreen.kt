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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse
import ptit.vietpq.fitnessapp.data.remote.response.TrainingProgramExerciseResponse
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.domain.model.WorkoutPlan
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutCalendarScreen(
    uiState: WorkoutPlanUiState,
    onBackPressed: () -> Unit,
    onWorkoutPlanClicked: (Long) -> Unit,
    onCreateWorkoutPlan: (WorkoutPlan) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showCreateDialog by remember { mutableStateOf(false) }

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
                workoutPlans = uiState.workoutPlans.filter {
                    it.scheduledDate == selectedDate
                }.toImmutableList()
            )
        }

        if (showCreateDialog) {
            CreateWorkoutPlanDialog(
                onDismiss = { showCreateDialog = false },
                onCreatePlan = { plan ->
                    onCreateWorkoutPlan(plan)
                    showCreateDialog = false
                }
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
    workoutPlans: ImmutableList<WorkoutPlan>,
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
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
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
                val hasWorkout = workoutPlans.any { it.scheduledDate == date }

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
    workoutPlans: ImmutableList<WorkoutPlan>,
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

        if (workoutPlans.isEmpty()) {
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
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(workoutPlans) { plan ->
                    WorkoutPlanCard(workoutPlan = plan)
                }
            }
        }
    }
}

@Composable
private fun WorkoutPlanCard(
    workoutPlan: WorkoutPlan,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                    color = FitnessTheme.color.limeGreen
                )
                Text(
                    text = "${workoutPlan.exercises.size} exercises • ${workoutPlan.estimatedDuration} min",
                    style = FitnessTheme.typo.caption,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            Badge(
                containerColor = FitnessTheme.color.limeGreen,
                contentColor = FitnessTheme.color.black
            ) {
                Text(workoutPlan.difficulty)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateWorkoutPlanDialog(
    onDismiss: () -> Unit,
    onCreatePlan: (WorkoutPlan) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("Beginner") }
    var duration by remember { mutableStateOf("30") }
    var exercises by remember { mutableStateOf<List<TrainingProgramExerciseResponse>>(emptyList()) }
    var showAddExercise by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = FitnessTheme.color.background
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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
                    label = { Text("Plan Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = FitnessTheme.color.limeGreen,
                        focusedLabelColor = FitnessTheme.color.limeGreen,
                        unfocusedLabelColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = FitnessTheme.color.limeGreen,
                        focusedLabelColor = FitnessTheme.color.limeGreen,
                        unfocusedLabelColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = {},
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = difficulty,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Difficulty") },
                            trailingIcon = { Icon(Icons.Filled.ArrowDropDown, null) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.White,
                                focusedBorderColor = FitnessTheme.color.limeGreen,
                                focusedLabelColor = FitnessTheme.color.limeGreen,
                                unfocusedLabelColor = Color.White
                            )
                        )
                        // Dropdown menu content here
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Duration (min)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.White,
                            focusedBorderColor = FitnessTheme.color.limeGreen,
                            focusedLabelColor = FitnessTheme.color.limeGreen,
                            unfocusedLabelColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Exercises (${exercises.size})",
                        style = FitnessTheme.typo.body,
                        color = Color.White
                    )
                    IconButton(onClick = { showAddExercise = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Exercise",
                            tint = FitnessTheme.color.limeGreen
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.height(200.dp)
                ) {
                    items(exercises) { exercise ->
                        ExerciseItem(exercise = exercise)
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
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onCreatePlan(
                                WorkoutPlan(
                                    name = name,
                                    description = description,
                                    difficulty = difficulty,
                                    estimatedDuration = duration.toIntOrNull() ?: 30,
                                    exercises = exercises,
                                    scheduledDate = LocalDate.now()
                                )
                            )
                        },
                        enabled = name.isNotBlank() && exercises.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FitnessTheme.color.limeGreen,
                            contentColor = FitnessTheme.color.black
                        )
                    ) {
                        Text("Create")
                    }
                }
            }
        }
    }

//    if (showAddExercise) {
//        AddExerciseDialog(
//            onDismiss = { showAddExercise = false },
//            onAddExercise = { exercise ->
//                exercises = exercises + exercise.copy(order = exercises.size)
//                showAddExercise = false
//            }
//        )
//    }
}

@Composable
private fun ExerciseItem(
    exercise: TrainingProgramExerciseResponse,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = exercise.exercise.name,
            style = FitnessTheme.typo.body,
            color = Color.White
        )
        Text(
            text = "${exercise.sets}×${exercise.reps}",
            style = FitnessTheme.typo.body,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Preview
@Composable
private fun WorkoutCalendarScreenPreview() {
    val workoutPlans = List(3) {
        WorkoutPlan(
            name = "Workout Plan $it",
            description = "Description",
            scheduledDate = LocalDate.now(),
            difficulty = "Beginner",
            estimatedDuration = 30,
            exercises = persistentListOf(
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
                        name = "Exercise",
                        description = "Description",
                        videoUrl = "",
                        image = "",
                        muscleGroupId = 1
                    )
                )
            )
        )
    }

    WorkoutCalendarScreen(
        uiState = WorkoutPlanUiState(workoutPlans = workoutPlans.toImmutableList()),
        onBackPressed = {},
        onWorkoutPlanClicked = {},
        onCreateWorkoutPlan = {}
    )
}