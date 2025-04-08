package com.kt.worktimetrackermanager.domain.use_case.task

import com.kt.worktimetrackermanager.data.remote.repositories.ITaskRepo

data class TaskUseCase(
    val getTaskDetail: GetTaskDetail
)

class GetTaskDetail(
    private val iTaskRepo: ITaskRepo,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
    ) = iTaskRepo.getTaskDetail(token, id)
}