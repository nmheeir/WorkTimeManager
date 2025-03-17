package com.kt.worktimetrackermanager.domain.use_case.user


import com.kt.worktimetrackermanager.data.remote.dto.request.UpdateUserRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.repositories.IUserRepo
import com.skydoves.sandwich.ApiResponse

class UpdateUser(private val iUserRepo: IUserRepo) {
    suspend operator fun invoke(token:String, user: UpdateUserRequest): ApiResponse<DataResponse<Any>> {
        return iUserRepo.updateUser(token, user);
    }
}