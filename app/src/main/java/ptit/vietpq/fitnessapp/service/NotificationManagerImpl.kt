package ptit.vietpq.fitnessapp.service

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.service.DndWorker.Companion.KEY_IS_DND_ACTIVE
import ptit.vietpq.fitnessapp.ui.NotificationConstants
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationManagerImpl @Inject constructor(
    private val workManager: WorkManager,
    private val sharedPreferenceProvider: SharePreferenceProvider,
) {

    fun setupNotifications() {
        val isGeneralEnabled =
            sharedPreferenceProvider.get(SharePreferenceProvider.GENERAL_NOTIFICATION, false)
                ?: false
        if (isGeneralEnabled) {
            scheduleReminder()
            setupDndMode()
        } else {
            cancelAllWork()
        }

    }

    private fun scheduleReminder() {
        val isRemindersEnabled =
            sharedPreferenceProvider.get(SharePreferenceProvider.REMINDERS, false) ?: false
        if (isRemindersEnabled) {
            val constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()
            val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
                24, TimeUnit.HOURS,
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS
            ).setConstraints(constraint)
                .addTag(NotificationConstants.REMINDER_WORK)
                .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
                .build()
            workManager.enqueueUniquePeriodicWork(
                NotificationConstants.REMINDER_WORK,
                ExistingPeriodicWorkPolicy.UPDATE,
                reminderRequest
            )
        } else {
            workManager.cancelAllWorkByTag(NotificationConstants.REMINDER_WORK)
        }
    }

    private fun setupDndMode() {
        val isDndEnabled = sharedPreferenceProvider.get(SharePreferenceProvider.DND_MODE, false) ?: false

        if (isDndEnabled) {
            // Schedule DND start
            scheduleDndWork(true, NotificationConstants.DND_START_WORK, NotificationConstants.DND_START_TIME)

            // Schedule DND end
            scheduleDndWork(false, NotificationConstants.DND_END_WORK, NotificationConstants.DND_END_TIME)
        } else {
            workManager.cancelAllWorkByTag(NotificationConstants.DND_START_WORK)
            workManager.cancelAllWorkByTag(NotificationConstants.DND_END_WORK)

            // Reset any active DND state
            if (sharedPreferenceProvider.get(KEY_IS_DND_ACTIVE, false) == true) {
                // Create one-time work to end DND immediately
                val immediateEndDndRequest = OneTimeWorkRequestBuilder<DndWorker>()
                    .setInputData(DndWorker.createInputDataForDndState(false))
                    .build()
                workManager.enqueue(immediateEndDndRequest)
            }
        }
    }

    private fun scheduleDndWork(isStart: Boolean, tag: String, targetTime: LocalTime) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val initialDelay = calculateDelayUntil(targetTime)

        val dndWorkRequest = PeriodicWorkRequestBuilder<DndWorker>(
            24, TimeUnit.HOURS,
            PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS, TimeUnit.MILLISECONDS
        )
            .setConstraints(constraints)
            .addTag(tag)
            .setInputData(DndWorker.createInputDataForDndState(isStart))
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.UPDATE,
            dndWorkRequest
        )
    }

    private fun calculateDelayUntil(targetTime: LocalTime): Long {
        val now = LocalTime.now()
        var delay = Duration.between(now, targetTime).toMillis()
        if (delay < 0) {
            delay += TimeUnit.DAYS.toMillis(1)
        }
        return delay
    }


    private fun calculateInitialDelay(): Long {
        val now = LocalTime.now()
        val targetTime = LocalTime.of(9, 0)
        var delay = Duration.between(now, targetTime).toMillis()
        if (delay < 0) {
            delay += TimeUnit.DAYS.toMillis(1)
        }
        return delay
    }


    private fun cancelAllWork() {
        workManager.cancelAllWork()
    }

}