package ptit.vietpq.fitnessapp.presentation.setup.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.NumberPicker
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R

// Base picker layout to avoid duplication
@Composable
private fun PickerLayout(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
    onContinueClick: () -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(
                top = innerPadding.calculateTopPadding(),
                start = 16.dp,
                end = 16.dp
            )
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = FitnessTheme.typo.h2,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = subtitle,
            textAlign = TextAlign.Center,
            color = Color.White,
            style = FitnessTheme.typo.body3
        )

        Spacer(modifier = Modifier.height(36.dp))

        content()

        Spacer(modifier = Modifier.weight(1f))

        ContinueButton(
            onClick = onContinueClick,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp),
        )
    }
}

// Common number picker style
@Composable
private fun StyledNumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: Iterable<Int>,
    label: (Int) -> String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    NumberPicker(
        modifier = modifier
            .background(
                color = FitnessTheme.color.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .semantics {
                this.contentDescription = contentDescription
            },
        value = value,
        onValueChange = onValueChange,
        range = range,
        label = label,
        textStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            color = Color.White
        ),
        dividersColor = Color.White
    )
}

@Composable
fun WeightPicker(
    innerPadding: PaddingValues,
    onContinueClick: (Int) -> Unit,
    initialWeight: Int,
    modifier: Modifier = Modifier
) {
    var weight by remember { mutableStateOf(initialWeight) }

    PickerLayout(
        title = stringResource(R.string.what_is_your_weight),
        subtitle = stringResource(R.string.just_don_t_be_shy_to_tell_us_your_weight_we_ll_help_you_to_keep_it_in_secret),
        innerPadding = innerPadding,
        onContinueClick = { onContinueClick(weight) },
        modifier = modifier,
        content = {
            StyledNumberPicker(
                value = weight,
                onValueChange = { weight = it },
                range = 30..150,
                label = { "$it kg" },
                contentDescription = stringResource(R.string.weight_picker_content_description)
            )
        }
    )
}

@Composable
fun HeightPicker(
    innerPadding: PaddingValues,
    onContinueClick: (Int) -> Unit,
    initialHeight: Int,
    modifier: Modifier = Modifier
) {
    var height by remember { mutableStateOf(initialHeight) }

    PickerLayout(
        title = stringResource(R.string.what_is_your_height),
        subtitle = stringResource(R.string.just_don_t_be_shy_to_tell_us_your_height_we_ll_help_you_to_keep_it_in_secret),
        innerPadding = innerPadding,
        onContinueClick = { onContinueClick(height) },
        modifier = modifier,
        content = {
            StyledNumberPicker(
                value = height,
                onValueChange = { height = it },
                range = 120..220,
                label = { "$it cm" },
                contentDescription = stringResource(R.string.height_picker_content_description)
            )
        }
    )
}

@SuppressLint("StringFormatMatches")
@Composable
fun AgePicker(
    innerPadding: PaddingValues,
    onContinueClick: (Int) -> Unit,
    initialAge: Int,
    modifier: Modifier = Modifier
) {
    var age by remember { mutableStateOf(initialAge) }

    PickerLayout(
        title = stringResource(R.string.how_old_are_you),
        subtitle = stringResource(R.string.your_age_will_be_used_to_calculate_your_daily_calorie_needs),
        innerPadding = innerPadding,
        onContinueClick = { onContinueClick(age) },
        modifier = modifier,
        content = {
            StyledNumberPicker(
                value = age,
                onValueChange = { age = it },
                range = 13..100,
                label = { "$it" },
                contentDescription = "Age picker"
            )
        }
    )
}

// Common continue button
@Composable
private fun ContinueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .size(width = 200.dp, height = 48.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(48.dp)
            )
            .semantics {
                contentDescription = "Continue"
            },
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = FitnessTheme.color.semiBlack,
            contentColor = FitnessTheme.color.primary
        )
    ) {
        Text(
            text = stringResource(id = R.string.continue_),
            color = Color.White,
            style = FitnessTheme.typo.button,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}