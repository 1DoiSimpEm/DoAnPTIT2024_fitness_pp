package ptit.vietpq.fitnessapp.core

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.AppCoroutineScope
import ptit.vietpq.fitnessapp.core.DefaultAppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.DefaultAppCoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface CoreModule {
    @Binds
    @Singleton
    fun appCoroutineDispatchers(impl: DefaultAppCoroutineDispatchers): AppCoroutineDispatchers

    @Binds
    @Singleton
    fun appCoroutineScope(impl: DefaultAppCoroutineScope): AppCoroutineScope
}
