package ptit.vietpq.fitnessapp.presentation.setup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.extension.toast
import ptit.vietpq.fitnessapp.presentation.setup.component.AgePicker
import ptit.vietpq.fitnessapp.presentation.setup.component.GenderSelection
import ptit.vietpq.fitnessapp.presentation.setup.component.HeightPicker
import ptit.vietpq.fitnessapp.presentation.setup.component.WeightPicker

@Composable
fun SetupRoute(
    onSetupComplete: () -> Unit,
    viewModel: SetupViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.eventFlow.collectAsStateWithLifecycle(initialValue = SetupState.Idle)
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
    SetupScreen(
        isLoading = isLoading,
        onUpdateUserInfo = { age, weight, height, isMale ->
            viewModel.updateUser(age, weight, height, if (isMale) "Male" else "Female", "")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    isLoading: Boolean,
    onUpdateUserInfo: (
        age: Int,
        weight: Int,
        height: Int,
        isMale: Boolean,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    var step by remember { mutableIntStateOf(0) }
    var age by remember { mutableIntStateOf(18) }
    var weight by remember { mutableIntStateOf(60) }
    var height by remember { mutableIntStateOf(170) }
    var isMale by remember { mutableStateOf(true) }

    BackHandler {
        if (step > 0) {
            step--
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = FitnessTheme.color.black,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Row(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Image(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            painter = painterResource(id = R.drawable.ic_back_arrow),
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.back),
                            color = FitnessTheme.color.limeGreen,
                            style = FitnessTheme.typo.innerBoldSize16LineHeight24
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = FitnessTheme.color.black,
                    actionIconContentColor = FitnessTheme.color.limeGreen,
                    scrolledContainerColor = FitnessTheme.color.black,
                    titleContentColor = FitnessTheme.color.limeGreen,
                    navigationIconContentColor = FitnessTheme.color.limeGreen
                )
            )
        },
        content = { innerPadding ->
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = FitnessTheme.color.limeGreen
                    )
                }
            }
            when (step) {
                0 -> WeightPicker(
                    innerPadding = innerPadding,
                    onContinueClick = {
                        step = 1
                        weight = it
                    }
                )

                1 -> HeightPicker(
                    innerPadding = innerPadding,
                    onContinueClick = {
                        step = 2
                        height = it
                    }
                )

                2 -> AgePicker(
                    innerPadding = innerPadding,
                    onContinueClick = {
                        step = 3
                        age = it
                    }
                )

                3 -> GenderSelection(
                    innerPadding = innerPadding,
                    onContinueClick = {
                        isMale = it
                        onUpdateUserInfo(age, weight, height, isMale)
                    }
                )
            }
        }
    )
}