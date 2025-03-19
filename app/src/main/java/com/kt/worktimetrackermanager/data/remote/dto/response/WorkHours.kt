package com.kt.worktimetrackermanager.data.remote.dto.response

data class WorkHours(
    val id: Int,
    val username: String,
    val nightHours: Double,
    val normalHours: Double,
    val overtimeHours: Double,
    val totalHours: Double
)