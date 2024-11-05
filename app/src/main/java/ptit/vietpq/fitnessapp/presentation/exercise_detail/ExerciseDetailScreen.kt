package ptit.vietpq.fitnessapp.presentation.exercise_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R


@Composable
fun ExerciseDetailRoute(){
    ExerciseDetailScreen()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    modifier: Modifier = Modifier
) {
    val chipColor =
        ChipColors(
            containerColor = FitnessTheme.color.blackBg.copy(alpha = 0.25f),
            labelColor = FitnessTheme.color.blackBg,
            leadingIconContentColor = FitnessTheme.color.blackBg,
            trailingIconContentColor = FitnessTheme.color.blackBg,
            disabledLabelColor = FitnessTheme.color.blackBg,
            disabledContainerColor = FitnessTheme.color.blackBg.copy(alpha = 0.1f),
            disabledLeadingIconContentColor = FitnessTheme.color.blackBg,
            disabledTrailingIconContentColor = FitnessTheme.color.blackBg
        )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dumbbell Step Up",
                        color = FitnessTheme.color.primary
                    )
                },
                colors = TopAppBarColors(
                    containerColor = FitnessTheme.color.blackBg,
                    scrolledContainerColor = FitnessTheme.color.blackBg,
                    navigationIconContentColor = FitnessTheme.color.primary,
                    titleContentColor = FitnessTheme.color.primary,
                    actionIconContentColor = FitnessTheme.color.primary

                ),
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = FitnessTheme.color.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = FitnessTheme.color.primary
                        )
                    }
                    IconButton(onClick = { /* Handle profile */ }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Profile",
                            tint = FitnessTheme.color.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(FitnessTheme.color.blackBg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(FitnessTheme.color.primary.copy(alpha = 0.2f))
            ) {
                // Exercise Image
                Image(
                    painter = painterResource(id = R.drawable.ic_apple),  // Replace with your image resource
                    contentDescription = "Exercise demonstration",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Play Button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(FitnessTheme.color.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play video",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                // Favorite Star
                IconButton(
                    onClick = { /* Handle favorite */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint = FitnessTheme.color.limeGreen
                    )
                }
            }

            // Exercise Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FitnessTheme.color.limeGreen
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Dumbbell Step Up",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = FitnessTheme.color.blackBg
                    )

                    Text(
                        text = "Lorem ipsum Dolor Sit Amet, Consectetur Adipiscing Elit. Sed Cursus Libero Eget.",
                        fontSize = 14.sp,
                        color = FitnessTheme.color.blackBg.copy(alpha = 0.7f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Chip(
                            onClick = { },
                            colors = chipColor,
                            label = {
                                Row {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(R.drawable.ic_timer),
                                        contentDescription = "Duration",
                                        tint = FitnessTheme.color.primary
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("12 Minutes", color = Color.White)
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Chip(
                            onClick = { },
                            colors = chipColor,
                            label = {
                                Row {
                                    Icon(
                                        modifier = Modifier.size(16.dp),
                                        painter = painterResource(R.drawable.ic_fire),
                                        contentDescription = "Difficulty",
                                        tint = FitnessTheme.color.primary
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Intermediate", color = Color.White)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Chip(
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    colors: ChipColors
) {
    SuggestionChip(
        onClick = onClick,
        label = label,
        colors = colors,
        shape = RoundedCornerShape(50),
        border = null
    )
}

@Preview
@Composable
private fun ExerciseDetailScreenPreview() {
    ExerciseDetailScreen()
}