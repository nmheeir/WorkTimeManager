package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.enum.Period
import com.kt.worktimetrackermanager.data.remote.dto.response.AttendanceRecord
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.TeamStatistic
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.summary.SummaryUseCase
import com.kt.worktimetrackermanager.domain.use_case.team.TeamUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class TeamDashboardViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val teamUseCase: TeamUseCase,
    private val summaryUseCase: SummaryUseCase,
    private val userUseCase: UserUseCase,
) : ViewModel() {

    val teamId = savedStateHandle.get<Int>("id")
    private val token = context.dataStore[TokenKey]!!

    val uiState = MutableStateFlow(TeamDashboardUiState())


    init {
        fetchTeamDetail()
        fetchData()
    }

    fun onAction(action: TeamDashboardUiAction) {
        when (action) {
            TeamDashboardUiAction.FetchTeamStatistic -> {
                fetchTeamStatistic()
            }

            TeamDashboardUiAction.FetchData -> {
                fetchData()
            }

            is TeamDashboardUiAction.OnEndDateChange -> {
                updateEndDate(action.date)
            }

            is TeamDashboardUiAction.OnPeriodChange -> {
                periodChange(action.period)
            }

            is TeamDashboardUiAction.OnStartDateChange -> {
                updateStartDate(action.date)
            }
        }
    }

    private fun fetchData() {
        fetchStaffInTeam()
        fetchTeamAttendanceRecord()
        fetchTeamAttendanceRecordEachTime()
    }

    private fun fetchTeamStatistic() {
        viewModelScope.launch {
            // TODO: Làm sau
        }
    }

    private fun updateStartDate(date: LocalDate) {
        uiState.update {
            it.copy(start = date)
        }
    }

    private fun updateEndDate(date: LocalDate) {
        uiState.update {
            it.copy(end = date)
        }
    }

    private fun periodChange(period: Period) {
        Timber.d(period.name)
        uiState.update {
            it.copy(
                period = period
            )
        }
        fetchTeamAttendanceRecordEachTime()
    }


    private fun fetchTeamDetail() {
        viewModelScope.launch {
            teamUseCase.getCompanyTeamById(
                token = token,
                id = teamId
            )
                .suspendOnSuccess {
                    uiState.update {
                        it.copy(
                            teamDetail = this.data.data!!
                        )
                    }
                    Timber.d("Success: %s", this.data.data!!)
                }
                .suspendOnFailure {
                    Timber.d("Failure: %s", this.message())
                }
                .suspendOnError {
                    Timber.d("Error: %s", this.message())
                }
        }
    }

    private fun fetchTeamAttendanceRecord() {
        viewModelScope.launch {
            summaryUseCase.getTeamAttendanceRecord(
                token = token,
                start = uiState.value.start.atStartOfDay(),
                end = uiState.value.end.atTime(23, 59, 59),
                teamId = teamId
            ).suspendOnSuccess {
                uiState.update {
                    it.copy(
                        attendanceRecord = this.data.data!!
                    )
                }
                Timber.d("Success: %s", this.data.data!!)
            }
                .suspendOnFailure {
                    Timber.d("Failure: %s", this.message())
                }
                .suspendOnError {
                    Timber.d("Error: %s", this.message())
                }
        }
    }

    private fun fetchStaffInTeam() {
        viewModelScope.launch {
            userUseCase.getUsers(
                token = token,
                teamId = teamId,
//                role = Role.UNAUTHORIZED.ordinal
            )
                .suspendOnSuccess {
                    uiState.update {
                        it.copy(
                            staffs = this.data.data!!
                        )
                    }
                    Timber.d("Success: %s", this.data.data.toString())
                }
                .suspendOnFailure {
                    Timber.d("Failure: %s", this.message())
                }
                .suspendOnError {
                    Timber.d("Error: %s", this.message())
                }
        }
    }

    private fun fetchTeamAttendanceRecordEachTime() {
        viewModelScope.launch {
            summaryUseCase.getTeamAttendanceRecordEachTime(
                start = uiState.value.start.atStartOfDay(),
                end = uiState.value.end.atTime(23, 59, 59),
                period = uiState.value.period.time,
                teamId = teamId,
                token = token
            )
                .suspendOnSuccess {
                    uiState.update {
                        it.copy(
                            teamAttendanceRecordEachTime = this.data.data ?: emptyList()
                        )
                    }
                    Timber.d("Success: %s", this.data.data.toString())
                }
                .suspendOnFailure {
                    Timber.d("Failure: %s", this.message())
                }
                .suspendOnError {
                    Timber.d("Error: %s", this.message())
                }
        }
    }

}


sealed interface TeamDashboardUiAction {
    data object FetchTeamStatistic : TeamDashboardUiAction
    data object FetchData : TeamDashboardUiAction

    data class OnStartDateChange(val date: LocalDate) : TeamDashboardUiAction
    data class OnEndDateChange(val date: LocalDate) : TeamDashboardUiAction

    data class OnPeriodChange(val period: Period) : TeamDashboardUiAction
}

data class TeamDashboardUiState(
    val start: LocalDate = LocalDate.now().withDayOfMonth(1),
    val end: LocalDate = YearMonth.now().atEndOfMonth(),

    val period: Period = Period.DAILY,

    val attendanceRecord: AttendanceRecord? = null,

    val teamStatistic: TeamStatistic? = null,

    val newHireEmployee: List<User> = emptyList(),
    val staffs: List<User> = emptyList(),

    val teamAttendanceRecordEachTime: List<AttendanceRecord> = emptyList(),

    val teamDetail: Team? = null,
)