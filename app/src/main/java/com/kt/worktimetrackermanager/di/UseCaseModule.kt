package com.kt.worktimetrackermanager.di

import com.kt.worktimetrackermanager.data.remote.repositories.IAuthRepo
import com.kt.worktimetrackermanager.data.remote.repositories.ISummaryRepo
import com.kt.worktimetrackermanager.data.remote.repositories.ITeamRepo
import com.kt.worktimetrackermanager.data.remote.repositories.IUserRepo
import com.kt.worktimetrackermanager.domain.use_case.AuthUseCase
import com.kt.worktimetrackermanager.domain.use_case.Login
import com.kt.worktimetrackermanager.domain.use_case.summary.GetCompanyAttendanceRecord
import com.kt.worktimetrackermanager.domain.use_case.summary.GetCompanyAttendanceRecordEachTime
import com.kt.worktimetrackermanager.domain.use_case.summary.GetEmployeeAttendanceRecord
import com.kt.worktimetrackermanager.domain.use_case.summary.GetEmployeeAttendanceRecordEachTime
import com.kt.worktimetrackermanager.domain.use_case.summary.GetNewHireEmployee
import com.kt.worktimetrackermanager.domain.use_case.summary.GetTeamAttendanceRecord
import com.kt.worktimetrackermanager.domain.use_case.summary.GetTeamAttendanceRecordEachTime
import com.kt.worktimetrackermanager.domain.use_case.summary.GetTeamEmployeeWorkHours
import com.kt.worktimetrackermanager.domain.use_case.summary.GetTeamStatistic
import com.kt.worktimetrackermanager.domain.use_case.summary.SummaryUseCase
import com.kt.worktimetrackermanager.domain.use_case.team.CreateTeam
import com.kt.worktimetrackermanager.domain.use_case.team.GetCompanyTeam
import com.kt.worktimetrackermanager.domain.use_case.team.GetCompanyTeamById
import com.kt.worktimetrackermanager.domain.use_case.team.TeamUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.AddUser
import com.kt.worktimetrackermanager.domain.use_case.user.GetUserById
import com.kt.worktimetrackermanager.domain.use_case.user.GetUserByUsername
import com.kt.worktimetrackermanager.domain.use_case.user.GetUserInTeam
import com.kt.worktimetrackermanager.domain.use_case.user.GetUserProfile
import com.kt.worktimetrackermanager.domain.use_case.user.GetUsers
import com.kt.worktimetrackermanager.domain.use_case.user.GetUsersStatistic
import com.kt.worktimetrackermanager.domain.use_case.user.UpdateUser
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideAuthUseCase(iAuthRepo: IAuthRepo): AuthUseCase {
        return AuthUseCase(
            login = Login(iAuthRepo),
        )
    }

    @Provides
    @Singleton
    fun provideUserUseCase(iUserRepo: IUserRepo): UserUseCase {
        return UserUseCase(
            getUserByUsername = GetUserByUsername(iUserRepo),
            addUser = AddUser(iUserRepo),
            getUserInTeam = GetUserInTeam(iUserRepo),
            getUserById = GetUserById(iUserRepo),
            updateUser = UpdateUser(iUserRepo),
            getUsers = GetUsers(iUserRepo),
            getUsersStatistic = GetUsersStatistic(iUserRepo),
            getUserProfile = GetUserProfile(iUserRepo)
        )
    }

    @Provides
    @Singleton
    fun provideTeamUseCase(iTeamRepo: ITeamRepo): TeamUseCase {
        return TeamUseCase(
            getCompanyTeams = GetCompanyTeam(iTeamRepo),
            createTeam = CreateTeam(iTeamRepo),
            getCompanyTeamById = GetCompanyTeamById(iTeamRepo)
        )
    }

    @Provides
    @Singleton
    fun provideSummaryUseCase(iSummaryRepo: ISummaryRepo): SummaryUseCase {
        return SummaryUseCase(
            getEmployeeAttendanceRecord = GetEmployeeAttendanceRecord(iSummaryRepo),
            getTeamAttendanceRecord = GetTeamAttendanceRecord(iSummaryRepo),
            getCompanyAttendanceRecord = GetCompanyAttendanceRecord(iSummaryRepo),
            getTeamEmployeeWorkHours = GetTeamEmployeeWorkHours(iSummaryRepo),
            getTeamStatistic = GetTeamStatistic(iSummaryRepo),
            getNewHireEmployee = GetNewHireEmployee(iSummaryRepo),
            getCompanyAttendanceRecordEachTime = GetCompanyAttendanceRecordEachTime(iSummaryRepo),
            getTeamAttendanceRecordEachTime = GetTeamAttendanceRecordEachTime(iSummaryRepo),
            getEmployeeAttendanceRecordEachTime = GetEmployeeAttendanceRecordEachTime(iSummaryRepo)
        )
    }
}