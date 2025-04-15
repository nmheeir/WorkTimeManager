package com.kt.worktimetrackermanager.data.remote.dto.enum

enum class Role {
    Master, Manager, Staff;

    companion object {
        fun fromInt(value: Int): Role {
            return entries.find { it.ordinal == value }
                ?: Staff
        }
        fun fromIntToName(value: Int): String? {
            return Role.entries.find { it.ordinal == value }?.name
                ?.replace("_", " ")
                ?.split(" ")
                ?.joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
        }
    }
}