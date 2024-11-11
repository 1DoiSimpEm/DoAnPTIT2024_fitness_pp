package ptit.vietpq.fitnessapp.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import kotlinx.coroutines.delay
import ptit.vietpq.fitnessapp.R

@Composable
fun HomeFeature(
    onFeatureSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedPosition by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(selectedPosition) {
        delay(1000L)
        selectedPosition = 0
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        HomeItem(
            text = stringResource(R.string.workout),
            image = R.drawable.ic_dumbell,
            isSelected = selectedPosition == 0,
            onItemSelected = {
                selectedPosition = 0
                onFeatureSelected(0)
            }
        )
        Spacer(modifier = Modifier.size(8.dp))
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(24.dp)
                .align(Alignment.CenterVertically)
                .background(
                    color = FitnessTheme.color.primary
                )
        )
        Spacer(modifier = Modifier.size(8.dp))
        HomeItem(
            text = stringResource(R.string.progress_tracking),
            image = R.drawable.ic_progress,
            isSelected = selectedPosition == 1,
            onItemSelected = {
                selectedPosition = 1
                onFeatureSelected(1)
            }
        )
        Spacer(modifier = Modifier.size(8.dp))
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(24.dp)
                .align(Alignment.CenterVertically)
                .background(
                    color = FitnessTheme.color.primary
                )
        )
        Spacer(modifier = Modifier.size(8.dp))
        HomeItem(
            text = stringResource(R.string.nutrition),
            image = R.drawable.ic_apple,
            isSelected = selectedPosition == 2,
            onItemSelected = {
                selectedPosition = 2
                onFeatureSelected(2)
            }
        )
        Spacer(modifier = Modifier.size(8.dp))
        Box(
            modifier = Modifier
                .width(1.dp)
                .height(24.dp)
                .align(Alignment.CenterVertically)
                .background(
                    color = FitnessTheme.color.primary
                )
        )
        Spacer(modifier = Modifier.size(8.dp))
        HomeItem(
            text = stringResource(R.string.community),
            image = R.drawable.ic_community,
            isSelected = selectedPosition == 3,
            onItemSelected = {
                selectedPosition = 3
                onFeatureSelected(3)
            }
        )
        Spacer(modifier = Modifier.size(8.dp))

    }
}

@Preview
@Composable
private fun PreviewHomeFeature() {
    HomeFeature({})
}