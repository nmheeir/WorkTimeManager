package com.kt.worktimetrackermanager.data.remote.repositories

import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Token
import com.skydoves.sandwich.ApiResponse

interface IAuthRepo {
    suspend fun login(
        username: String,
        password: String,
        deviceToken: String
    ): ApiResponse<DataResponse<Token>>

    suspend fun requestPasswordReset(
        email: String
    ): ApiResponse<DataResponse<Unit>>

    suspend fun resetPassword(
        token: String,
        newPassword: String
    ): ApiResponse<DataResponse<Unit>>
}