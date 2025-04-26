package com.kt.worktimetrackermanager.data.remote.dto.request

import com.kt.worktimetrackermanager.data.remote.dto.enums.Priority
import java.time.LocalDateTime

data class CreateTaskDto(
    val projectId: Int,
    val name: String,
    val description: String,
    val priority: Priority,
    val assigneeIds: List<Int>,
    val dueDate: LocalDateTime,
)
