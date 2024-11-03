package ptit.vietpq.fitnessapp.presentation.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.remote.response.ExerciseResponse

@Composable
fun ExerciseCard(
    onItemClicked : (ExerciseResponse) -> Unit,
    exerciseResponse: ExerciseResponse,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
) {
    val context = LocalContext.current
    val tint by animateColorAsState(
        if (isFavorite) FitnessTheme.color.limeGreen else FitnessTheme.color.white,
        label = ""
    )
    Box(
        modifier = modifier
            .padding(8.dp)
            .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onItemClicked(exerciseResponse)
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                AsyncImage(
                    contentDescription = null,
                    model = ImageRequest.Builder(context).data(
                        exerciseResponse.image
                    ).build(),
                    imageLoader = ImageLoader(context),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_star), // Star icon resource
                    contentDescription = "Favorite",
                    tint = tint,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exerciseResponse.name,
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
                Row(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Calories Icon and Text
                Row(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
    ExerciseCard(
        onItemClicked = { },
        ExerciseResponse(
            name = "Squat Exercise",
            description = "Squat is a full body exercise that trains primarily the muscles of the thighs, hips and buttocks, quadriceps femoris muscle, hamstrings, as well as strengthening the bones, ligaments and insertion of the tendons throughout the lower body.",
            videoUrl = "https://www.youtube.com/watch?v=U3Hj5ZCpGzY",
            image = "https://images.unsplash.com/photo-1619117459660-3b1b3b3b3b3b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwyMjIwNzV8MHwxfGFsbHwxf",
            muscleGroupId = 1,
            id = 1
        ),
    )
}