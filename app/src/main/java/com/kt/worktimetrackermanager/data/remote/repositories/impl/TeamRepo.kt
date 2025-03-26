package com.kt.worktimetrackermanager.data.remote.repositories.impl

import com.kt.worktimetrackermanager.data.remote.api.TeamApi
import com.kt.worktimetrackermanager.data.remote.dto.request.CreateTeamRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.repositories.ITeamRepo
import com.skydoves.sandwich.ApiResponse

class TeamRepo(
    private val teamApi: TeamApi
) : ITeamRepo {
    override suspend fun getCompanyTeams(
        token: String,
        pageNumber: Int,
        pageSize: Int,
        searchValue: String?
    ): ApiResponse<PagedDataResponse<List<Team>>> {
        return teamApi.getCompanyTeams(token, searchValue, pageNumber, pageSize)
    }

    override suspend fun getCompanyTeamById(
        token: String,
        id: Int?
    ): ApiResponse<DataResponse<Team>> {
        return teamApi.getCompanyTeamById(token, id)
    }


    override suspend fun createTeam(
        token: String,
        createTeamRequest: CreateTeamRequest
    ): ApiResponse<DataResponse<Any>> {
        return teamApi.createTeam("Bearer $token", createTeamRequest)
    }
}