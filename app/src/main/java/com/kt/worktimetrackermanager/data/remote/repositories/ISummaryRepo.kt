package com.kt.worktimetrackermanager.data.remote.repositories

import com.kt.worktimetrackermanager.data.remote.dto.response.AttendanceRecord
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.TeamStatistic
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.data.remote.dto.response.WorkHours
import com.skydoves.sandwich.ApiResponse

interface ISummaryRepo {
    suspend fun getTeamAttendanceRecord(
        start: Long,
        end: Long,
        teamId: Int,
        token: String
    ): ApiResponse<DataResponse<AttendanceRecord>>

    suspend fun getCompanyAttendanceRecord(
        start: Long,
        end: Long,
        token: String
    ): ApiResponse<DataResponse<AttendanceRecord>>

    suspend fun getEmployeeAttendanceRecord(
        start: Long,
        end: Long,
        token: String
    ): ApiResponse<DataResponse<AttendanceRecord>>

    suspend fun getTeamEmployeeWorkHours(
        start: Long,
        end: Long,
        teamId: Int,
        token: String
    ): ApiResponse<DataResponse<List<WorkHours>>>

    suspend fun getTeamStatistic(
        token: String
    ): ApiResponse<DataResponse<TeamStatistic>>

    suspend fun getNewHireEmployee(
        start: Long,
        end: Long,
        token: String
    ): ApiResponse<DataResponse<List<User>>>

    suspend fun getCompanyAttendanceRecordEachTime(
        start: Long,
        end: Long,
        period: Long,
        token: String
    ): ApiResponse<DataResponse<List<AttendanceRecord>>>

    suspend fun getTeamAttendanceRecordEachTime(
        start: Long,
        end: Long,
        period: Long,
        token: String
    ): ApiResponse<DataResponse<List<AttendanceRecord>>>

    suspend fun getEmployeeAttendanceRecordEachTime(
        start: Long,
        end: Long,
        period: Long,
        token: String
    ): ApiResponse<DataResponse<List<AttendanceRecord>>>
}