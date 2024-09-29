package ptit.vietpq.fitnessapp.di

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

  @Provides
  @Singleton
  fun provideMoshi(): Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .add(KotlinJsonAdapterFactory())
    .build()

  @Singleton
  @Provides
  fun sharedPreference(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences(
    SharePreferenceProvider.NAME_SHARE_PREFERENCE,
    Context.MODE_PRIVATE,
  )

}
