package com.kt.worktimetrackermanager.data.remote.dto.enums

enum class NotificationType {
    AddLog,
    UpdateProfile,
    ApprovedLog,
    DeniedLog,
    UpdateShift;

    companion object {
        fun fromString(value: String): NotificationType? {
            return when (value) {
                "AddLog" -> AddLog
                "UpdateProfile" -> UpdateProfile
                "ApprovedLog" -> ApprovedLog
                "DeniedLog" -> DeniedLog
                "UpdateShift" -> UpdateShift
                else -> null
            }
        }
    }
}
