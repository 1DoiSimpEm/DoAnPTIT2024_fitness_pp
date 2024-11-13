package ptit.vietpq.fitnessapp

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FitnessApplication : Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration = Configuration.Builder()
        .setMinimumLoggingLevel(android.util.Log.INFO)
        .build()

}