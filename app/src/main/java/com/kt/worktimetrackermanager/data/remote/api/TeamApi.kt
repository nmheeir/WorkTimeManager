package com.kt.worktimetrackermanager.data.remote.api

import com.kt.worktimetrackermanager.data.remote.dto.request.CreateTeamRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TeamApi {

    @GET("CompanyTeam/getCompanyTeams")
    suspend fun getCompanyTeams(
        @Header("Authorization") token: String,
        @Query("searchValue") searchValue: String?,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
    ) : ApiResponse<PagedDataResponse<List<Team>>>

    @GET("CompanyTeam/getCompanyTeamById")
    suspend fun getCompanyTeamById(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): ApiResponse<DataResponse<Team>>

    @POST("CompanyTeam/create")
    suspend fun createTeam(
        @Header("Authorization") token: String,
        @Body createTeamRequest: CreateTeamRequest
    ) : ApiResponse<DataResponse<Any>>
}