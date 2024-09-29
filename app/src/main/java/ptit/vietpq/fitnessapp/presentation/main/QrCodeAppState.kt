package ptit.vietpq.fitnessapp.presentation.main

import android.annotation.SuppressLint
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ptit.vietpq.fitnessapp.presentation.main.navigation.TopLevelDestination
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ptit.vietpq.fitnessapp.presentation.login.LoginDestination

@Composable
fun rememberQrAppState(
  startDestination: FitnessNavigationDestination = LoginDestination,
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  coroutineScope: CoroutineScope = rememberCoroutineScope(),
  navController: NavHostController = rememberNavController(),
) = remember(
  snackbarHostState,
  coroutineScope,
  navController,
  startDestination,
) {
  QrCodeAppState(
    snackbarHostState = snackbarHostState,
    coroutineScope = coroutineScope,
    navController = navController,
    startDestination = startDestination,
  )
}

@Stable
class QrCodeAppState(
  val snackbarHostState: SnackbarHostState,
  val coroutineScope: CoroutineScope,
  val navController: NavHostController,
  val startDestination: FitnessNavigationDestination,
) {

  private val currentDestination: NavDestination?
    @Composable get() = navController.currentBackStackEntryAsState().value?.destination

  val currentTopLevelDestination: FitnessNavigationDestination
    @Composable get() {
      topLevelDestinations.firstOrNull { it.route == currentDestination?.route }
        ?.let { _currentTopLevelDestination = it }
      return _currentTopLevelDestination
    }

  val shouldShowBottomBar: Boolean
    @Composable get() = currentDestination?.route == currentTopLevelDestination.route

  var shouldShowBottomSheetDialog by mutableStateOf(false)
    private set

  fun setShowBottomSheetDialog(shouldShow: Boolean) {
    shouldShowBottomSheetDialog = shouldShow
  }

  /**
   * Top level destinations to be used in the BottomBar.
   */
  val topLevelDestinations = TopLevelDestination.entries.toPersistentList()

  private var _currentTopLevelDestination by mutableStateOf(startDestination)

  private val snackbarMessages = MutableStateFlow<List<String>>(emptyList())

  /**
   * UI logic for navigating to a particular destination in the app. The NavigationOptions to
   * navigate with are based on the type of destination, which could be a top level destination or
   * just a regular destination.
   *
   * Top level destinations have only one copy of the destination of the back stack, and save and
   * restore state whenever you navigate to and from it.
   * Regular destinations can have multiple copies in the back stack and state isn't saved nor
   * restored.
   *
   * @param destination The [FitnessNavigationDestination] the app needs to navigate to.
   * @param route Optional route to navigate to in case the destination contains arguments.
   */
  @SuppressLint("RestrictedApi")
  fun navigate(destination: FitnessNavigationDestination, route: String? = null) = with(navController) {
    if (destination is TopLevelDestination) {
      navigate(route ?: destination.route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items.
//        popUpTo(ScanDestination.route) {
//          saveState = true
//        }
        // Avoid multiple copies of the same destination when
        // re-selecting the same item.
        launchSingleTop = true
        // Restore state when re-selecting a previously selected item.
        restoreState = true
      }
//      Timber.tag("QrCodeAppState").d("navigateTopLevel: ${graph.findStartDestination().id} and graphId ${graph.id} -- startId ${graph.startDestDisplayName} $destination and $route")
    } else {
//      Timber.tag("QrCodeAppState").d("navigate: $destination and $route")
      // Navigate to the destination without rooting the back stack
      navigate(route ?: destination.route)
    }
  }

  fun navigateWithPopUpTo(destination: FitnessNavigationDestination, route: String) {
    navController.navigate(route) {
      popUpTo(route) {
        inclusive = true
        saveState = false
      }
    }
  }

  fun navigateWithPopUpToRoute(
    destination: FitnessNavigationDestination,
//    clearRoute: String
//    = SplashDestination.route
  ) {
    // Destination is a top level destination
//    navController.navigate(ScanDestination.route) {
//      // Clear Route Click for this
//      popUpTo(SplashDestination.route) {
//        inclusive = true
//      }
//    }
  }

  fun onBackClick() = navController.popBackStack()

  fun showMessage(message: String) = snackbarMessages.update { it + message }
}
