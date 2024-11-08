package ptit.vietpq.fitnessapp.data.di

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

const val BASE_URL = "https://79bc-2a09-bac5-398e-e6-00-17-34d.ngrok-free.app"

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