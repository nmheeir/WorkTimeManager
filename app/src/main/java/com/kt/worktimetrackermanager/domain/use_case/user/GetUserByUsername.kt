package com.kt.worktimetrackermanager.domain.use_case.user

import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.data.remote.repositories.IUserRepo
import com.skydoves.sandwich.ApiResponse

class GetUserByUsername(
    private val iUserRepo: IUserRepo
) {
    suspend operator fun invoke(username: String): ApiResponse<DataResponse<User>> {
        return iUserRepo.getUserByUsername(username)
    }
}