package ptit.vietpq.fitnessapp.presentation.main.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ptit.vietpq.fitnessapp.data.remote.response.toModel
import ptit.vietpq.fitnessapp.presentation.exercise_guidance.exerciseGraph
import ptit.vietpq.fitnessapp.presentation.exercise_category.ExerciseCategoryDestination
import ptit.vietpq.fitnessapp.presentation.exercise_category.exerciseCategoryGraph
import ptit.vietpq.fitnessapp.presentation.exercise_detail.exerciseDetailRoute
import ptit.vietpq.fitnessapp.presentation.exercise_guidance.ExerciseDestination
import ptit.vietpq.fitnessapp.presentation.home.HomeDestination
import ptit.vietpq.fitnessapp.presentation.home.homeGraph
import ptit.vietpq.fitnessapp.presentation.login.LoginDestination
import ptit.vietpq.fitnessapp.presentation.login.loginGraph
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination
import ptit.vietpq.fitnessapp.presentation.meal_detailed.MealDetailedUiState
import ptit.vietpq.fitnessapp.presentation.meal_detailed.mealDetailedGraph
import ptit.vietpq.fitnessapp.presentation.meal_planning.MealPlanningDestination
import ptit.vietpq.fitnessapp.presentation.meal_planning.mealPlanningRoute
import ptit.vietpq.fitnessapp.presentation.meal_plans.MealListDestination
import ptit.vietpq.fitnessapp.presentation.meal_plans.mealListGraph
import ptit.vietpq.fitnessapp.presentation.notification_setting.NotificationSettingsDestination
import ptit.vietpq.fitnessapp.presentation.notification_setting.notificationSettingsGraph
import ptit.vietpq.fitnessapp.presentation.password_setting.PasswordSettingDestination
import ptit.vietpq.fitnessapp.presentation.password_setting.passwordSettingGraph
import ptit.vietpq.fitnessapp.presentation.profile.ProfileDestination
import ptit.vietpq.fitnessapp.presentation.profile.profileGraph
import ptit.vietpq.fitnessapp.presentation.progress.ProgressDestination
import ptit.vietpq.fitnessapp.presentation.progress.progressGraph
import ptit.vietpq.fitnessapp.presentation.register.RegisterDestination
import ptit.vietpq.fitnessapp.presentation.register.registerGraph
import ptit.vietpq.fitnessapp.presentation.setting.SettingDestination
import ptit.vietpq.fitnessapp.presentation.setting.settingGraph
import ptit.vietpq.fitnessapp.presentation.setup.SetupDestination
import ptit.vietpq.fitnessapp.presentation.setup.setupGraph
import ptit.vietpq.fitnessapp.presentation.training_program.TrainingProgramDestination
import ptit.vietpq.fitnessapp.presentation.training_program.trainingProgramGraph
import ptit.vietpq.fitnessapp.presentation.training_program_exercise.trainingProgramExerciseRoute
import ptit.vietpq.fitnessapp.presentation.training_program_exercise_detail.trainingProgramExerciseDetailRoute
import ptit.vietpq.fitnessapp.presentation.workout_plan.WorkoutPlanDestination
import ptit.vietpq.fitnessapp.presentation.workout_plan.workoutCalendarGraph

