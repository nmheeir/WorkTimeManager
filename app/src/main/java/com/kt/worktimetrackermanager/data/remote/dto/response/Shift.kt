package com.kt.worktimetrackermanager.data.remote.dto.response

import java.time.LocalDateTime

data class Shift(
    val id: Int,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val checkIn: LocalDateTime? = null,
    val checkOut: LocalDateTime? = null,
    val workDuration: Float,
    val user: User?,
)
