package ptit.vietpq.fitnessapp.extension

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.changeLocale(code : String){
    val dm = resources.displayMetrics
    val conf = resources.configuration
    conf.locale = Locale(code)
    resources.updateConfiguration(conf, dm)
}

fun AppCompatActivity.restartActivity() {
    val intent = Intent(this, this::class.java)
    finish()
    startActivity(intent)
}