package com.kt.worktimetrackermanager.di

import com.kt.worktimetrackermanager.data.remote.RemoteDataSource
import com.kt.worktimetrackermanager.data.remote.api.AuthApi
import com.kt.worktimetrackermanager.data.remote.api.SummaryApi
import com.kt.worktimetrackermanager.data.remote.api.UserApi
import com.kt.worktimetrackermanager.data.remote.repositories.IAuthRepo
import com.kt.worktimetrackermanager.data.remote.repositories.ISummaryRepo
import com.kt.worktimetrackermanager.data.remote.repositories.IUserRepo
import com.kt.worktimetrackermanager.data.remote.repositories.impl.AuthRepo
import com.kt.worktimetrackermanager.data.remote.repositories.impl.SummaryRepo
import com.kt.worktimetrackermanager.data.remote.repositories.impl.UserRepo
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

    @Provides
    @Singleton
    fun provideUserRepo(userApi: UserApi): IUserRepo {
        return UserRepo(userApi)
    }

    @Provides
    @Singleton
    fun provideUserApi(remoteDataSource: RemoteDataSource): UserApi {
        return remoteDataSource.buildApi(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSummaryApi(remoteDataSource: RemoteDataSource): SummaryApi {
        return remoteDataSource.buildApi(SummaryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSummaryRepo(summaryApi: SummaryApi): ISummaryRepo {
        return SummaryRepo(summaryApi)
    }
}