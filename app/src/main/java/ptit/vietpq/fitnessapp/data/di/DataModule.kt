package ptit.vietpq.fitnessapp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ptit.vietpq.fitnessapp.data.repository.AuthRepositoryImpl
import ptit.vietpq.fitnessapp.domain.repository.AuthRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindAuthRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository
}