package ptit.vietpq.fitnessapp.presentation.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.util.PatternsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qrcode.qrscanner.barcode.barcodescan.qrreader.designsystem.FitnessTheme
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.extension.toast


@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState: LoginUiState by viewModel.uiState.collectAsStateWithLifecycle()
    when (uiState) {
        is LoginUiState.Empty -> {
            // Do nothing
        }

        is LoginUiState.Error -> {
            context.toast((uiState as LoginUiState.Error).message)
        }

        is LoginUiState.Loading -> {

        }

        is LoginUiState.LoginSuccess -> {

        }

        is LoginUiState.RegisterSuccess -> {

        }

    }

    LoginScreen(
        onLoginClick = viewModel::login,
        onRegisterClick = viewModel::register,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: (String, String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isRegistering by remember {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = modifier,
        containerColor = FitnessTheme.color.black,
        contentColor = FitnessTheme.color.primary,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (isRegistering) stringResource(R.string.create_account) else stringResource(
                            R.string.login_in
                        ),
                        style = FitnessTheme.typo.innerBoldSize20LineHeight28,
                        color = FitnessTheme.color.limeGreen
                    )
                },
                colors = TopAppBarColors(
                    containerColor = FitnessTheme.color.black,
                    actionIconContentColor = FitnessTheme.color.limeGreen,
                    scrolledContainerColor = FitnessTheme.color.black,
                    titleContentColor = FitnessTheme.color.limeGreen,
                    navigationIconContentColor = FitnessTheme.color.limeGreen

                ),
                navigationIcon = {
                    if (isRegistering)
                        Image(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .clickable {
                                    if (isRegistering) {
                                        isRegistering = false
                                    }
                                },
                            painter = painterResource(id = R.drawable.ic_back_arrow),
                            contentDescription = null
                        )
                }
            )
        },
        content = { innerPadding ->

            AnimatedVisibility(visible = !isRegistering) {
                LoginForm(
                    innerPadding = innerPadding,
                    onLoginClick = onLoginClick,
                    onToRegister = {
                        isRegistering = true
                    }
                )
            }
            AnimatedVisibility(visible = isRegistering) {
                RegisterForm(
                    innerPadding = innerPadding,
                    onRegisterClick = onRegisterClick
                )
            }
        }
    )
}

@Composable
private fun RegisterForm(
    innerPadding: PaddingValues,
    onRegisterClick: (String, String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var userNameTextState by remember {
        mutableStateOf("")
    }
    var emailTextState by remember {
        mutableStateOf("")
    }
    var passwordTextState by remember {
        mutableStateOf("")
    }
    var confirmPasswordTextState by remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier
            .padding(
                start = innerPadding.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                end = innerPadding.calculateEndPadding(layoutDirection = LayoutDirection.Ltr),
                top = innerPadding.calculateTopPadding() + 24.dp
            )
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.let_s_start),
            style = FitnessTheme.typo.innerBoldSize20LineHeight28,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(48.dp))
        EditTextForm(title = stringResource(R.string.name), hint = "Bro",
            onTextChanged = {
                userNameTextState = it
            }
        )
        EditTextForm(title = stringResource(R.string.email), hint = "@example@example.com",
            onTextChanged = {
                emailTextState = it
            }
        )
        EditTextForm(
            title = stringResource(R.string.password),
            hint = "Password",
            isPassword = true,
            onTextChanged = {
                passwordTextState = it
            }
        )
        EditTextForm(
            title = stringResource(R.string.confirm_password),
            hint = stringResource(R.string.confirm_password),
            isPassword = true,
            onTextChanged = {
                confirmPasswordTextState = it
            }
        )
        Spacer(modifier = Modifier.height(48.dp))
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
                if (passwordTextState == confirmPasswordTextState && PatternsCompat.EMAIL_ADDRESS.matcher(
                        emailTextState
                    ).matches()
                ) {
                    onRegisterClick(userNameTextState, emailTextState, passwordTextState)
                } else {
                    // Show error
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = FitnessTheme.color.neutral1,
                contentColor = FitnessTheme.color.primary
            )
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(R.string.register),
                color = Color.White,
                style = FitnessTheme.typo.button
            )

        }
    }
}

@Composable
private fun LoginForm(
    innerPadding: PaddingValues,
    onLoginClick: (String, String) -> Unit,
    onToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    var userNameTextState by remember {
        mutableStateOf("")
    }
    var passwordTextState by remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier
            .padding(
                start = innerPadding.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                end = innerPadding.calculateEndPadding(layoutDirection = LayoutDirection.Ltr),
                top = innerPadding.calculateTopPadding() + 24.dp
            )
            .fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.welcome),
            style = FitnessTheme.typo.innerBoldSize20LineHeight28,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.you_have_just_signed_up_for_the_best_fitness_app_in_the_world_let_s_get_started),
            style = FitnessTheme.typo.body1,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(64.dp))
        EditTextForm(
            title = stringResource(R.string.username_or_email),
            hint = stringResource(R.string.enter_your_email),
            onTextChanged = {
                userNameTextState = it
            }
        )
        EditTextForm(
            title = stringResource(R.string.password),
            hint = stringResource(R.string.enter_your_password),
            isPassword = true,
            onTextChanged = {
                passwordTextState = it
            },
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    //TODO: Forgot password
                }
                .background(FitnessTheme.color.primary)
                .fillMaxWidth()
                .padding(top = 8.dp, end = 16.dp, bottom = 8.dp),
            text = stringResource(R.string.forgot_password),
            style = FitnessTheme.typo.subTitle,
            color = FitnessTheme.color.black,
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(48.dp))
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
                onLoginClick(userNameTextState, passwordTextState)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = FitnessTheme.color.semiBlack,
                contentColor = FitnessTheme.color.primary
            )
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(id = R.string.login),
                color = Color.White,
                style = FitnessTheme.typo.button
            )

        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.don_t_have_an_account),
                color = Color.White,
                style = FitnessTheme.typo.body1
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.clickable {
                    onToRegister()
                },
                text = stringResource(R.string.sign_up),
                color = FitnessTheme.color.limeGreen,
                style = FitnessTheme.typo.body1
            )
        }
    }
}

@Composable
private fun EditTextForm(
    title: String,
    hint: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
) {
    val inputTextState = remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier
            .background(color = FitnessTheme.color.primary)
            .padding(vertical = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = title,
            style = FitnessTheme.typo.subTitle,
            color = FitnessTheme.color.black
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                ),
            value = inputTextState.value,
            onValueChange = {
                inputTextState.value = it
                onTextChanged(it)
            },
            placeholder = {
                Text(
                    text = hint,
                    style = FitnessTheme.typo.body1,
                    color = FitnessTheme.color.black
                )
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = FitnessTheme.color.primary,
                unfocusedPlaceholderColor = Color.Transparent,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.Transparent,
                disabledTextColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            ),
            textStyle = FitnessTheme.typo.body1,
            singleLine = true,
            maxLines = 1,
            visualTransformation = if (!isPassword) VisualTransformation.None else PasswordVisualTransformation(),
        )
    }
}

@Preview
@Composable
private fun PreviewLoginScreen() {
    LoginScreen({ _, _ -> }, { _, _, _ -> })
}
