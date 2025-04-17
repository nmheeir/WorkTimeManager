package com.kt.worktimetrackermanager.data.remote.repositories

import com.kt.worktimetrackermanager.data.remote.dto.request.AddShiftRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.Shift
import com.skydoves.sandwich.ApiResponse
import java.time.LocalDateTime

interface IShiftRepo {
    suspend fun addShift(
        token: String,
        shifts: List<AddShiftRequest>,
    ): ApiResponse<DataResponse<Unit>>

    suspend fun getShiftsInTeam(
        token: String,
        teamId: Int? = null,
        pageNumber: Int = 1,
        pageSize: Int = 10,
        start: LocalDateTime? = null,
        end: LocalDateTime? = null,
    ): ApiResponse<PagedDataResponse<List<Shift>>>
}