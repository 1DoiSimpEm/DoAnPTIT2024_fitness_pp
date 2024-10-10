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
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

const val BASE_URL = "https://4668-117-4-241-89.ngrok-free.app/"

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class OkHttpQualifierWithoutToken

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RetrofitAuthQualifier

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
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideAuthService(@RetrofitAuthQualifier retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)
}