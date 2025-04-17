package com.kt.worktimetrackermanager.data.remote.dto.response

import com.kt.worktimetrackermanager.data.remote.dto.enums.ShiftType
import java.time.LocalDateTime

data class Shift(
    val id: Int,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val checkIn: LocalDateTime? = null,
    val checkOut: LocalDateTime? = null,
    val workDuration: Float,
    val shiftType: ShiftType,
    val user: User?,
)
