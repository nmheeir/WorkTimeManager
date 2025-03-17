package com.kt.worktimetrackermanager.domain.use_case.user

import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.data.remote.repositories.IUserRepo
import com.skydoves.sandwich.ApiResponse

class GetUserInTeam(
    private val iUserRepo: IUserRepo
) {
    suspend operator fun invoke(
        token: String,
        pageNumber: Int,
        pageSize: Int,
        username: String,
        role: Int? = null,
        employeeType: Int? = null,
        active: Boolean = true,
        teamId: Int?
    ): ApiResponse<PagedDataResponse<List<User>>> {
        return iUserRepo.getUsersInTeam(
            token,
            pageNumber,
            pageSize,
            username,
            role,
            employeeType,
            active,
            teamId
        )
    }
}