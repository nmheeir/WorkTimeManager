package com.kt.worktimetrackermanager.data.remote.dto.request

import java.time.LocalDateTime

data class CreateProjectRequest(
    val name: String,
    val description: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
)
