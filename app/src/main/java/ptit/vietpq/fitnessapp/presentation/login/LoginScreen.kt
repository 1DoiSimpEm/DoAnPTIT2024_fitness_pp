package ptit.vietpq.fitnessapp.presentation.login

import android.widget.Space
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.extension.toast


@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val eventFlow = viewModel.eventFlow.collectAsStateWithLifecycle(initialValue = LoginState.Empty)
    var showLoading by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(eventFlow.value) {
        when (val event = eventFlow.value) {
            is LoginState.LoginSuccess -> {
                context.toast("Login success")
                onLoginSuccess()
                showLoading = false
            }

            is LoginState.RegisterSuccess -> {
                context.toast("Register success")
                showLoading = false
            }

            is LoginState.Error -> {
                context.toast(event.message)
                showLoading = false
            }

            LoginState.Empty -> {
                // Do nothing
            }

            LoginState.Loading -> {
                showLoading = true
            }
        }
    }
    LoginScreen(
        onLoginClick = viewModel::login,
        showLoading = showLoading,
        onRegisterClick = onRegisterClick
    )
}

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    showLoading: Boolean,
    modifier: Modifier = Modifier,
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

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

    // Pulse animation for the logo
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
                // App Title and Tagline
                Text(
                    text = stringResource(R.string.app_name),
                    color = Color.White,
                    style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.transform_your_life_one_rep_at_a_time),
                    color = Color.White.copy(alpha = 0.9f),
                    style = FitnessTheme.typo.innerBoldSize16LineHeight24,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FitnessCenter,  // Fitness-specific icon
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                AnimatedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = stringResource(R.string.username_or_email),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.password),
                    isPassword = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Password,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Remember me checkbox with fitness-themed text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,
                            uncheckedColor = Color.White.copy(alpha = 0.7f)
                        )
                    )
                    Text(
                        text = stringResource(R.string.keep_me_active),  // Fitness-themed remember me text
                        color = Color.White,
                        modifier = Modifier.clickable { rememberMe = !rememberMe }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login button with fitness theme
                Button(
                    onClick = { onLoginClick(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)  // Fitness green
                    ),
                    enabled = email.isNotEmpty() && password.isNotEmpty() && !showLoading
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
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.start_your_journey),  // Fitness-themed login text
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onForgotPasswordClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Reset Your Password",  // More friendly password reset text
                        color = Color.White,
                        style = TextStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }

                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.ready_to_start_your_fitness_journey),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.join_now),
                        color = Color.White,
                        style = TextStyle(
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.clickable(onClick = onRegisterClick)
                    )
                }

                // Motivational text at the bottom
                Text(
                    text = stringResource(R.string.your_strongest_self_awaits),
                    color = Color.White.copy(alpha = 0.8f),
                    style = FitnessTheme.typo.innerRegularSize14LineHeight20,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun AnimatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    val strokeWidth by animateFloatAsState(
        targetValue = if (isFocused) 2f else 1f,
        label = "stroke"
    )

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = if (isFocused) Color.White else Color.White.copy(alpha = 0.7f)
            )
        },
        leadingIcon = leadingIcon,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = strokeWidth.dp,
                color = if (isFocused) Color.White else Color.White.copy(alpha = 0.7f),
                shape = RoundedCornerShape(12.dp)
            )
            .onFocusEvent {
                isFocused = it.isFocused
            },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.1f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
    )

}


@Preview
@Composable
private fun PreviewLoginScreen() {
    LoginScreen({ _, _ -> }, false)
}