package com.kt.worktimetrackermanager.domain.use_case.user

import com.kt.worktimetrackermanager.data.remote.dto.request.AddUserRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.repositories.IUserRepo
import com.skydoves.sandwich.ApiResponse

class AddUser(private val iUserRepo: IUserRepo) {
    suspend operator fun invoke(token: String, user: AddUserRequest): ApiResponse<DataResponse<Any>> {
        return iUserRepo.addUser(token, user);
    }
}