package com.kt.worktimetrackermanager.data.remote.dto.response

import java.time.LocalDateTime

data class Report(
    val id: Int,
    val title: String,
    val description: String,
    val taskId: Int,
    val authorId: Int,
    val reportUrl: String? = null,
    val createdAt: LocalDateTime
)
