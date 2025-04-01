package com.kt.worktimetrackermanager.data.remote.dto.response

import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus
import java.time.LocalDateTime

data class Project(
    val createdAt: String,
    val description: String,
    val endDate: LocalDateTime,
    val id: Int,
    val managerId: Int,
    val name: String,
    val startDate: LocalDateTime,
    val status: ProjectStatus,
    val tasks: List<Task>,
    val updatedAt: LocalDateTime
)