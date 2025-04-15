package com.kt.worktimetrackermanager.data.remote.api

import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus
import com.kt.worktimetrackermanager.data.remote.dto.request.CreateProjectRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Project
import com.kt.worktimetrackermanager.data.remote.dto.response.Task
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectApi {
    @GET("Project/projects")
    suspend fun getProjects(
        @Header("Authorization") token: String,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("filter") filter: ProjectStatus?,
    ): ApiResponse<PagedDataResponse<List<Project>>>

    @GET("Project/{id}")
    suspend fun getProject(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): ApiResponse<DataResponse<Project>>

    @GET("Project/projects/{id}/tasks")
    suspend fun getTasksFromProject(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("filter") filter: ProjectStatus?,
    ): ApiResponse<PagedDataResponse<List<Task>>>

    @POST("Project/create")
    suspend fun createProject(
        @Header("Authorization") token: String,
        @Body project: CreateProjectRequest,
    ): ApiResponse<DataResponse<Project>>
}