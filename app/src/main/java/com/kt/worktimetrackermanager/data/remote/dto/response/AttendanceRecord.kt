package com.kt.worktimetrackermanager.data.remote.dto.response

data class AttendanceRecord(
    val absenceAttendance: Int,
    val fullAttendance: Int,
    val id: Int,
    val partialAttendance: Int,
    val start: Long,
    val end: Long
)