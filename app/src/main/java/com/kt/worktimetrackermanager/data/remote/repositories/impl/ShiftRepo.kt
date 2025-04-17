package com.kt.worktimetrackermanager.data.remote.repositories.impl

import com.kt.worktimetrackermanager.data.remote.api.ShiftApi
import com.kt.worktimetrackermanager.data.remote.dto.request.AddShiftRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Shift
import com.kt.worktimetrackermanager.data.remote.repositories.IShiftRepo
import com.skydoves.sandwich.ApiResponse
import java.time.LocalDateTime

class ShiftRepo(
    private val shiftApi: ShiftApi,
) : IShiftRepo {
    override suspend fun addShift(
        token: String,
        shifts: List<AddShiftRequest>,
    ): ApiResponse<DataResponse<Unit>> = shiftApi.addShift("Bearer $token", shifts)

    override suspend fun getShiftsInTeam(
        token: String,
        teamId: Int?,
        pageNumber: Int,
        pageSize: Int,
        start: LocalDateTime?,
        end: LocalDateTime?,
    ): ApiResponse<PagedDataResponse<List<Shift>>> = shiftApi.getShiftsInTeam(
        token = "Bearer $token",
        teamId = teamId,
        pageNumber = pageNumber,
        pageSize = pageSize,
        start = start,
        end = end
    )
}