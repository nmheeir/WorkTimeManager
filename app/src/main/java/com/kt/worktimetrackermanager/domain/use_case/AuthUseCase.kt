package com.kt.worktimetrackermanager.domain.use_case

import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Token
import com.kt.worktimetrackermanager.data.remote.repositories.IAuthRepo
import com.skydoves.sandwich.ApiResponse

class AuthUseCase(
    val login: Login,
    val resetPassword: ResetPassword,
    val requestPasswordReset: RequestPasswordReset,
)

class Login(
    private val iAuthRepo: IAuthRepo,
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        deviceToken: String
    ): ApiResponse<DataResponse<Token>> {
        return iAuthRepo.login(username, password, deviceToken)
    }
}

class ResetPassword(
    private val iAuthRepo: IAuthRepo
) {
    suspend operator fun invoke(
        token: String,
        newPassword: String
    ): ApiResponse<DataResponse<Unit>> {
        return iAuthRepo.resetPassword(token, newPassword)
    }
}

class RequestPasswordReset(
    private val iAuthRepo: IAuthRepo
) {
    suspend operator fun invoke(
        email: String
    ): ApiResponse<DataResponse<Unit>> {
        return iAuthRepo.requestPasswordReset(email)
    }
}