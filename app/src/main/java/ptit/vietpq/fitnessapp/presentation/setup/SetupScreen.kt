package ptit.vietpq.fitnessapp.presentation.setup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.presentation.setup.component.AgePicker
import ptit.vietpq.fitnessapp.presentation.setup.component.GenderSelection
import ptit.vietpq.fitnessapp.presentation.setup.component.HeightPicker
import ptit.vietpq.fitnessapp.presentation.setup.component.WeightPicker

@Composable
fun SetupRoute() {
    SetupScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
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
                        step = 4
                        isMale = it
                    }
                )


            }
        }
    )
}


@Preview
@Composable
private fun PreviewSetupScreen() {
    SetupScreen()
}