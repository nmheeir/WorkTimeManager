package com.kt.worktimetrackermanager.data.remote.dto.response

data class TeamShiftStats(
    val team: TeamInformation,
    val teamName: String,
    val totalShifts: Int,
    val normalShifts: Int,
    val overtimeShifts: Int,
    val nightShifts: Int,
)
