package com.kt.worktimetrackermanager.di

import com.kt.worktimetrackermanager.data.remote.RemoteDataSource
import com.kt.worktimetrackermanager.data.remote.api.AuthApi
import com.kt.worktimetrackermanager.data.remote.repositories.IAuthRepo
import com.kt.worktimetrackermanager.data.remote.repositories.impl.AuthRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAuthRepo(authApi: AuthApi): IAuthRepo {
        return AuthRepo(authApi)
    }

    @Provides
    @Singleton
    fun provideAuthApi(remoteDataSource: RemoteDataSource): AuthApi {
        return remoteDataSource.buildApi(AuthApi::class.java)
    }
}