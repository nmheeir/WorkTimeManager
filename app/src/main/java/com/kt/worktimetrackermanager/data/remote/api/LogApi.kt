package com.kt.worktimetrackermanager.data.remote.api

import com.kt.worktimetrackermanager.data.remote.dto.enums.CheckType
import com.kt.worktimetrackermanager.data.remote.dto.enums.LogStatus
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.LogModel
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface LogApi {

    @GET("Log/team")
    suspend fun getMyTeamLogs(
        @Header("Authorization") token: String,
        @Query("teamId") teamId: Int?,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("type") type: CheckType?,
        @Query("status") status: LogStatus?,
    ): ApiResponse<PagedDataResponse<List<LogModel>>>

    @PUT("Log/updateStatus")
    suspend fun updateStatus(
        @Header("Authorization") token: String,
        @Query("id") id: Int,
        @Query("status") status: LogStatus,
    ) : ApiResponse<DataResponse<Unit>>
}