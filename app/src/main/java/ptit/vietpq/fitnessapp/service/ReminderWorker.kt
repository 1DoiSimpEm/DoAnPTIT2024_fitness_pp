package ptit.vietpq.fitnessapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.ui.NotificationConstants
import java.time.LocalTime
import javax.inject.Inject

class ReminderWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters,
    private val sharePreferenceProvider: SharePreferenceProvider,
    private val notificationManager: NotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val isGeneralEnabled = sharePreferenceProvider.get(SharePreferenceProvider.GENERAL_NOTIFICATION, false) ?: false
            val isRemindersEnabled = sharePreferenceProvider.get(SharePreferenceProvider.REMINDERS, false) ?: false
            val isDndEnabled = sharePreferenceProvider.get(SharePreferenceProvider.DND_MODE, false) ?: false

            if (!isGeneralEnabled || !isRemindersEnabled) {
                return@withContext Result.success()
            }

            if (isDndEnabled && isInDndTimeRange(LocalTime.now())) {
                return@withContext Result.success()
            }

            showNotification()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun isInDndTimeRange(currentTime: LocalTime): Boolean {
        return if (NotificationConstants.DND_START_TIME > NotificationConstants.DND_END_TIME) {
            currentTime >= NotificationConstants.DND_START_TIME || currentTime < NotificationConstants.DND_END_TIME
        } else {
            currentTime >= NotificationConstants.DND_START_TIME && currentTime < NotificationConstants.DND_END_TIME
        }
    }

    private fun showNotification() {
        // Create notification channel for Android O and above
        val channel = NotificationChannel(
            NotificationConstants.CHANNEL_ID,
            NotificationConstants.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Fitness app notifications"
            enableVibration(sharePreferenceProvider.get(SharePreferenceProvider.VIBRATE, false) ?: false)
        }
        notificationManager.createNotificationChannel(channel)

        val shouldPlaySound = sharePreferenceProvider.get(SharePreferenceProvider.SOUND, false) ?: false
        val shouldVibrate = sharePreferenceProvider.get(SharePreferenceProvider.VIBRATE, false) ?: false
        val showOnLockScreen = sharePreferenceProvider.get(SharePreferenceProvider.LOCK_SCREEN, false) ?: false

        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.fitness_reminder))
            .setContentText(context.getString(R.string.time_for_your_daily_workout))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setVisibility(
                if (showOnLockScreen) NotificationCompat.VISIBILITY_PUBLIC
                else NotificationCompat.VISIBILITY_PRIVATE
            )

        if (!shouldPlaySound) {
            builder.setSound(null)
        }

        if (shouldVibrate) {
            builder.setVibrate(longArrayOf(0, 500, 250, 500))
        } else {
            builder.setVibrate(null)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}