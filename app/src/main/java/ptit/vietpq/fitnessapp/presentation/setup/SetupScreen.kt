package ptit.vietpq.fitnessapp.presentation.setup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.extension.toast
import ptit.vietpq.fitnessapp.presentation.setup.component.AgePicker
import ptit.vietpq.fitnessapp.presentation.setup.component.GenderSelection
import ptit.vietpq.fitnessapp.presentation.setup.component.HeightPicker
import ptit.vietpq.fitnessapp.presentation.setup.component.WeightPicker
import ptit.vietpq.fitnessapp.ui.common.LoadingDialog

@Composable
fun SetupRoute(
    onSetupComplete: () -> Unit,
    viewModel: SetupViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.eventFlow.collectAsStateWithLifecycle(initialValue = SetupState.Idle)
    val screenState by viewModel.state.collectAsStateWithLifecycle()
    var isLoading by remember {
        mutableStateOf(false)
    }
    when (state) {
        SetupState.Error -> {
            context.toast("Error")
        }

        SetupState.Loading -> {
            isLoading = true
        }

        SetupState.Success -> {
            context.toast("Success")
            onSetupComplete()
        }

        SetupState.Idle -> Unit
    }
    LoadingDialog(isLoading)
    SetupScreen(
        state = screenState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun SetupScreen(
    state: SetupScreenState,
    onEvent: (SetupScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onEvent(SetupScreenEvent.OnBackPressed)
    }

    LoadingDialog(isLoading = state.isLoading)

    Scaffold(
        modifier = modifier,
        containerColor = FitnessTheme.color.black,
        topBar = {
            SetupTopBar(
                onBackClick = { onEvent(SetupScreenEvent.OnBackPressed) },
                showBack = state.currentStep != SetupStep.Weight
            )
        }
    ) { innerPadding ->
        Column {
            when (state.currentStep) {
                SetupStep.Weight -> WeightPicker(
                    innerPadding = innerPadding,
                    initialWeight = state.weight,
                    onContinueClick = { weight ->
                        onEvent(SetupScreenEvent.OnWeightSelected(weight))
                    }
                )
                SetupStep.Height -> HeightPicker(
                    innerPadding = innerPadding,
                    initialHeight = state.height,
                    onContinueClick = { height ->
                        onEvent(SetupScreenEvent.OnHeightSelected(height))
                    }
                )
                SetupStep.Age -> AgePicker(
                    innerPadding = innerPadding,
                    initialAge = state.age,
                    onContinueClick = { age ->
                        onEvent(SetupScreenEvent.OnAgeSelected(age))
                    }
                )
                SetupStep.Gender -> GenderSelection(
                    innerPadding = innerPadding,
                    isMale = state.isMale,
                    onContinueClick = { isMale ->
                        onEvent(SetupScreenEvent.OnGenderSelected(isMale))
                    }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetupTopBar(
    onBackClick: () -> Unit,
    showBack: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            if (showBack) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable(
                            onClick = onBackClick,
                            role = Role.Button
                        ),
                ) {
                    Image(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = "Back",
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.back),
                        color = FitnessTheme.color.limeGreen,
                        style = FitnessTheme.typo.innerBoldSize16LineHeight24
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = FitnessTheme.color.black,
            navigationIconContentColor = FitnessTheme.color.limeGreen,
        ),
        modifier = modifier
    )
}


@Preview
@Composable
fun SetupScreenPreview() {
    SetupScreen(
        state = SetupScreenState(),
        onEvent = {}
    )
}