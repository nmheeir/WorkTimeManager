package com.kt.worktimetrackermanager.data.remote.repositories

import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus
import com.kt.worktimetrackermanager.data.remote.dto.request.CreateProjectRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Project
import com.kt.worktimetrackermanager.data.remote.dto.response.Task
import com.skydoves.sandwich.ApiResponse

interface IProjectRepo {
    suspend fun getProjects(
        token: String,
    ): ApiResponse<DataResponse<List<Project>>>

    suspend fun getProject(
        token: String,
        id: Int,
    ): ApiResponse<DataResponse<Project>>

    suspend fun getTasksFromProject(
        token: String,
        id: Int,
        pageNumber: Int,
        pageSize: Int,
        filter: ProjectStatus? = null,
    ): ApiResponse<DataResponse<List<Task>>>

    suspend fun createProject(
        token: String,
        project: CreateProjectRequest,
    ): ApiResponse<DataResponse<Project>>
}