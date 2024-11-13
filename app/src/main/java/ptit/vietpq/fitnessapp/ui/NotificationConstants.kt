package ptit.vietpq.fitnessapp.ui

import java.time.LocalTime

object NotificationConstants {
    const val CHANNEL_ID = "fitness_notifications"
    const val CHANNEL_NAME = "Fitness Notifications"
    const val REMINDER_WORK = "reminder_work"
    const val DND_START_WORK = "dnd_start_work"
    const val DND_END_WORK = "dnd_end_work"
    
    val DND_START_TIME: LocalTime = LocalTime.of(22, 0) // 10 PM
    val DND_END_TIME: LocalTime = LocalTime.of(6, 0)    // 6 AM
}
