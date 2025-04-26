package com.kt.worktimetrackermanager.data.remote.repositories.impl

import com.kt.worktimetrackermanager.data.remote.api.TaskApi
import com.kt.worktimetrackermanager.data.remote.dto.request.CreateTaskDto
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Task
import com.kt.worktimetrackermanager.data.remote.repositories.ITaskRepo
import com.skydoves.sandwich.ApiResponse

class TaskRepo(
    private val taskApi: TaskApi,
) : ITaskRepo {
    override suspend fun getTaskDetail(
        token: String,
        id: Int,
    ): ApiResponse<DataResponse<Task>> {
        return taskApi.getTaskDetail("Bearer $token", id)
    }

    override suspend fun createTask(
        token: String,
        createTaskDto: CreateTaskDto,
    ) = taskApi.createTask(token, createTaskDto)
}