package com.kt.worktimetrackermanager.data.remote.repositories

import com.kt.worktimetrackermanager.data.remote.dto.response.AttendanceRecord
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.TeamStatistic
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.data.remote.dto.response.WorkHours
import com.skydoves.sandwich.ApiResponse
import java.time.LocalDateTime

interface ISummaryRepo {
    suspend fun getTeamAttendanceRecord(
        start: LocalDateTime,
        end: LocalDateTime,
        teamId: Int? = null,
        token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>>

    suspend fun getCompanyAttendanceRecord(
        start: LocalDateTime,
        end: LocalDateTime,
        token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>>

    suspend fun getEmployeeAttendanceRecord(
        start: LocalDateTime,
        end: LocalDateTime,
        userId: Int? = null,
        token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>>

    suspend fun getTeamEmployeeWorkHours(
        start: LocalDateTime,
        end: LocalDateTime,
        teamId: Int,
        token: String,
    ): ApiResponse<DataResponse<List<WorkHours>>>

    suspend fun getTeamStatistic(
        token: String,
    ): ApiResponse<DataResponse<TeamStatistic>>

    suspend fun getNewHireEmployee(
        start: LocalDateTime,
        end: LocalDateTime,
        token: String,
    ): ApiResponse<DataResponse<List<User>>>

    suspend fun getCompanyAttendanceRecordEachTime(
        start: LocalDateTime,
        end: LocalDateTime,
        period: Long,
        token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>>

    suspend fun getTeamAttendanceRecordEachTime(
        start: LocalDateTime,
        end: LocalDateTime,
        period: Long,
        teamId: Int? = null,
        token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>>

    suspend fun getEmployeeAttendanceRecordEachTime(
        start: LocalDateTime,
        end: LocalDateTime,
        period: Long,
        userId: Int? = null,
        token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>>
}