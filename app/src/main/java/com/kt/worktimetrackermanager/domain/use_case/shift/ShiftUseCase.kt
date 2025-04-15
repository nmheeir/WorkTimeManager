package com.kt.worktimetrackermanager.domain.use_case.shift

import com.kt.worktimetrackermanager.data.remote.dto.request.AddShiftRequest
import com.kt.worktimetrackermanager.data.remote.repositories.IShiftRepo
import java.time.LocalDateTime

data class ShiftUseCase(
    val addShift: AddShift,
    val getShiftsInTeam: GetShiftsInTeam,
)

class AddShift(
    private val shiftRepo: IShiftRepo,
) {
    suspend operator fun invoke(
        token: String,
        shifts: List<AddShiftRequest>,
    ) = shiftRepo.addShift(token, shifts)
}

class GetShiftsInTeam(
    private val shiftRepo: IShiftRepo,
) {
    suspend operator fun invoke(
        token: String,
        teamId: Int? = null,
        pageNumber: Int = 1,
        pageSize: Int = 10,
        start: LocalDateTime? = null,
        end: LocalDateTime? = null,
    ) = shiftRepo.getShiftsInTeam(token, teamId, pageNumber, pageSize, start, end)
}