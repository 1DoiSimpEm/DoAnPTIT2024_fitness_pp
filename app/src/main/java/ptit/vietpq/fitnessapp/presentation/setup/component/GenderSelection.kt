package ptit.vietpq.fitnessapp.presentation.setup.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R

@Composable
fun GenderSelection(
    innerPadding: PaddingValues,
    onContinueClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    var isMaleSelected by remember {
        mutableStateOf(
            true
        )
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
            text = stringResource(id = R.string.what_s_your_gender),
            style = FitnessTheme.typo.h2,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = FitnessTheme.color.primary)
                .padding(16.dp),
            text = stringResource(R.string.what_you_are_doesn_t_as_important_as_you_are_not_gay_or_whatever_you_can_be_just_be_normal_don_t_be_weird_so_that_you_can_have_your_chance_to_improve_yourself_just_know_that_you_are_a_human),
            textAlign = TextAlign.Center,
            color = FitnessTheme.color.black,
            style = FitnessTheme.typo.body3
        )
        Spacer(modifier = Modifier.height(36.dp))
        GenderSelectionItem(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            isMale = true,
            isSelected = isMaleSelected,
            onSelected = {
                isMaleSelected = true
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.male),
            style = FitnessTheme.typo.h2,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(24.dp))
        GenderSelectionItem(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            isMale = false,
            isSelected = !isMaleSelected,
            onSelected = {
                isMaleSelected = false
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.female),
            style = FitnessTheme.typo.h2,
            color = Color.White
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
                onContinueClick(isMaleSelected)
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
private fun GenderSelectionItem(
    onSelected : () -> Unit,
    modifier: Modifier = Modifier,
    isMale: Boolean = false,
    isSelected: Boolean = false,
) {
    Box(
        modifier = modifier
            .clickable {
                onSelected()
            }
            .background(
                color = if (isSelected) FitnessTheme.color.limeGreen else FitnessTheme.color.semiBlack,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = if (isSelected) FitnessTheme.color.limeGreen else Color.White,
                shape = CircleShape
            )
            .padding(48.dp)
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            tint = if (isSelected) Color.Black else Color.White,
            painter = painterResource(id = if (isMale) R.drawable.ic_male else R.drawable.ic_female),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun MalePreview() {
    GenderSelectionItem(isMale = true, isSelected = true, onSelected =  {})
}

@Preview
@Composable
private fun FemalePreview() {
    GenderSelectionItem(isMale = false, onSelected = {})
}