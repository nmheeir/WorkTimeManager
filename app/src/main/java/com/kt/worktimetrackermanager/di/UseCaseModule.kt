package com.kt.worktimetrackermanager.di

import com.kt.worktimetrackermanager.data.remote.repositories.IAuthRepo
import com.kt.worktimetrackermanager.data.remote.repositories.ILogRepo
import com.kt.worktimetrackermanager.data.remote.repositories.IProjectRepo
import com.kt.worktimetrackermanager.data.remote.repositories.IShiftRepo
import com.kt.worktimetrackermanager.data.remote.repositories.ISummaryRepo
import com.kt.worktimetrackermanager.data.remote.repositories.ITaskRepo
import com.kt.worktimetrackermanager.data.remote.repositories.ITeamRepo
import com.kt.worktimetrackermanager.data.remote.repositories.IUserRepo
import com.kt.worktimetrackermanager.domain.use_case.AuthUseCase
import com.kt.worktimetrackermanager.domain.use_case.Login
import com.kt.worktimetrackermanager.domain.use_case.RequestPasswordReset
import com.kt.worktimetrackermanager.domain.use_case.ResetPassword
import com.kt.worktimetrackermanager.domain.use_case.log.GetTeamLogs
import com.kt.worktimetrackermanager.domain.use_case.log.LogUseCase
import com.kt.worktimetrackermanager.domain.use_case.log.UpdateLogStatus
import com.kt.worktimetrackermanager.domain.use_case.project.CreateProject
import com.kt.worktimetrackermanager.domain.use_case.project.GetProject
import com.kt.worktimetrackermanager.domain.use_case.project.GetProjects
import com.kt.worktimetrackermanager.domain.use_case.project.GetTasksFromProject
import com.kt.worktimetrackermanager.domain.use_case.project.ProjectUseCase
import com.kt.worktimetrackermanager.domain.use_case.shift.AddShift
import com.kt.worktimetrackermanager.domain.use_case.shift.GetShiftsInTeam
import com.kt.worktimetrackermanager.domain.use_case.shift.ShiftUseCase
import com.kt.worktimetrackermanager.domain.use_case.summary.GetCompanyAttendanceRecord
import com.kt.worktimetrackermanager.domain.use_case.summary.GetCompanyAttendanceRecordEachTime
import com.kt.worktimetrackermanager.domain.use_case.summary.GetEmployeeAttendanceRecord
import com.kt.worktimetrackermanager.domain.use_case.summary.GetEmployeeAttendanceRecordEachTime
import com.kt.worktimetrackermanager.domain.use_case.summary.GetNewHireEmployee
import com.kt.worktimetrackermanager.domain.use_case.summary.GetTeamAttendanceRecord
import com.kt.worktimetrackermanager.domain.use_case.summary.GetTeamAttendanceRecordEachTime
import com.kt.worktimetrackermanager.domain.use_case.summary.GetTeamEmployeeWorkHours
import com.kt.worktimetrackermanager.domain.use_case.summary.GetTeamShiftsStat
import com.kt.worktimetrackermanager.domain.use_case.summary.GetTeamShiftsStatById
import com.kt.worktimetrackermanager.domain.use_case.summary.GetTeamStatistic
import com.kt.worktimetrackermanager.domain.use_case.summary.SummaryUseCase
import com.kt.worktimetrackermanager.domain.use_case.task.CreateTask
import com.kt.worktimetrackermanager.domain.use_case.task.GetTaskDetail
import com.kt.worktimetrackermanager.domain.use_case.task.TaskUseCase
import com.kt.worktimetrackermanager.domain.use_case.team.CreateTeam
import com.kt.worktimetrackermanager.domain.use_case.team.GetCompanyTeam
import com.kt.worktimetrackermanager.domain.use_case.team.GetCompanyTeamById
import com.kt.worktimetrackermanager.domain.use_case.team.TeamUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.AddUser
import com.kt.worktimetrackermanager.domain.use_case.user.GetUserById
import com.kt.worktimetrackermanager.domain.use_case.user.GetUserByUsername
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
            resetPassword = ResetPassword(iAuthRepo),
            requestPasswordReset = RequestPasswordReset(iAuthRepo)
        )
    }

    @Provides
    @Singleton
    fun provideUserUseCase(iUserRepo: IUserRepo): UserUseCase {
        return UserUseCase(
            getUserByUsername = GetUserByUsername(iUserRepo),
            addUser = AddUser(iUserRepo),
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
            getEmployeeAttendanceRecordEachTime = GetEmployeeAttendanceRecordEachTime(iSummaryRepo),
            getTeamShiftsStat = GetTeamShiftsStat(iSummaryRepo),
            getTeamShiftsStatById = GetTeamShiftsStatById(iSummaryRepo)
        )
    }

    @Provides
    @Singleton
    fun provideProjectUseCase(iProjectRepo: IProjectRepo): ProjectUseCase {
        return ProjectUseCase(
            getProjects = GetProjects(iProjectRepo),
            getProject = GetProject(iProjectRepo),
            getTasksFromProject = GetTasksFromProject(iProjectRepo),
            createProject = CreateProject(iProjectRepo)
        )
    }

    @Provides
    @Singleton
    fun provideTaskUseCase(iTaskRepo: ITaskRepo): TaskUseCase {
        return TaskUseCase(
            getTaskDetail = GetTaskDetail(iTaskRepo),
            createTask = CreateTask(iTaskRepo)
        )
    }

    @Provides
    @Singleton
    fun provideShiftUseCase(iShiftRepo: IShiftRepo): ShiftUseCase {
        return ShiftUseCase(
            addShift = AddShift(iShiftRepo),
            getShiftsInTeam = GetShiftsInTeam(iShiftRepo)
        )
    }

    @Provides
    @Singleton
    fun provideLogUseCase(iLogRepo: ILogRepo): LogUseCase {
        return LogUseCase(
            getTeamLogs = GetTeamLogs(iLogRepo),
            updateLogStatus = UpdateLogStatus(iLogRepo)
        )
    }
}