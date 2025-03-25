package com.kt.worktimetrackermanager.data.remote.api

import com.kt.worktimetrackermanager.data.remote.dto.response.AttendanceRecord
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.TeamStatistic
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.data.remote.dto.response.WorkHours
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import java.time.LocalDateTime

interface SummaryApi {

    //Get Attendance Record
    @GET("Summary/GetTeamAttendanceRecord")
    suspend fun getTeamAttendanceRecord(
        @Query("start") start: LocalDateTime,
        @Query("end") end: LocalDateTime,
        @Query("teamId") teamId: Int? = null,
        @Header("Authorization") token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>>

    @GET("Summary/GetCompanyAttendanceRecord")
    suspend fun getCompanyAttendanceRecord(
        @Query("start") start: LocalDateTime,
        @Query("end") end: LocalDateTime,
        @Header("Authorization") token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>>

    @GET("Summary/GetEmployeeAttendanceRecord")
    suspend fun getEmployeeAttendanceRecord(
        @Query("start") start: LocalDateTime,
        @Query("end") end: LocalDateTime,
        @Query("userId") userId: Int? = null,
        @Header("Authorization") token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>>


    @GET("Summary/GetTeamEmployeeWorkTime")
    suspend fun getTeamEmployeeWorkHours(
        @Query("start") start: LocalDateTime,
        @Query("end") end: LocalDateTime,
        @Query("teamId") teamId: Int,
        @Header("Authorization") token: String,
    ): ApiResponse<DataResponse<List<WorkHours>>>

    @GET("Summary/GetTeamStatistic")
    suspend fun getTeamStatistic(
        @Header("Authorization") token: String,
    ): ApiResponse<DataResponse<TeamStatistic>>

    @GET("Summary/GetNewHireEmployee")
    suspend fun getNewHireEmployee(
        @Query("start") start: LocalDateTime,
        @Query("end") end: LocalDateTime,
        @Header("Authorization") token: String,
    ): ApiResponse<DataResponse<List<User>>>

    // ================================
    //Get Attendance Each Time
    @GET("Summary/GetCompanyAttendanceRecordEachTime")
    suspend fun getCompanyAttendanceRecordEachTime(
        @Query("start") start: LocalDateTime,
        @Query("end") end: LocalDateTime,
        @Query("period") period: Long,
        @Header("Authorization") token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>>

    @GET("Summary/GetTeamAttendanceRecordEachTime")
    suspend fun getTeamAttendanceRecordEachTime(
        @Query("start") start: LocalDateTime,
        @Query("end") end: LocalDateTime,
        @Query("period") period: Long,
        @Query("teamId") teamId: Int? = null,
        @Header("Authorization") token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>>

    @GET("Summary/GetEmployeeAttendanceRecordEachTime")
    suspend fun getEmployeeAttendanceRecordEachTime(
        @Query("start") start: LocalDateTime,
        @Query("end") end: LocalDateTime,
        @Query("period") period: Long,
        @Query("userId") userId: Int? = null,
        @Header("Authorization") token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>>
    //End Get Attendance Each Time
    //======================

}