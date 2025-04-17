package com.kt.worktimetrackermanager.di

import android.content.Context
import com.kt.worktimetrackermanager.data.local.AppDatabase
import com.kt.worktimetrackermanager.data.local.InternalDatabase
import com.kt.worktimetrackermanager.data.remote.RemoteDataSource
import com.kt.worktimetrackermanager.data.remote.api.AuthApi
import com.kt.worktimetrackermanager.data.remote.api.ProjectApi
import com.kt.worktimetrackermanager.data.remote.api.ShiftApi
import com.kt.worktimetrackermanager.data.remote.api.SummaryApi
import com.kt.worktimetrackermanager.data.remote.api.TaskApi
import com.kt.worktimetrackermanager.data.remote.api.TeamApi
import com.kt.worktimetrackermanager.data.remote.api.UserApi
import com.kt.worktimetrackermanager.data.remote.repositories.IAuthRepo
import com.kt.worktimetrackermanager.data.remote.repositories.IProjectRepo
import com.kt.worktimetrackermanager.data.remote.repositories.IShiftRepo
import com.kt.worktimetrackermanager.data.remote.repositories.ISummaryRepo
import com.kt.worktimetrackermanager.data.remote.repositories.ITaskRepo
import com.kt.worktimetrackermanager.data.remote.repositories.ITeamRepo
import com.kt.worktimetrackermanager.data.remote.repositories.IUserRepo
import com.kt.worktimetrackermanager.data.remote.repositories.impl.AuthRepo
import com.kt.worktimetrackermanager.data.remote.repositories.impl.ProjectRepo
import com.kt.worktimetrackermanager.data.remote.repositories.impl.ShiftRepo
import com.kt.worktimetrackermanager.data.remote.repositories.impl.SummaryRepo
import com.kt.worktimetrackermanager.data.remote.repositories.impl.TaskRepo
import com.kt.worktimetrackermanager.data.remote.repositories.impl.TeamRepo
import com.kt.worktimetrackermanager.data.remote.repositories.impl.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return InternalDatabase.newInstance(context)
    }

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

    @Provides
    @Singleton
    fun provideTeamApi(remoteDataSource: RemoteDataSource): TeamApi {
        return remoteDataSource.buildApi(TeamApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTeamRepo(teamApi: TeamApi): ITeamRepo {
        return TeamRepo(teamApi)
    }

    @Provides
    @Singleton
    fun provideProjectApi(remoteDataSource: RemoteDataSource): ProjectApi {
        return remoteDataSource.buildApi(ProjectApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProjectRepo(projectApi: ProjectApi): IProjectRepo {
        return ProjectRepo(projectApi)
    }

    @Provides
    @Singleton
    fun provideTaskApi(remoteDataSource: RemoteDataSource): TaskApi {
        return remoteDataSource.buildApi(TaskApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTaskRepo(taskApi: TaskApi): ITaskRepo {
        return TaskRepo(taskApi)
    }

    @Provides
    @Singleton
    fun provideShiftApi(remoteDataSource: RemoteDataSource): ShiftApi {
        return remoteDataSource.buildApi(ShiftApi::class.java)
    }

    @Provides
    @Singleton
    fun provideShiftRepo(shiftApi: ShiftApi): IShiftRepo {
        return ShiftRepo(shiftApi)
    }
}