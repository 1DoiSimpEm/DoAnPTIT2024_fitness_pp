package ptit.vietpq.fitnessapp.presentation.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.extension.toast
import ptit.vietpq.fitnessapp.ui.common.LoadingDialog


@Composable
fun ProfileRoute(
    onBackPressed: () -> Unit,
    onMealListNavigate: () -> Unit,
    onSettingNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val state by viewModel.eventFlow.collectAsStateWithLifecycle(initialValue = ProfileState.Idle)
    val context = LocalContext.current
    var isLoading by remember {
        mutableStateOf(false)
    }
    when (state) {
        ProfileState.Error -> {
            isLoading = false
            context.toast("Error")
        }

        ProfileState.Idle -> Unit
        ProfileState.Loading -> {
            isLoading = true
        }

        ProfileState.Success -> {
            isLoading = false
            context.toast("Success")
        }
    }

    ProfileScreen(
        uiState = uiState,
        isLoading = isLoading,
        onBackPressed = onBackPressed,
        onSettingNavigate = onSettingNavigate,
        onMealListNavigate = onMealListNavigate,
        onUpdateProfile = viewModel::updateUser
    )
}


@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    isLoading: Boolean,
    onBackPressed: () -> Unit,
    onMealListNavigate: () -> Unit,
    onSettingNavigate: () -> Unit,
    onUpdateProfile: (
        height: Int,
        weight: Int,
        age: Int,
        gender: String,
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val purple = Color(0xFF9D84FF)
    val darkBackground = Color(0xFF1E1E1E)
    val lightPurple = Color(0xFFE5E0FF)
    var isUpdatingState by remember { mutableStateOf(false) }
    var fullName by remember { mutableStateOf(uiState.user?.username) }
    var age by remember { mutableIntStateOf(uiState.user?.age ?: 0) }
    var weight by remember { mutableIntStateOf(uiState.user?.weight ?: 0) }
    var height by remember { mutableStateOf("${uiState.user?.height}") }
    var email by remember { mutableStateOf(uiState.user?.email ?: "") }

    BackHandler {
        if (isUpdatingState) {
            isUpdatingState = false
        } else {
            onBackPressed()
        }
    }

    LaunchedEffect(uiState) {
        fullName = uiState.user?.fullName ?: ""
        age = uiState.user?.age ?: 0
        weight = uiState.user?.weight ?: 0
        height = "${uiState.user?.height ?: 0}"
        email = uiState.user?.email ?: ""
    }

    LoadingDialog(isLoading = isLoading)

    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(darkBackground)
                .padding(paddingValues)
        ) {

            // Profile Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(purple)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Back button and title row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        modifier = Modifier.clickable {
                            if (isUpdatingState) {
                                isUpdatingState = false
                            } else {
                                onBackPressed()
                            }
                        },
                        contentDescription = "Back",
                        tint = Color.White
                    )
                    Text(
                        text = stringResource(R.string.my_profile),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Text(
                    fullName ?: "",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    email,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    "Age: $age",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Stats Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("${uiState.user?.weight} Kg", stringResource(R.string.weight))
                    Spacer(
                        modifier = Modifier
                            .width(2.dp)
                            .align(Alignment.CenterVertically)
                            .height(36.dp)
                            .background(Color.White)
                    )
                    StatItem(uiState.user?.age.toString(), stringResource(R.string.years_old))
                    Spacer(
                        modifier = Modifier
                            .width(2.dp)
                            .align(Alignment.CenterVertically)
                            .height(36.dp)
                            .background(Color.White)
                    )
                    StatItem("${height} CM", stringResource(R.string.height))
                }
            }
            if (isUpdatingState) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    FormField(
                        label = "Full name",
                        value = fullName.toString(),
                        onValueChange = { fullName = it })
                    FormField(
                        label = "Age",
                        value = age.toString(),
                        onValueChange = { age = it.toInt() })
                    FormField(
                        label = "Email",
                        value = email,
                        onValueChange = { email = it })
                    FormField(
                        label = "Weight",
                        value = weight.toString(),
                        onValueChange = { weight = it.toInt() })
                    FormField(label = "Height", value = height, onValueChange = { height = it })

                    Button(
                        onClick = {
                            onUpdateProfile(
                                height.toInt(),
                                weight,
                                age,
                                uiState.user?.gender ?: ""
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE7FF90)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Update Profile",
                            color = Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            } else {
                MenuListItem(Icons.Default.Person, stringResource(R.string.profile)) {
                    isUpdatingState = !isUpdatingState
                }
                MenuListItem(Icons.Default.Fastfood, stringResource(R.string.meal_plans)) {
                    onMealListNavigate()
                }
                MenuListItem(Icons.Default.Lock, stringResource(R.string.privacy_policy))
                MenuListItem(Icons.Default.Settings, stringResource(R.string.settings)){
                    onSettingNavigate()
                }
                MenuListItem(Icons.Default.Headphones, stringResource(R.string.help))
                MenuListItem(Icons.Default.ExitToApp, stringResource(R.string.logout))
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            value,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            label,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = Color(0xFF9D84FF),
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun MenuListItem(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = text,
            tint = Color(0xFF9D84FF),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text,
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = Color.White
        )
    }
}
