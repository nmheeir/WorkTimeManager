package com.kt.worktimetrackermanager.domain.use_case.user

import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.UsersStatistic
import com.kt.worktimetrackermanager.data.remote.repositories.IUserRepo
import com.skydoves.sandwich.ApiResponse

class GetUsersStatistic(
    private val iUserRepo: IUserRepo
) {
    suspend operator fun invoke(
        token: String
    ) : ApiResponse<DataResponse<UsersStatistic>> {
        return iUserRepo.getUsersStatistic(token)
    }
}