@Composable
fun FitnessNavHost(
    navController: NavHostController,
    startDestination: FitnessNavigationDestination,
    onNavigateToDestination: (FitnessNavigationDestination, String) -> Unit,
    onNavigateToDestinationPopUpTo: (FitnessNavigationDestination, String) -> Unit,
    onNavigateToDestinationPopUpToSplash: (FitnessNavigationDestination) -> Unit,
    onBackPressed: () -> Unit,
    onShowMessage: (String) -> Unit,
    onSetSystemBarsColorTransparent: () -> Unit,
    onResetSystemBarsColor: () -> Unit,
    showBottomBar: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(700)
            )
        },
    ) {

        loginGraph(
            onLoginSuccess = {
                onNavigateToDestinationPopUpTo(
                    SetupDestination,
                    SetupDestination.route
                )
            },
            onRegisterClick = {
                onNavigateToDestination(
                    RegisterDestination,
                    RegisterDestination.route
                )
            }
        )

        registerGraph(
            onBackPressed = onBackPressed
        )

        setupGraph(
            onSetupCompleted = {
                onNavigateToDestinationPopUpTo(
                    HomeDestination,
                    HomeDestination.route
                )
            }
        )
        homeGraph(
            onUserClicked = {
                onNavigateToDestination(
                    ProfileDestination,
                    ProfileDestination.route
                )
            },
            onWorkoutClicked = {
                onNavigateToDestination(
                    ExerciseCategoryDestination,
                    ExerciseCategoryDestination.route
                )
            },
            onProgressClicked = {
                onNavigateToDestination(
                    ProgressDestination,
                    ProgressDestination.route
                )
            },
            onSettingsClicked = {
                onNavigateToDestination(
                    SettingDestination,
                    SettingDestination.route
                )
            },
            onNutritionClicked = {
                onNavigateToDestination(
                    MealListDestination,
                    MealListDestination.route
                )
            },
            onSeeAllClicked = {
                onNavigateToDestination(
                    TrainingProgramDestination,
                    TrainingProgramDestination.route
                )
            },
            onCalendarClicked = {
                onNavigateToDestination(
                    WorkoutPlanDestination,
                    WorkoutPlanDestination.route
                )
            },
            onRecommendedItemClicked = navController::navigate
        )
        profileGraph(
            onBackPressed = onBackPressed,
            onLoginNavigate = {
                onNavigateToDestinationPopUpTo(
                    LoginDestination,
                    LoginDestination.route
                )
            },
            onSettingNavigate = {
                onNavigateToDestination(
                    SettingDestination,
                    SettingDestination.route
                )
            }
        )

        progressGraph(
            onBackPressed = onBackPressed
        )

        workoutCalendarGraph(
            onBackPressed = onBackPressed,
            onWorkoutPlanClicked = { workoutPlan ->
                navController.navigate(workoutPlan.trainingProgramExercise.toModel())
            }
        )

        exerciseGraph()

        mealPlanningRoute(
            onMealDetailedNavigating = { mealContent ->
                navController.navigate(MealDetailedUiState(mealContent))
            },
            onBackPressed = onBackPressed
        )

        exerciseDetailRoute(
            onBackPressed = onBackPressed,
            onPoseDetectionClick = {
                onNavigateToDestination(
                    ExerciseDestination,
                    ExerciseDestination.route
                )
            }
        )
        mealDetailedGraph(
            onBackPressed = onBackPressed,
        )
        mealListGraph(
            onBackPressed = onBackPressed,
            onMealPlanClick = { meal ->
                navController.navigate(MealDetailedUiState(meal.description))
            },
            onMealAddClicked = {
                onNavigateToDestination(
                    MealPlanningDestination,
                    MealPlanningDestination.route
                )
            }
        )
        exerciseCategoryGraph(
            onBackPressed = onBackPressed,
            onExerciseClicked = navController::navigate
        )

        trainingProgramGraph(
            onBackPressed = onBackPressed,
            onProgramClicked = navController::navigate
        )

        trainingProgramExerciseRoute(
            onBackPressed = onBackPressed,
            onExerciseSelected = { exercise ->
                navController.navigate(exercise.toModel())
            }
        )

        trainingProgramExerciseDetailRoute(
            onBackPressed = onBackPressed
        )

        settingGraph(
            onBackPressed = onBackPressed,
            onNotificationSettingClicked = {
                onNavigateToDestination(
                    NotificationSettingsDestination,
                    NotificationSettingsDestination.route
                )
            },
            onPasswordSettingClicked = {
                onNavigateToDestination(
                    PasswordSettingDestination,
                    PasswordSettingDestination.route
                )
            },
        )

        passwordSettingGraph(
            onBackPressed = onBackPressed,
        )

        notificationSettingsGraph(
            onBackPressed = onBackPressed
        )
    }
}
