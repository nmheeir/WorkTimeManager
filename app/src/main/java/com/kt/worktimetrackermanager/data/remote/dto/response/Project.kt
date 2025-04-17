package com.kt.worktimetrackermanager.data.remote.dto.response

import com.kt.worktimetrackermanager.data.remote.dto.enums.ProjectStatus
import java.time.LocalDateTime

data class Project(
    val createdAt: LocalDateTime,
    val description: String,
    val endDate: LocalDateTime?,
    val id: Int,
    val managerId: Int,
    val name: String,
    val startDate: LocalDateTime,
    val status: ProjectStatus,
    val updatedAt: LocalDateTime,
    val statistics: TaskStatistics,
)