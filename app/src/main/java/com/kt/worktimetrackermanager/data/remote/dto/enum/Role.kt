package com.kt.worktimetrackermanager.data.remote.dto.enum

enum class Role {
    MASTER, MANAGER, STAFF;

    companion object {
        fun fromInt(value: Int): Role {
            return entries.find { it.ordinal == value }
                ?: STAFF
        }
    }
}