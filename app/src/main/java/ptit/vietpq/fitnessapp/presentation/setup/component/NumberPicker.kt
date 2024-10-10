package ptit.vietpq.fitnessapp.presentation.setup.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.NumberPicker
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R


@SuppressLint("StringFormatMatches")
@Composable
fun AgePicker(
    innerPadding: PaddingValues,
    onContinueClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var age by remember {
        mutableStateOf(18)
    }
    Column(
        modifier = modifier
            .padding(
                top = innerPadding.calculateTopPadding(),
            )
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.how_old_are_you),
            style = FitnessTheme.typo.h2,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(R.string.your_age_will_be_used_to_calculate_your_daily_calorie_needs),
            textAlign = TextAlign.Center,
            color = Color.White,
            style = FitnessTheme.typo.body3
        )
        Spacer(modifier = Modifier.height(36.dp))
        NumberPicker(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(
                    color = FitnessTheme.color.primary,
                    shape = RoundedCornerShape(8.dp)
                ),
            value = age,
            onValueChange = { value ->
                age = value
            },
            label = { value ->
                context.getString(R.string.years, value)
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = Color.White
            ),
            dividersColor = Color.White,
            range = 0..100
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            modifier = Modifier
                .size(width = 200.dp, height = 48.dp)
                .align(Alignment.CenterHorizontally)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(48.dp)
                ),
            onClick = {
                onContinueClick(age)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = FitnessTheme.color.semiBlack,
                contentColor = FitnessTheme.color.primary
            )
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(id = R.string.continue_),
                color = Color.White,
                style = FitnessTheme.typo.button
            )

        }
    }
}

@Composable
fun HeightPicker(
    onContinueClick: (Int) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    var height by remember {
        mutableIntStateOf(160)
    }
    Column(
        modifier = modifier
            .padding(
                top = innerPadding.calculateTopPadding(),
            )
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.what_is_your_height),
            style = FitnessTheme.typo.h2,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(R.string.just_don_t_be_shy_to_tell_us_your_height_we_ll_help_you_to_keep_it_in_secret),
            textAlign = TextAlign.Center,
            color = Color.White,
            style = FitnessTheme.typo.body3
        )
        Spacer(modifier = Modifier.height(36.dp))
        NumberPicker(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(
                    color = FitnessTheme.color.primary,
                    shape = RoundedCornerShape(8.dp)
                ),
            onValueChange = {
                height = it
            },
            value = height,
            label = { value ->
                "$value cm"
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = Color.White
            ),
            dividersColor = Color.White,
            range = 0..250
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            modifier = Modifier
                .size(width = 200.dp, height = 48.dp)
                .align(Alignment.CenterHorizontally)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(48.dp)
                ),
            onClick = {
                onContinueClick(height)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = FitnessTheme.color.semiBlack,
                contentColor = FitnessTheme.color.primary
            )
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(id = R.string.continue_),
                color = Color.White,
                style = FitnessTheme.typo.button
            )

        }
    }
}


@Composable
fun WeightPicker(
    innerPadding: PaddingValues,
    onContinueClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var weight by remember {
        mutableIntStateOf(60)
    }
    Column(
        modifier = modifier
            .padding(
                top = innerPadding.calculateTopPadding(),
            )
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.what_is_your_weight),
            style = FitnessTheme.typo.h2,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(R.string.just_don_t_be_shy_to_tell_us_your_weight_we_ll_help_you_to_keep_it_in_secret),
            textAlign = TextAlign.Center,
            color = Color.White,
            style = FitnessTheme.typo.body3
        )
        Spacer(modifier = Modifier.height(36.dp))
        NumberPicker(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(
                    color = FitnessTheme.color.primary,
                    shape = RoundedCornerShape(8.dp)
                ),
            value = weight,
            onValueChange = { value ->
                weight = value
            },
            label = { value ->
                "$value kg"
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                color = Color.White
            ),
            dividersColor = Color.White,
            range = 0..150,
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            modifier = Modifier
                .size(width = 200.dp, height = 48.dp)
                .align(Alignment.CenterHorizontally)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(48.dp)
                ),
            onClick = {
                onContinueClick(weight)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = FitnessTheme.color.semiBlack,
                contentColor = FitnessTheme.color.primary
            )
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(id = R.string.continue_),
                color = Color.White,
                style = FitnessTheme.typo.button
            )

        }
    }
}