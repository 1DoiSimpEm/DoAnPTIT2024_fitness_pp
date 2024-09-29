package ptit.vietpq.fitnessapp.data.local.sharepref

import android.content.SharedPreferences
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class is used to save and get data from SharePreference
 * @param sharedPreferences: SharedPreferences
 * @param moshi: Moshi
 * @return SharePreferenceProvider
 */

@Singleton
class SharePreferenceProvider @Inject constructor(
    val sharedPreferences: SharedPreferences, val moshi: Moshi
) {

    @ToJson
    inline fun <reified T> save(key: String, any: Any) {
        val editor = sharedPreferences.edit()
        when (any) {
            is String -> editor.putString(key, any)
            is Float -> editor.putFloat(key, any)
            is Int -> editor.putInt(key, any)
            is Long -> editor.putLong(key, any)
            is Boolean -> editor.putBoolean(key, any)
            else -> {
                val adapter = moshi.adapter(any.javaClass)
                editor.putString(key, adapter.toJson(any))
            }
        }
        editor.apply()
    }


    // Get type of data from SharePreference
    @FromJson
    inline fun <reified T> get(key: String, defaultValue: T? = null): T? {
        when (T::class) {
            Float::class -> return sharedPreferences.getFloat(
                key,
                (defaultValue as? Float) ?: 0f
            ) as T

            Int::class -> return sharedPreferences.getInt(key, (defaultValue as? Int) ?: 0) as T
            Long::class -> return sharedPreferences.getLong(key, (defaultValue as? Long) ?: 0) as T
            String::class -> return sharedPreferences.getString(
                key,
                (defaultValue as? String) ?: ""
            ) as T

            Boolean::class -> return sharedPreferences.getBoolean(
                key,
                (defaultValue as? Boolean) ?: false
            ) as T

            else -> {
                val any = sharedPreferences.getString(key, "")
                val adapter = moshi.adapter(T::class.java)
                if (!any.isNullOrEmpty()) {
                    return adapter.fromJson(any)
                }
            }
        }
        return null
    }

    companion object {
        const val NAME_SHARE_PREFERENCE = "FitnessAppSharePref"
    }
}
