package com.kt.worktimetrackermanager.data.remote.api

import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Task
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface TaskApi {
    @GET("Task/{id}")
    suspend fun getTaskDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): ApiResponse<DataResponse<Task>>
}