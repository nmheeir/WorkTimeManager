package com.kt.worktimetrackermanager.domain.use_case

import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Token
import com.kt.worktimetrackermanager.data.remote.repositories.IAuthRepo
import com.skydoves.sandwich.ApiResponse

class AuthUseCase(
    val login: Login
)

class Login(
    private val iAuthRepo: IAuthRepo
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        deviceToken: String
    ): ApiResponse<DataResponse<Token>> {
        return iAuthRepo.login(username, password, deviceToken)
    }
}