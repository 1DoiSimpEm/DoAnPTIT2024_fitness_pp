package ptit.vietpq.fitnessapp.presentation.meal_plans

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ptit.vietpq.fitnessapp.designsystem.FitnessTheme
import kotlinx.collections.immutable.persistentListOf
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.remote.response.MealPlanResponse
import ptit.vietpq.fitnessapp.extension.formatTimestamp
import ptit.vietpq.fitnessapp.ui.common.LoadingDialog

@Composable
fun MealListRoute(
    onBackPressed: () -> Unit,
    onMealAddClicked: () -> Unit,
    onMealPlanClick: (MealPlanResponse) -> Unit,
    viewModel: MealListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MealPlanListScreen(
        uiState = uiState,
        onBackPressed = onBackPressed,
        onMealPlanClick = {
            onMealPlanClick(it)
        },
        onMealAddClicked = onMealAddClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanListScreen(
    uiState: MealListUiState,
    onMealAddClicked: () -> Unit,
    onMealPlanClick: (MealPlanResponse) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {

    LoadingDialog(isLoading = uiState.isLoading)

    Scaffold(
        modifier = modifier.background(Color(0xFF1E1E1E)),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(id = R.string.meal_plans),
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
                    Image(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable(onClick = onBackPressed),
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = "Back"
                    )
                },
                actions = {
                    Row(modifier = Modifier
                        .padding(
                            end = 16.dp
                        )
                        .clickable {
                            onMealAddClicked()
                        }) {
                        Icon(imageVector = Icons.Default.FoodBank, contentDescription = null)

                    }

                }
            )
        },
        contentColor = FitnessTheme.color.primary,
        containerColor = Color(0xFF1E1E1E)
    ) { innerPadding ->
        if (uiState.mealList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPadding.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
            ) {
                item { Spacer(modifier = Modifier.height(24.dp)) }
                items(
                    items = uiState.mealList,
                    key = {
                        it.id
                    }) { mealPlan ->
                    MealPlanCard(
                        mealPlan = mealPlan,
                        onClick = { onMealPlanClick(mealPlan) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else {
            if (uiState.isLoading) return@Scaffold
            MealEmpty(
                onCreateMealClick = onMealAddClicked
            )
        }
    }
}

@Composable
private fun MealEmpty(
    modifier: Modifier = Modifier,
    onCreateMealClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Fastfood,
            contentDescription = null,
            modifier = Modifier.size(128.dp),
            tint = FitnessTheme.color.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.no_meals),
            fontSize = 18.sp,
            color = Color.White,
            style = FitnessTheme.typo.caption
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onCreateMealClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = FitnessTheme.color.limeGreen,
                contentColor = FitnessTheme.color.primary
            )
        ) {
            Text(
                text = stringResource(id = R.string.create_meal_prompt),
                style = FitnessTheme.typo.button,
            )
        }
    }
}

@Composable
private fun MealPlanCard(
    mealPlan: MealPlanResponse,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F1F)
        ),
        border = BorderStroke(1.dp, Color(0xFF6200EE)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = mealPlan.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFCCFF00),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = mealPlan.description,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp),
                maxLines = 3
            )

            Text(
                text = stringResource(R.string.created, (mealPlan.timeStamp.formatTimestamp())),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}


@Preview
@Composable
private fun PreviewMealPlanList() {
    val sampleMealPlans = listOf(
        MealPlanResponse(
            id = 1,
            name = "Weight Loss Meal Plan",
            description = "A balanced meal plan designed for healthy weight loss with focus on protein and vegetables.",
            userId = 123,
            timeStamp = System.currentTimeMillis() / 1000
        ),
        MealPlanResponse(
            id = 2,
            name = "Muscle Gain Plan",
            description = "High protein meal plan optimized for muscle growth and recovery.",
            userId = 123,
            timeStamp = System.currentTimeMillis() / 1000 - 86400 // Yesterday
        )
    )

    MealPlanListScreen(
        uiState = MealListUiState(
            false,
            persistentListOf()
        ),
        onMealPlanClick = {},
        onBackPressed = {},
        onMealAddClicked = {}
    )
}