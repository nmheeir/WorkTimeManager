package com.kt.worktimetrackermanager.data.remote.repositories.impl

import com.kt.worktimetrackermanager.data.remote.api.AuthApi
import com.kt.worktimetrackermanager.data.remote.dto.request.UserLoginRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Token
import com.kt.worktimetrackermanager.data.remote.repositories.IAuthRepo
import com.skydoves.sandwich.ApiResponse

class AuthRepo(
    private val authApi: AuthApi
) : IAuthRepo {
    override suspend fun login(
        username: String,
        password: String,
        deviceToken: String
    ): ApiResponse<DataResponse<Token>> {
        return authApi.login(UserLoginRequest(username, password, deviceToken))
    }

    override suspend fun requestPasswordReset(email: String): ApiResponse<DataResponse<Unit>> {
        return authApi.requestPasswordReset(email)
    }

    override suspend fun resetPassword(
        token: String,
        newPassword: String
    ): ApiResponse<DataResponse<Unit>> {
        return authApi.resetPassword(token, newPassword)
    }
}