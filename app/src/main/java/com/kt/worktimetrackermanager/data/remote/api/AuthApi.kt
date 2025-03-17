package com.kt.worktimetrackermanager.data.remote.api

import com.kt.worktimetrackermanager.data.remote.dto.request.UserLoginRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Token
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("Auth/login")
    suspend fun login(
        @Body user: UserLoginRequest
    ): ApiResponse<DataResponse<Token>>

    @POST("Auth/requestPasswordReset")
    suspend fun requestPasswordReset(
        @Query("email") email: String
    ): ApiResponse<DataResponse<Unit>>

    @POST("Auth/resetPassword")
    suspend fun resetPassword(
        @Header("Authorization") token: String,
        @Query("newPassword") newPassword: String
    ): ApiResponse<DataResponse<Unit>>
}