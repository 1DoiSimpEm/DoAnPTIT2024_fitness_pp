package ptit.vietpq.fitnessapp.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import kotlinx.collections.immutable.ImmutableList
import ptit.vietpq.fitnessapp.presentation.main.navigation.FitnessNavHost
import ptit.vietpq.fitnessapp.presentation.main.navigation.TopLevelDestination
import ptit.vietpq.fitnessapp.presentation.main.navigation.destination.FitnessNavigationDestination
import ptit.vietpq.fitnessapp.ui.theme.FitnessAppTheme

@Composable
fun FitnessApp(
  startDestination: FitnessNavigationDestination,
  modifier: Modifier = Modifier,
  appState: QrCodeAppState = rememberQrAppState(),
  ) {

  FitnessAppTheme {
    Scaffold(
      modifier = modifier,
      bottomBar = {
        AnimatedVisibility(
          visible = appState.shouldShowBottomBar,
          enter = EnterTransition.None,
          exit = ExitTransition.None,
        ) {
//          Column {
//            FitnessBottomBar(
//              destinations = appState.topLevelDestinations,
//              currentDestination = appState.currentTopLevelDestination,
//              onNavigateToDestination = {
//                appState.navigate(it)
//              },
//            )
//            Box(
//              modifier = remember {
//                Modifier
//                  .navigationBarsPadding()
//              },
//            )
//          }
        }
      },
      snackbarHost = {
      },
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .padding(paddingValues = innerPadding)
          .consumeWindowInsets(paddingValues = innerPadding),
      ) {
        FitnessNavHost(
          navController = appState.navController,
          startDestination = startDestination,
          onNavigateToDestination = appState::navigate,
          onNavigateToDestinationPopUpTo = appState::navigateWithPopUpTo,
          onNavigateToDestinationPopUpToSplash = appState::navigateWithPopUpToRoute,
          onBackPressed = appState::onBackClick,
          onShowMessage = { message -> appState.showMessage(message) },
          onSetSystemBarsColorTransparent = { },
          onResetSystemBarsColor = { },
          showBottomBar = appState::setShowBottomSheetDialog,
        )
      }
    }
  }
}

@Composable
private fun FitnessBottomBar(
  destinations: ImmutableList<TopLevelDestination>,
  currentDestination: TopLevelDestination,
  onNavigateToDestination: (TopLevelDestination) -> Unit,
  modifier: Modifier = Modifier,
  color: Color = FitnessTheme.color.background,
) {
  BottomNavigation(
    modifier = modifier,
    backgroundColor = color,
  ) {
    destinations.forEach { destination ->
      val selected = destination == currentDestination

      val icon = painterResource(id = if (!selected) destination.iconResourceId else destination.iconResourceSelect)
      val text = stringResource(id = destination.textResourceId)

      val tint by animateColorAsState(
        if (selected) {
          FitnessTheme.color.primary
        } else {
          Color.White
        },
        label = text,
      )
      BottomNavigationItem(
        icon = {
          Image(
            painter = icon,
            contentDescription = text,
          )
        },
        label = {
          Text(
            text = text,
            color = tint,
            maxLines = 1,
            style = FitnessTheme.typo.innerBoldSize12Line16,
          )
        },
        selected = selected,
        onClick = { onNavigateToDestination(destination) },
      )
    }
  }
}

private val BottomBarEnterTransition = fadeIn() + expandVertically(expandFrom = Alignment.Top)
private val BottomBarExitTransition = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
