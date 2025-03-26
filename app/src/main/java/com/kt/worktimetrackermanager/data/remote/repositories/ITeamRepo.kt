package com.kt.worktimetrackermanager.data.remote.repositories

import com.kt.worktimetrackermanager.data.remote.dto.request.CreateTeamRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.skydoves.sandwich.ApiResponse

interface ITeamRepo {
    suspend fun getCompanyTeams(
        token: String,
        pageNumber: Int,
        pageSize: Int,
        searchValue: String?
    ): ApiResponse<PagedDataResponse<List<Team>>>

    suspend fun getCompanyTeamById(
        token: String,
        id: Int? = null
    ): ApiResponse<DataResponse<Team>>

    suspend fun createTeam(
        token: String,
        createTeamRequest: CreateTeamRequest
    ): ApiResponse<DataResponse<Any>>
}