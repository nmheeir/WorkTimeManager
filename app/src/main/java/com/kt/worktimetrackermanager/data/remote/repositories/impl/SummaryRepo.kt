package com.kt.worktimetrackermanager.data.remote.repositories.impl

import com.kt.worktimetrackermanager.data.remote.api.SummaryApi
import com.kt.worktimetrackermanager.data.remote.dto.response.AttendanceRecord
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.TeamStatistic
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.data.remote.dto.response.WorkHours
import com.kt.worktimetrackermanager.data.remote.repositories.ISummaryRepo
import com.skydoves.sandwich.ApiResponse
import java.time.LocalDateTime

class SummaryRepo(
    private val summaryApi: SummaryApi,
) : ISummaryRepo {
    override suspend fun getTeamAttendanceRecord(
        start: LocalDateTime,
        end: LocalDateTime,
        teamId: Int?,
        token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>> {
        return summaryApi.getTeamAttendanceRecord(start, end, teamId, token)
    }

    override suspend fun getEmployeeAttendanceRecord(
        start: LocalDateTime,
        end: LocalDateTime,
        token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>> {
        return summaryApi.getEmployeeAttendanceRecord(start, end, token)
    }

    override suspend fun getCompanyAttendanceRecord(
        start: LocalDateTime,
        end: LocalDateTime,
        token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>> {
        return summaryApi.getCompanyAttendanceRecord(start, end, token)
    }

    override suspend fun getTeamEmployeeWorkHours(
        start: LocalDateTime,
        end: LocalDateTime,
        teamId: Int,
        token: String,
    ): ApiResponse<DataResponse<List<WorkHours>>> {
        return summaryApi.getTeamEmployeeWorkHours(start, end, teamId, token)
    }

    override suspend fun getTeamStatistic(token: String): ApiResponse<DataResponse<TeamStatistic>> {
        return summaryApi.getTeamStatistic(token)
    }

    override suspend fun getNewHireEmployee(
        start: LocalDateTime,
        end: LocalDateTime,
        token: String,
    ): ApiResponse<DataResponse<List<User>>> {
        return summaryApi.getNewHireEmployee(start, end, token)
    }

    override suspend fun getCompanyAttendanceRecordEachTime(
        start: LocalDateTime,
        end: LocalDateTime,
        period: Long,
        token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>> {
        return summaryApi.getCompanyAttendanceRecordEachTime(start, end, period, token)
    }

    override suspend fun getTeamAttendanceRecordEachTime(
        start: LocalDateTime,
        end: LocalDateTime,
        period: Long,
        teamId: Int?,
        token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>> {
        return summaryApi.getTeamAttendanceRecordEachTime(start, end, period, teamId, token)
    }

    override suspend fun getEmployeeAttendanceRecordEachTime(
        start: LocalDateTime,
        end: LocalDateTime,
        period: Long,
        token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>> {
        return summaryApi.getEmployeeAttendanceRecordEachTime(start, end, period, token)
    }
}