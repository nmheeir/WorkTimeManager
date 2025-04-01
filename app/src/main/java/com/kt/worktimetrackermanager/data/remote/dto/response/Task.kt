package com.kt.worktimetrackermanager.data.remote.dto.response

import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus

data class Task(
    val assigneeId: Int,
    val createdAt: String,
    val description: String,
    val dueDate: String,
    val id: Int,
    val name: String,
    val projectId: Int,
    val status: ProjectStatus,
)