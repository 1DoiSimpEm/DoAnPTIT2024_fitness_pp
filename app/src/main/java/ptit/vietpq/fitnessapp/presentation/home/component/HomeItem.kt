package ptit.vietpq.fitnessapp.presentation.home.component

import android.widget.Space
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R


@Composable
fun HomeItem(
    onItemSelected : () -> Unit ,
    modifier: Modifier = Modifier,
    text : String = stringResource(R.string.workout),
    @DrawableRes image : Int = R.drawable.ic_dumbell,
    isSelected : Boolean = false
) {
    val tint by animateColorAsState(if (isSelected) FitnessTheme.color.limeGreen else FitnessTheme.color.primary,
        label = ""
    )

    Column (
        modifier = modifier.clickable {
            onItemSelected()
        },
    ){
        Icon(
            modifier = Modifier.align(Alignment.CenterHorizontally).size(36.dp),
            painter = painterResource(image),
            tint = tint,
            contentDescription = "Dumbell",
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = text,
            style = FitnessTheme.typo.body,
            color = tint
        )
    }
}


@Preview
@Composable
private fun PreviewHomeItem(){
    HomeItem({})
}