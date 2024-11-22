package ptit.vietpq.fitnessapp.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.alpha
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
import androidx.compose.ui.unit.sp
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
        onRegisterClick = onRegisterClick,
        onForgotPasswordClick = {
            context.toast("Coming soon...")
        }
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
                    },
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
                        text = stringResource(R.string.reset_your_password),  // More friendly password reset text
                        color = Color.White,
                        style = TextStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }

                Column(
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
    isPassword: Boolean = false,
    regex: Regex? = null,
    errorMessage: String = stringResource(R.string.invalid_input)
) {
    var isFocused by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    // Animations
    val strokeWidth by animateFloatAsState(
        targetValue = if (isFocused) 2f else 1f,
        label = "stroke"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> Color.Red
            isFocused -> Color.White
            else -> Color.White.copy(alpha = 0.7f)
        },
        label = "borderColor"
    )

    val containerColor by animateColorAsState(
        targetValue = when {
            isError -> Color.Red.copy(alpha = 0.1f)
            isFocused -> Color.White.copy(alpha = 0.1f)
            else -> Color.White.copy(alpha = 0.1f)
        },
        label = "containerColor"
    )

    val errorAlpha by animateFloatAsState(
        targetValue = if (showError) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "errorAlpha"
    )

    val shakeOffset by animateFloatAsState(
        targetValue = if (isError) 0f else 10f,
        animationSpec = spring(
            dampingRatio = 0.2f,
            stiffness = Spring.StiffnessHigh
        ),
        label = "shakeOffset"
    )

    Column {
        TextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(newValue)
                if (regex != null && newValue.isNotEmpty()) {
                    isError = !newValue.matches(regex)
                    showError = isError
                } else {
                    isError = false
                    showError = false
                }
            },
            label = {
                Text(
                    text = label,
                    color = when {
                        isError -> Color.Red
                        isFocused -> Color.White
                        else -> Color.White.copy(alpha = 0.7f)
                    }
                )
            },
            leadingIcon = leadingIcon,
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = strokeWidth.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .offset(x = if (isError) shakeOffset.dp else 0.dp)
                .onFocusEvent {
                    isFocused = it.isFocused
                    if (!it.isFocused && regex != null) {
                        isError = !value.matches(regex)
                        showError = isError
                    }
                },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorContainerColor = containerColor,
                errorIndicatorColor = Color.Transparent,
                errorLeadingIconColor = Color.Red,
                errorTrailingIconColor = Color.Red,
                errorCursorColor = Color.Red,
                errorLabelColor = Color.Red
            ),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = true,
            isError = isError
        )

        AnimatedVisibility(
            visible = showError,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(start = 16.dp, top = 4.dp)
                    .alpha(errorAlpha)
            )
        }
    }
}


@Preview
@Composable
private fun PreviewLoginScreen() {
    LoginScreen({ _, _ -> }, false)
}