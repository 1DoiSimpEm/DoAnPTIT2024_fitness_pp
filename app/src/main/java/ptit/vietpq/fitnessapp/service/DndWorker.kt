package ptit.vietpq.fitnessapp.service

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.R
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.ui.NotificationConstants
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DndWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters,
    private val sharePreferenceProvider: SharePreferenceProvider,
    private val notificationManager: android.app.NotificationManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val isDndEnabled = sharePreferenceProvider.get(SharePreferenceProvider.DND_MODE, false) ?: false
            if (!isDndEnabled) {
                return@withContext Result.success()
            }

            // Check if this is start or end DND work
            val isStartDnd = inputData.getBoolean(KEY_IS_START_DND, true)
            
            if (isStartDnd) {
                handleDndStart()
            } else {
                handleDndEnd()
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun handleDndStart() {
        // Update notification channel to silent mode
        val channel = notificationManager.getNotificationChannel(NotificationConstants.CHANNEL_ID)
        channel?.apply {
            importance = android.app.NotificationManager.IMPORTANCE_LOW
            setSound(null, null)
            enableVibration(false)
            notificationManager.createNotificationChannel(this)
        }

        // Save DND state
        sharePreferenceProvider.save(KEY_IS_DND_ACTIVE, true)
        
        // Optional: Show a notification that DND mode is active
        showDndStateNotification(true)
    }

    private fun handleDndEnd() {
        // Restore notification channel to normal settings
        val channel = notificationManager.getNotificationChannel(NotificationConstants.CHANNEL_ID)
        channel?.apply {
            importance = android.app.NotificationManager.IMPORTANCE_DEFAULT
            // Restore sound and vibration based on user preferences
            val shouldVibrate = sharePreferenceProvider.get(SharePreferenceProvider.VIBRATE, false) ?: false
            enableVibration(shouldVibrate)
            notificationManager.createNotificationChannel(this)
        }

        // Clear DND state
        sharePreferenceProvider.save(KEY_IS_DND_ACTIVE, false)
        
        // Optional: Show a notification that DND mode is inactive
        showDndStateNotification(false)
    }

    private fun showDndStateNotification(isDndActive: Boolean) {
        val shouldShowNotification = sharePreferenceProvider.get(SharePreferenceProvider.GENERAL_NOTIFICATION, false) ?: false
        if (!shouldShowNotification) return

        val title = if (isDndActive) context.getString(R.string.do_not_disturb_mode_active) else context.getString(
            R.string.do_not_disturb_mode_ended
        )
        val message = if (isDndActive)
            context.getString(
                R.string.notifications_will_be_silent_until,
                NotificationConstants.DND_END_TIME.format(
                    DateTimeFormatter.ofPattern("HH:mm")
                )
            )
        else
            context.getString(R.string.notifications_restored_to_normal)

        val builder = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)

        notificationManager.notify(DND_NOTIFICATION_ID, builder.build())
    }

    companion object {
        private const val KEY_IS_START_DND = "is_start_dnd"
        const val KEY_IS_DND_ACTIVE = "is_dnd_active"
        private const val DND_NOTIFICATION_ID = 1001

        fun createInputDataForDndState(isStart: Boolean): Data {
            return Data.Builder()
                .putBoolean(KEY_IS_START_DND, isStart)
                .build()
        }
    }
}
