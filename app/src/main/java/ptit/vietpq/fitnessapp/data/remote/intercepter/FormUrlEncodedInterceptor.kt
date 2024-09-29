package ptit.vietpq.fitnessapp.data.remote.intercepter

import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response

class FormUrlEncodedInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalBody = original.body
        if(originalBody is FormBody) {
            val request = original.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build()
            return chain.proceed(request)
        }
        return chain.proceed(original)
    }
}