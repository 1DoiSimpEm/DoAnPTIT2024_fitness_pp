package ptit.vietpq.fitnessapp.presentation.main.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ptit.vietpq.fitnessapp.presentation.exercise.exerciseGraph
import ptit.vietpq.fitnessapp.presentation.exercise_category.exerciseCategoryGraph
import ptit.vietpq.fitnessapp.presentation.exercise_detail.exerciseDetailRoute
import ptit.vietpq.fitnessapp.presentation.home.HomeDestination
import ptit.vietpq.fitnessapp.presentation.home.homeGraph
import ptit.vietpq.fitnessapp.presentation.login.loginGraph
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination
import ptit.vietpq.fitnessapp.presentation.meal_detailed.MealDetailedDestination
import ptit.vietpq.fitnessapp.presentation.meal_detailed.mealDetailedGraph
import ptit.vietpq.fitnessapp.presentation.meal_planning.mealPlanningRoute
import ptit.vietpq.fitnessapp.presentation.meal_plans.MealListDestination
import ptit.vietpq.fitnessapp.presentation.meal_plans.mealListGraph
import ptit.vietpq.fitnessapp.presentation.profile.ProfileDestination
import ptit.vietpq.fitnessapp.presentation.profile.profileGraph
import ptit.vietpq.fitnessapp.presentation.setup.SetupDestination
import ptit.vietpq.fitnessapp.presentation.setup.setupGraph

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
            }
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
            }
        )
        profileGraph(
            onBackPressed = onBackPressed,
            onMealListNavigate = {
                onNavigateToDestination(
                    MealListDestination,
                    MealListDestination.route
                )
            }
        )

        exerciseGraph()

        mealPlanningRoute(
            onMealDetailedNavigating = { mealContent ->
                onNavigateToDestination(
                    MealDetailedDestination,
                    MealDetailedDestination.createNavigationRoute(mealContent)
                )
            },
            onBackPressed = onBackPressed
        )

        exerciseDetailRoute(
            onBackPressed = onBackPressed
        )
        mealDetailedGraph(
            onBackPressed = onBackPressed,

        )
        mealListGraph(
            onBackPressed = onBackPressed,
            onMealPlanClick = { meal ->
                onNavigateToDestination(
                    MealDetailedDestination,
                    MealDetailedDestination.createNavigationRoute(meal.description)
                )
            }
        )
        exerciseCategoryGraph(
            onBackPressed = onBackPressed,
            onExerciseClicked = { exercise ->
                navController.navigate(exercise)
            }
        )
    }
}
