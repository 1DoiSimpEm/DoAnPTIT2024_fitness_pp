package ptit.vietpq.fitnessapp.presentation.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R

@Composable
fun ExerciseCard(
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
) {
    val tint by animateColorAsState(if (isFavorite) FitnessTheme.color.limeGreen else FitnessTheme.color.white,
        label = ""
    )
    Box(
        modifier = modifier
            .width(250.dp)
            .padding(16.dp)
            .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_progress),
                    contentDescription = "Exercise Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_star), // Star icon resource
                    contentDescription = "Favorite",
                    tint = tint,
                    modifier = Modifier.run {
                        size(32.dp)
                            .padding(8.dp)
                            .align(Alignment.TopEnd)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Squat Exercise",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFFE5FF00), // Light yellow color
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            // Icons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Time Icon and Text
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_timer), // Timer icon resource
                        contentDescription = "Timer",
                        tint = Color(0xFF9B6FFF), // Custom purple color
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "12 Minutes",
                        color = Color(0xFFB2B2B2), // Gray text color
                        fontSize = 14.sp
                    )
                }

                // Calories Icon and Text
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_fire), // Fire icon resource
                        contentDescription = "Calories",
                        tint = Color(0xFF9B6FFF), // Custom purple color
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "120 Kcal",
                        color = Color(0xFFB2B2B2), // Gray text color
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Preview
@Composable
private fun ExerciseCardPreview() {
    ExerciseCard()
}