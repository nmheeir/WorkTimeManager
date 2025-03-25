package com.kt.worktimetrackermanager.domain.use_case.team

import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.repositories.ITeamRepo
import com.skydoves.sandwich.ApiResponse

class GetCompanyTeam(
    private val iTeamRepo: ITeamRepo,
) {
    suspend operator fun invoke(
        token: String,
        pageNumber: Int = 1,
        pageSize: Int = 20,
        searchValue: String? = "",
    ): ApiResponse<PagedDataResponse<List<Team>>> {
        return iTeamRepo.getCompanyTeams("Bearer $token", pageNumber, pageSize, searchValue)
    }
}