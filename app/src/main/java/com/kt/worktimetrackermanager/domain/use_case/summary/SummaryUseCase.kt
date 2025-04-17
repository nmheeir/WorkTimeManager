package com.kt.worktimetrackermanager.domain.use_case.summary

import com.kt.worktimetrackermanager.data.remote.dto.response.AttendanceRecord
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.TeamShiftStats
import com.kt.worktimetrackermanager.data.remote.dto.response.TeamStatistic
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.data.remote.dto.response.WorkHours
import com.kt.worktimetrackermanager.data.remote.repositories.ISummaryRepo
import com.skydoves.sandwich.ApiResponse
import java.time.LocalDateTime

data class SummaryUseCase(
    val getEmployeeAttendanceRecord: GetEmployeeAttendanceRecord,
    val getTeamAttendanceRecord: GetTeamAttendanceRecord,
    val getCompanyAttendanceRecord: GetCompanyAttendanceRecord,
    val getTeamEmployeeWorkHours: GetTeamEmployeeWorkHours,
    val getTeamStatistic: GetTeamStatistic,
    val getNewHireEmployee: GetNewHireEmployee,
    val getCompanyAttendanceRecordEachTime: GetCompanyAttendanceRecordEachTime,
    val getTeamAttendanceRecordEachTime: GetTeamAttendanceRecordEachTime,
    val getEmployeeAttendanceRecordEachTime: GetEmployeeAttendanceRecordEachTime,
    val getTeamShiftsStat: GetTeamShiftsStat,
    val getTeamShiftsStatById: GetTeamShiftsStatById,
)

class GetEmployeeAttendanceRecord(
    private val summaryRepository: ISummaryRepo,
) {
    suspend operator fun invoke(
        start: LocalDateTime,
        end: LocalDateTime,
        userId: Int? = null,
        token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>> {
        return summaryRepository.getEmployeeAttendanceRecord(start, end, userId, "Bearer $token")
    }
}

class GetTeamAttendanceRecord(
    private val summaryRepository: ISummaryRepo,
) {
    suspend operator fun invoke(
        start: LocalDateTime,
        end: LocalDateTime,
        teamId: Int? = null,
        token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>> {
        return summaryRepository.getTeamAttendanceRecord(start, end, teamId, "Bearer $token")
    }
}

class GetCompanyAttendanceRecord(
    private val summaryRepository: ISummaryRepo,
) {
    suspend operator fun invoke(
        start: LocalDateTime,
        end: LocalDateTime,
        token: String,
    ): ApiResponse<DataResponse<AttendanceRecord>> {
        return summaryRepository.getCompanyAttendanceRecord(start, end, "Bearer $token")
    }
}

class GetTeamEmployeeWorkHours(
    private val summaryRepository: ISummaryRepo,
) {
    suspend operator fun invoke(
        start: LocalDateTime,
        end: LocalDateTime,
        teamId: Int,
        token: String,
    ): ApiResponse<DataResponse<List<WorkHours>>> {
        return summaryRepository.getTeamEmployeeWorkHours(start, end, teamId, token)
    }
}

class GetTeamStatistic(
    private val summaryRepository: ISummaryRepo,
) {
    suspend operator fun invoke(
        token: String,
    ): ApiResponse<DataResponse<TeamStatistic>> {
        return summaryRepository.getTeamStatistic(token)
    }
}

class GetNewHireEmployee(
    private val summaryRepository: ISummaryRepo,
) {
    suspend operator fun invoke(
        start: LocalDateTime,
        end: LocalDateTime,
        token: String,
    ): ApiResponse<DataResponse<List<User>>> {
        return summaryRepository.getNewHireEmployee(start, end, token)
    }
}

class GetCompanyAttendanceRecordEachTime(
    private val summaryRepository: ISummaryRepo,
) {
    suspend operator fun invoke(
        start: LocalDateTime,
        end: LocalDateTime,
        period: Long,
        token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>> {
        return summaryRepository.getCompanyAttendanceRecordEachTime(
            start, end, period, "Bearer $token"
        )
    }
}

class GetTeamAttendanceRecordEachTime(
    private val summaryRepository: ISummaryRepo,
) {
    suspend operator fun invoke(
        start: LocalDateTime,
        end: LocalDateTime,
        period: Long,
        teamId: Int? = null,
        token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>> {
        return summaryRepository.getTeamAttendanceRecordEachTime(
            start, end, period, teamId, "Bearer $token"
        )
    }
}

class GetEmployeeAttendanceRecordEachTime(
    private val summaryRepository: ISummaryRepo,
) {
    suspend operator fun invoke(
        start: LocalDateTime,
        end: LocalDateTime,
        period: Long,
        userId: Int? = null,
        token: String,
    ): ApiResponse<DataResponse<List<AttendanceRecord>>> {
        return summaryRepository.getEmployeeAttendanceRecordEachTime(
            start, end, period, userId, "Bearer $token"
        )
    }
}

class GetTeamShiftsStat(
    private val summaryRepository: ISummaryRepo,
) {
    suspend operator fun invoke(
        token: String,
        startDate: LocalDateTime? = null,
        endDate: LocalDateTime? = null,
    ) = summaryRepository.getShiftsStatByTeam(token, startDate, endDate)
}

class GetTeamShiftsStatById(
    private val summaryRepository: ISummaryRepo,
) {
    suspend operator fun invoke(
        token: String,
        start: LocalDateTime? = null,
        endDate: LocalDateTime? = null,
        teamId: Int,
    ) = summaryRepository.getShiftsStatByTeamId(token, teamId, start, endDate)
}
