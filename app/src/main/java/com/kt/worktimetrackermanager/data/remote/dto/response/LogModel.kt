package com.kt.worktimetrackermanager.data.remote.dto.response

import com.kt.worktimetrackermanager.data.remote.dto.enums.CheckType
import com.kt.worktimetrackermanager.data.remote.dto.enums.LogStatus
import java.time.LocalDateTime

data class LogModel(
    val id: Int,
    val userId: Int,
    val type: CheckType,
    val status: LogStatus,
    val reason: String,
    val createAt: LocalDateTime,
    val checkTime: LocalDateTime,
    val approvalTime: LocalDateTime?,
    val user: User,
)
