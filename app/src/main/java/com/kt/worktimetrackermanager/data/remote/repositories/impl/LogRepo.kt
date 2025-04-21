package com.kt.worktimetrackermanager.data.remote.repositories.impl

import com.kt.worktimetrackermanager.data.remote.api.LogApi
import com.kt.worktimetrackermanager.data.remote.dto.enums.CheckType
import com.kt.worktimetrackermanager.data.remote.dto.enums.LogStatus
import com.kt.worktimetrackermanager.data.remote.dto.response.LogModel
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.repositories.ILogRepo
import com.skydoves.sandwich.ApiResponse

class LogRepo(
    private val logApi: LogApi,
) : ILogRepo {
    override suspend fun getMyTeamLogs(
        token: String,
        teamId: Int?,
        pageNumber: Int,
        pageSize: Int,
        type: CheckType?,
        status: LogStatus?,
    ): ApiResponse<PagedDataResponse<List<LogModel>>> = logApi.getMyTeamLogs(
        token = "Bearer $token",
        teamId = teamId,
        pageNumber = pageNumber,
        pageSize = pageSize,
        type = type,
        status = status,
    )
}