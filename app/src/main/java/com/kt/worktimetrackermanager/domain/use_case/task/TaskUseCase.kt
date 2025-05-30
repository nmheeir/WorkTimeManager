package com.kt.worktimetrackermanager.domain.use_case.task

import com.kt.worktimetrackermanager.data.remote.dto.request.CreateTaskDto
import com.kt.worktimetrackermanager.data.remote.repositories.ITaskRepo

data class TaskUseCase(
    val getTaskDetail: GetTaskDetail,
    val createTask: CreateTask,
)

class GetTaskDetail(
    private val iTaskRepo: ITaskRepo,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
    ) = iTaskRepo.getTaskDetail(token, id)
}

class CreateTask(
    private val iTaskRepo: ITaskRepo,
) {
    suspend operator fun invoke(
        token: String,
        createTaskDto: CreateTaskDto,
    ) = iTaskRepo.createTask("Bearer $token", createTaskDto)
}