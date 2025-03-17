package com.kt.worktimetrackermanager.domain.use_case.team

import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.repositories.ITeamRepo
import com.skydoves.sandwich.ApiResponse

class GetCompanyTeamById(
    private val iTeamRepo: ITeamRepo
) {
    suspend operator fun invoke (
        token: String,
        id: Int
    ) : ApiResponse<DataResponse<Team>> {
        return iTeamRepo.getCompanyTeamById(token, id)
    }
}