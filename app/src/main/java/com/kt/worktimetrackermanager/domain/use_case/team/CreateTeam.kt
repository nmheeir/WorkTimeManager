package com.kt.worktimetrackermanager.domain.use_case.team

import com.kt.worktimetrackermanager.data.remote.dto.request.CreateTeamRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.repositories.ITeamRepo
import com.skydoves.sandwich.ApiResponse

class CreateTeam(
    private val iTeamRepo: ITeamRepo
) {
    suspend operator fun  invoke (
        token: String,
        createTeam: CreateTeamRequest
    ): ApiResponse<DataResponse<Any>> {
        return iTeamRepo.createTeam(token, createTeam)
    }
}