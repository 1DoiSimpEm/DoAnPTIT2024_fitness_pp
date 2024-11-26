package ptit.vietpq.fitnessapp.presentation.getting_started

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme

@Composable
fun GettingStartedRoute(
    onBackPressed: () -> Unit,
    onNextScreen: () -> Unit,
) {

    GettingStartedScreen(
        onNextScreen = onNextScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GettingStartedScreen(
    onNextScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.background(FitnessTheme.color.background),
        contentColor = FitnessTheme.color.primary,
        containerColor = FitnessTheme.color.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Main Content Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(240.dp)
                        .padding(vertical = 24.dp),
                    contentDescription = null,
                    imageVector = Icons.Default.FitnessCenter,
                )

                // Title
                Text(
                    text = stringResource(R.string.welcome_to_fitness_app),
                    style = FitnessTheme.typo.heading,
                    color = FitnessTheme.color.white,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Description
                Text(
                    text = stringResource(R.string.time_to_get_fit_and_healthy_let_s_start_by_setting_up_your_profile),
                    style = FitnessTheme.typo.body,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            // Continue Button
            Button(
                onClick = onNextScreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FitnessTheme.color.limeGreen,
                    contentColor = FitnessTheme.color.black
                )
            ) {
                Text(
                    text = stringResource(id = R.string.continue_),
                    style = FitnessTheme.typo.button
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewGettingStarted() {
    GettingStartedScreen(
        onNextScreen = {}
    )
}