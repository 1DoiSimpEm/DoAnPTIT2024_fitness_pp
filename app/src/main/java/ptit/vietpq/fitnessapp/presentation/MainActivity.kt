package ptit.vietpq.fitnessapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.presentation.exercise.ExerciseDestination
import ptit.vietpq.fitnessapp.presentation.exercise_category.ExerciseCategoryDestination
import ptit.vietpq.fitnessapp.presentation.exercise_detail.ExerciseDetailDestination
import ptit.vietpq.fitnessapp.presentation.home.HomeDestination
import ptit.vietpq.fitnessapp.presentation.login.LoginDestination
import ptit.vietpq.fitnessapp.presentation.main.FitnessApp
import ptit.vietpq.fitnessapp.presentation.meal_planning.MealPlanningDestination
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var sharePreferenceProvider: SharePreferenceProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitnessApp(
                HomeDestination
//                if(sharePreferenceProvider.isSetupFinished){
//                    HomeDestination
//                }
//                else if (sharePreferenceProvider.accessToken.isEmpty()) {
//                    LoginDestination
//                } else {
//                    HomeDestination
//                }
                ,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

