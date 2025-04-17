package com.kt.worktimetrackermanager.data.remote.dto.request

import com.kt.worktimetrackermanager.data.remote.dto.enums.ShiftType
import java.time.LocalDateTime

data class AddShiftRequest(
    val start: LocalDateTime,
    val end: LocalDateTime,
    val shiftType: ShiftType,
    val userId: Int,
)
