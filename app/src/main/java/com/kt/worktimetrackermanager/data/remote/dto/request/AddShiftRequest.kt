package com.kt.worktimetrackermanager.data.remote.dto.request

data class AddShiftRequest(
    val start: Long,
    val end: Long,
    val shiftType: Int,
    val userId: Int
)
