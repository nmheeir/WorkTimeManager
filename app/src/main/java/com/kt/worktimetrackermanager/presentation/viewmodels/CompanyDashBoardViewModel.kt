package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
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
import com.skydoves.sandwich.retrofit.statusCode
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CompanyDashBoardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val summaryUseCase: SummaryUseCase,
    private val teamUseCase: TeamUseCase,
    private val userUseCase: UserUseCase,
) : ViewModel() {

    val uiState = MutableStateFlow<CompanyDashboardUiState>(CompanyDashboardUiState())

    private val _channel = Channel<CompanyDashboardUiEvent>()
    val channel = _channel.receiveAsFlow()

    private val token = context.dataStore[TokenKey]!!

    init {
        Timber.d("init view model")
        fetchData()
    }

    private fun fetchData() {
        fetchTeamInCompany()
        fetchCompanyAttendanceRecord()
        fetchCompanyAttendanceRecordEachTime()
    }

    fun onAction(action: CompanyDashboardUiAction) {
        when (action) {
            CompanyDashboardUiAction.FetchTeamStatistic -> {

            }

            CompanyDashboardUiAction.FetchData -> {
                fetchData()
            }

            is CompanyDashboardUiAction.OnEndDateChange -> {
                updateEndDate(action.date)
            }

            is CompanyDashboardUiAction.OnPeriodChange -> {
                periodChange(action.period)
            }

            is CompanyDashboardUiAction.OnStartDateChange -> {
                updateStartDate(action.date)
            }

            is CompanyDashboardUiAction.FetchUserInTeam -> {
                fetchUserInTeam(action.teamId)
            }
        }
    }

    private fun periodChange(period: Period) {
        Timber.d(period.name)
        uiState.update {
            it.copy(
                period = period
            )
        }
        fetchCompanyAttendanceRecordEachTime()
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

    private fun fetchCompanyAttendanceRecord() {
        viewModelScope.launch {
            summaryUseCase.getCompanyAttendanceRecord(
                token = token,
                start = uiState.value.start.atStartOfDay(),
                end = uiState.value.end.atTime(23, 59, 59)
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

    private fun fetchCompanyAttendanceRecordEachTime() {
        viewModelScope.launch {
            summaryUseCase.getCompanyAttendanceRecordEachTime(
                token = token,
                start = uiState.value.start.atStartOfDay(),
                end = uiState.value.end.atTime(23, 59, 59),
                period = uiState.value.period.time
            )
                .suspendOnSuccess {
                    uiState.update {
                        it.copy(
                            companyAttendanceRecord = this.data.data ?: emptyList()
                        )
                    }
                    Timber.d(uiState.value.toString())
                    Timber.d("Success: %s", this.data.data)
                }
                .suspendOnFailure {
                    Timber.d("Failure: %s", this.message())
                }
                .suspendOnError {
                    Timber.d("Error: %s", this.message())
                }
        }
    }

    private fun fetchTeamInCompany() {
        val token = context.dataStore[TokenKey]!!
        viewModelScope.launch {
            teamUseCase.getCompanyTeams(token = token)
                .suspendOnSuccess {
                    uiState.update {
                        it.copy(
                            teams = this.data.data!!
                        )
                    }
                    Timber.d("Success: %s", this.data.data!!)
                }
                .suspendOnError {
                    Timber.d("error: ${this.statusCode}")
                }
                .suspendOnException {
                    Timber.d("exception: %s", this.throwable.toString())
                }
        }
    }

    private fun fetchUserInTeam(teamId: Int) {
        viewModelScope.launch {
            userUseCase.getUsers(
                token = token,
                teamId = teamId,
            )
                .suspendOnSuccess {
                    uiState.update {
                        it.copy(
                            usersInCompany = teamId to this.data.data!!
                        )
                    }
                    Timber.d("Success: %s", this.data.data!!)
                }
                .suspendOnError {
                    Timber.d("error: ${this.statusCode}")
                }
                .suspendOnException {
                    Timber.d("exception: %s", this.throwable.toString())
                }
        }
    }
}

sealed interface CompanyDashboardUiEvent {

}

sealed interface CompanyDashboardUiAction {
    data object FetchTeamStatistic : CompanyDashboardUiAction
    data object FetchData : CompanyDashboardUiAction

    data class OnStartDateChange(val date: LocalDate) : CompanyDashboardUiAction
    data class OnEndDateChange(val date: LocalDate) : CompanyDashboardUiAction

    data class OnPeriodChange(val period: Period) : CompanyDashboardUiAction
    data class FetchUserInTeam(val teamId: Int) : CompanyDashboardUiAction
}

data class CompanyDashboardUiState(
    val start: LocalDate = LocalDate.now().withDayOfMonth(1),
    val end: LocalDate = YearMonth.now().atEndOfMonth(),

    val period: Period = Period.DAILY,

    val attendanceRecord: AttendanceRecord? = null,

    val teamStatistic: TeamStatistic? = null,

    val newHireEmployee: List<User> = emptyList(),
    val teams: List<Team> = emptyList(),
    val companyAttendanceRecord: List<AttendanceRecord> = emptyList(),
    val usersInCompany: Pair<Int, List<User>> = Pair(0, emptyList()),
)