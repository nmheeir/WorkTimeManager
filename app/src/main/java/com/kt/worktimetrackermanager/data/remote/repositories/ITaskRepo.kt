package com.kt.worktimetrackermanager.data.remote.repositories

import com.kt.worktimetrackermanager.data.remote.dto.request.CreateTaskDto
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Task
import com.skydoves.sandwich.ApiResponse

interface ITaskRepo {
    suspend fun getTaskDetail(
        token: String,
        id: Int,
    ): ApiResponse<DataResponse<Task>>

    suspend fun createTask(
        token: String,
        createTaskDto: CreateTaskDto,
    ): ApiResponse<DataResponse<Task>>
}