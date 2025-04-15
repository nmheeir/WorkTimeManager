package com.kt.worktimetrackermanager.data.remote.api

import com.kt.worktimetrackermanager.data.remote.dto.request.AddShiftRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Shift
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDateTime

interface ShiftApi {

    @POST("Shift/add")
    suspend fun addShift(
        @Header("Authorization") token: String,
        shifts: List<AddShiftRequest>,
    ): ApiResponse<DataResponse<Unit>>

    @GET("Shift/team")
    suspend fun getShiftsInTeam(
        @Header("Authorization") token: String,
        @Query("teamId") teamId: Int? = null,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 10,
        @Query("start") start: LocalDateTime? = null,
        @Query("end") end: LocalDateTime? = null,
    ): ApiResponse<PagedDataResponse<List<Shift>>>
}