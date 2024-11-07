package ptit.vietpq.fitnessapp.data.di

import com.azure.ai.inference.ChatCompletionsClient
import com.azure.ai.inference.ChatCompletionsClientBuilder
import com.azure.core.credential.AzureKeyCredential
import com.azure.core.util.Configuration
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ptit.vietpq.fitnessapp.data.remote.intercepter.FormUrlEncodedInterceptor
import ptit.vietpq.fitnessapp.data.remote.service.AuthService
import ptit.vietpq.fitnessapp.data.remote.service.ExerciseService
import ptit.vietpq.fitnessapp.data.remote.service.MealService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

const val BASE_URL = "https://ca1d-2405-4802-1a03-e280-9145-40d0-eb89-783d.ngrok-free.app"
const val GITHUB_KEY = "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"
const val CHAT_END_POINT = "https://models.inference.ai.azure.com"

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class OkHttpQualifierWithoutToken

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RetrofitAuthQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ChatCompletionQualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @OkHttpQualifierWithoutToken
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(FormUrlEncodedInterceptor())
            .build()
    }

    @RetrofitAuthQualifier
    @Provides
    @Singleton
    fun proveAuthRetrofit(
        @OkHttpQualifierWithoutToken okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            okHttpClient.newBuilder().callTimeout(30, TimeUnit.SECONDS).build()
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideAuthService(@RetrofitAuthQualifier retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideExerciseService(@RetrofitAuthQualifier retrofit: Retrofit): ExerciseService =
        retrofit.create(ExerciseService::class.java)

    @Provides
    @Singleton
    fun provideMealPlanningService(@RetrofitAuthQualifier retrofit: Retrofit): MealService =
        retrofit.create(MealService::class.java)
}