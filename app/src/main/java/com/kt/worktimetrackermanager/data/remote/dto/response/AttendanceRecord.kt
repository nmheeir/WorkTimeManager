package com.kt.worktimetrackermanager.data.remote.dto.response

import java.time.LocalDateTime

data class AttendanceRecord(
    val id: Int,
    val fullAttendance: Int,
    val partialAttendance: Int,
    val absenceAttendance: Int,
    val start: LocalDateTime,
    val end: LocalDateTime,
) {
    companion object {
        fun AttendanceRecord.isNoneData(): Boolean {
            return this.fullAttendance == 0 && this.partialAttendance == 0 && this.absenceAttendance == 0
        }
    }
}