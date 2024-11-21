package ptit.vietpq.fitnessapp.presentation.register

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.extension.toast
import ptit.vietpq.fitnessapp.presentation.login.AnimatedTextField
import ptit.vietpq.fitnessapp.presentation.login.LoginState


@Composable
fun RegisterRoute(
    onBackPressed: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val event by viewModel.eventFlow.collectAsStateWithLifecycle(LoginState.Empty)
    val context = LocalContext.current
    var showLoading by remember { mutableStateOf(false) }
    LaunchedEffect(event) {
        when (event) {
            LoginState.Empty -> Unit
            is LoginState.Error -> {
                context.toast((event as LoginState.Error).message)
                showLoading = false
            }

            LoginState.Loading -> {
                showLoading = true
            }

            LoginState.LoginSuccess -> Unit
            LoginState.RegisterSuccess -> {
                showLoading = false
                onBackPressed()
            }
        }
    }
    RegisterScreen(
        onRegisterClick = viewModel::register,
        onBackToLogin = onBackPressed,
        showLoading = showLoading
    )
}


@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String) -> Unit,
    showLoading: Boolean,
    modifier: Modifier = Modifier,
    onBackToLogin: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient"
    )
    val scale by animateFloatAsState(
        targetValue = if (showLoading) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF4CAF50),  // Fitness green
                        Color(0xFF2196F3),  // Energetic blue
                        Color(0xFF1976D2)   // Deep blue for strength
                    ),
                    start = Offset(gradientOffset, 0f),
                    end = Offset(gradientOffset + 1000f, 1000f)
                )
            )
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.Center),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.15f)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackToLogin) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to login",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = stringResource(R.string.create_account),
                        color = Color.White,
                        style = FitnessTheme.typo.innerBoldSize20LineHeight28,
                    )
                    // Empty box for centering
                    Box(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .scale(scale),
                    contentAlignment = Alignment.Center,

                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Name field
                AnimatedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = stringResource(R.string.name),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email field
                AnimatedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = stringResource(R.string.email),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                AnimatedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.password),
                    isPassword = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Password field
                AnimatedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = stringResource(R.string.confirm_password),
                    isPassword = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        if (password == confirmPassword && email.isNotEmpty() && name.isNotEmpty()) {
                            onRegisterClick(name, email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)  // Fitness green
                    ),
                    enabled = password == confirmPassword &&
                            password.isNotEmpty() &&
                            email.isNotEmpty() &&
                            name.isNotEmpty() || !showLoading
                ) {
                    if (showLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AppRegistration,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.register),  // Fitness-themed login text
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.already_have_an_account),
                        color = Color.White
                    )
                    TextButton(onClick = onBackToLogin) {
                        Text(
                            text = stringResource(R.string.login),
                            color = Color.White,
                            style = TextStyle(textDecoration = TextDecoration.Underline)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRegisterScreen() {
    RegisterScreen(
        onRegisterClick = { _, _, _ -> },
        showLoading = true
    )
}