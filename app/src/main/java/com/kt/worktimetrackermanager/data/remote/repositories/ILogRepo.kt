package com.kt.worktimetrackermanager.data.remote.repositories

import com.kt.worktimetrackermanager.data.remote.dto.enums.CheckType
import com.kt.worktimetrackermanager.data.remote.dto.enums.LogStatus
import com.kt.worktimetrackermanager.data.remote.dto.response.LogModel
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.skydoves.sandwich.ApiResponse

interface ILogRepo {
    suspend fun getMyTeamLogs(
        token: String,
        teamId: Int?,
        pageNumber: Int,
        pageSize: Int,
        type: CheckType?,
        status: LogStatus?,
    ): ApiResponse<PagedDataResponse<List<LogModel>>>
}