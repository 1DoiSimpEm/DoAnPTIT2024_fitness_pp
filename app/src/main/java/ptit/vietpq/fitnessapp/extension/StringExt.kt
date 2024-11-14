package ptit.vietpq.fitnessapp.extension

import ptit.vietpq.fitnessapp.data.di.BASE_URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatTimestamp(): String {
    val date = Date(this * 1000) // Convert Unix timestamp to milliseconds
    val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return format.format(date)
}

fun String.withUrl() = "$BASE_URL/$this"