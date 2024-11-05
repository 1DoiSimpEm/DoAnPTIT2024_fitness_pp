package ptit.vietpq.fitnessapp.presentation.main.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ptit.vietpq.fitnessapp.presentation.exercise.exerciseGraph
import ptit.vietpq.fitnessapp.presentation.home.HomeDestination
import ptit.vietpq.fitnessapp.presentation.home.homeGraph
import ptit.vietpq.fitnessapp.presentation.login.loginGraph
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination
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
    onBackClick: () -> Unit,
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
            onBackPressed = onBackClick
        )

        exerciseGraph()
    }
}