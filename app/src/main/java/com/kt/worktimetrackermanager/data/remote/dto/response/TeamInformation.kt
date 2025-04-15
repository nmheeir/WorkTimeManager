package com.kt.worktimetrackermanager.data.remote.dto.response

import java.time.LocalDateTime

data class TeamInformation(
    val id: Int,
    val companyId: Int,
    val name: String,
    val createdAt: LocalDateTime,
    val latitude: Float,
    val longitude: Float,
    val description: String,
)
