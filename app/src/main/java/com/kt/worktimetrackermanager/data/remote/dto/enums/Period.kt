package com.kt.worktimetrackermanager.data.remote.dto.enums

enum class Period(val time: Long) {
    DAILY(86400),
    WEEKLY(604800),
    MONTHLY(2592000)
}