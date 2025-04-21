package com.kt.worktimetrackermanager.domain.use_case.log

import com.kt.worktimetrackermanager.data.remote.dto.enums.CheckType
import com.kt.worktimetrackermanager.data.remote.dto.enums.LogStatus
import com.kt.worktimetrackermanager.data.remote.repositories.ILogRepo

data class LogUseCase(
    val getTeamLogs: GetTeamLogs,
)


class GetTeamLogs(
    private val iLogRepo: ILogRepo,
) {
    suspend operator fun invoke(
        token: String,
        teamId: Int?,
        pageNumber: Int,
        pageSize: Int,
        type: CheckType? = null,
        status: LogStatus? = null,
    ) =
        iLogRepo.getMyTeamLogs(
            token = token,
            teamId = teamId,
            pageNumber = pageNumber,
            pageSize = pageSize,
            type = type,
            status = status,
        )
}