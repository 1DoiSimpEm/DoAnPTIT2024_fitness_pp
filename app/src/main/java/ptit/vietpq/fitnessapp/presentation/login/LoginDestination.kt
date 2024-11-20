package ptit.vietpq.fitnessapp.presentation.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination

data object LoginDestination : FitnessNavigationDestination {
  override val route: String = "login_route"
  override val destination: String = "login_destination"
}

fun NavGraphBuilder.loginGraph(
  onLoginSuccess : () -> Unit,
  onRegisterClick: () -> Unit
) =
  composable(route = LoginDestination.route) {
    LoginRoute(onLoginSuccess = onLoginSuccess, onRegisterClick = onRegisterClick)
  }