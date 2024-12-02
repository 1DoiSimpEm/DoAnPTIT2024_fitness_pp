package ptit.vietpq.fitnessapp.presentation.exercise_guidance

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.util.GraphicOverlay
import ptit.vietpq.fitnessapp.util.SpeechHelper
import ptit.vietpq.fitnessapp.util.posedetector.ExerciseInfo
import ptit.vietpq.fitnessapp.util.posedetector.PoseDetectorProcessor
import kotlin.math.roundToInt


@Composable
fun ExerciseGuidance() {
    PoseDetectionExerciseScreen(onSettingsClick = {})
}

@Composable
fun PoseDetectionExerciseScreen(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var exerciseInfo by remember {
        mutableStateOf(
            ExerciseInfo(
                0,
                0f,
                "",
                mapOf(),
                ""
            )
        )
    }
    DisposableEffect(Unit) {
        SpeechHelper.initialize(context)
        onDispose {
            SpeechHelper.shutdown()
        }
    }

    LaunchedEffect(exerciseInfo.formFeedback) {
        if (exerciseInfo.formFeedback.isNotEmpty()) {
            SpeechHelper.speak(exerciseInfo.formFeedback)
        }
    }

    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        // Camera Preview Setup
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        var previewView by remember { mutableStateOf<PreviewView?>(null) }
        var graphicOverlay by remember { mutableStateOf<GraphicOverlay?>(null) }

        DisposableEffect(lensFacing) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            // Image Analysis Setup
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            // Initialize Pose Detector
            val poseDetector = try {
                val options = PoseDetectorOptions.Builder()
                    .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                    .setPreferredHardwareConfigs(PoseDetectorOptions.CPU_GPU)
                    .build()

                PoseDetectorProcessor(
                    context,
                    options,
                    false,
                    false,
                    false,
                    true,
                    true
                ) { updatedExerciseInfo ->
                    if (updatedExerciseInfo.currentReps > exerciseInfo.currentReps) {
                        exerciseInfo = updatedExerciseInfo
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            preview.setSurfaceProvider(previewView?.surfaceProvider)

            imageAnalyzer.setAnalyzer(
                ContextCompat.getMainExecutor(context)
            ) { imageProxy: ImageProxy ->
                val isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees

                graphicOverlay?.let { overlay ->
                    if (rotationDegrees == 0 || rotationDegrees == 180) {
                        overlay.setImageSourceInfo(
                            imageProxy.width,
                            imageProxy.height,
                            isImageFlipped
                        )
                    } else {
                        overlay.setImageSourceInfo(
                            imageProxy.height,
                            imageProxy.width,
                            isImageFlipped
                        )
                    }
                }

                try {
                    poseDetector?.processImageProxy(
                        imageProxy,
                        graphicOverlay ?: return@setAnalyzer
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

            onDispose {
                try {
                    poseDetector?.stop()
                    cameraProvider.unbindAll()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Camera and Overlay Views
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).also {
                        previewView = it
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            AndroidView(
                factory = { ctx ->
                    GraphicOverlay(ctx, null).also {
                        graphicOverlay = it
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Exercise Information Overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .systemBarsPadding()
        ) {
            // Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exerciseInfo.exerciseType
                        .replace("_CLASS", "")
                        .replace("_", " ")
                        .lowercase()
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Metrics and Feedback Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Metrics Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricBox(
                            label = stringResource(R.string.reps),
                            value = exerciseInfo.currentReps.toString(),
                            color = MaterialTheme.colorScheme.primary
                        )
                        MetricBox(
                            label = stringResource(R.string.form_accuracy),
                            value = "${exerciseInfo.formAccuracy.roundToInt()}%",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    // Joint Angles
                    if (exerciseInfo.jointAngles.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.joint_angles),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            exerciseInfo.jointAngles.forEach { (joint, angle) ->
                                AngleChip(
                                    joint = joint.replace("_", " "),
                                    angle = angle
                                )
                            }
                        }
                    }

                    // Form Feedback
                    if (exerciseInfo.formFeedback.isNotEmpty()) {
                        FormFeedbackBanner(feedback = exerciseInfo.formFeedback)
                    }
                }
            }

            // Camera Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                            CameraSelector.LENS_FACING_FRONT
                        } else {
                            CameraSelector.LENS_FACING_BACK
                        }
                    },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch Camera"
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricBox(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = color
        )
    }
}

@Composable
private fun AngleChip(
    joint: String,
    angle: Float,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = joint,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "${angle.roundToInt()}°",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun FormFeedbackBanner(
    feedback: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.errorContainer,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = feedback,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun PreviewExerciseGuidance() {
    PoseDetectionExerciseScreen(
        onSettingsClick = {}
    )
}