package com.kt.worktimetrackermanager.data.remote.api

import com.kt.worktimetrackermanager.data.remote.dto.request.CreateProjectRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Project
import com.kt.worktimetrackermanager.data.remote.dto.response.Task
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ProjectApi {
    @GET("Project/projects")
    suspend fun getProjects(
        @Header("Authorization") token: String,
    ): ApiResponse<DataResponse<List<Project>>>

    @GET("Project/projects/{id}/task")
    suspend fun getTasksFromProject(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): ApiResponse<DataResponse<List<Task>>>

    @POST("Project/create")
    suspend fun createProject(
        @Header("Authorization") token: String,
        @Body project: CreateProjectRequest,
    ): ApiResponse<DataResponse<Project>>
}