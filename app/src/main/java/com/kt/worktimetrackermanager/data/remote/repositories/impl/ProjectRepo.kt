package com.kt.worktimetrackermanager.data.remote.repositories.impl

import com.kt.worktimetrackermanager.data.remote.api.ProjectApi
import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus
import com.kt.worktimetrackermanager.data.remote.dto.request.CreateProjectRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Project
import com.kt.worktimetrackermanager.data.remote.dto.response.Task
import com.kt.worktimetrackermanager.data.remote.repositories.IProjectRepo
import com.skydoves.sandwich.ApiResponse

class ProjectRepo(
    private val projectApi: ProjectApi,
) : IProjectRepo {
    override suspend fun getProjects(token: String): ApiResponse<DataResponse<List<Project>>> {
        return projectApi.getProjects("Bearer $token")
    }

    override suspend fun getProject(
        token: String,
        id: Int,
    ): ApiResponse<DataResponse<Project>> {
        return projectApi.getProject("Bearer $token", id)
    }

    override suspend fun getTasksFromProject(
        token: String,
        id: Int,
        pageNumber: Int,
        pageSize: Int,
        filter: ProjectStatus?,
    ): ApiResponse<DataResponse<List<Task>>> {
        return projectApi.getTasksFromProject("Bearer $token", id, pageNumber, pageSize, filter)
    }

    override suspend fun createProject(
        token: String,
        project: CreateProjectRequest,
    ): ApiResponse<DataResponse<Project>> {
        return projectApi.createProject("Bearer $token", project)
    }
}