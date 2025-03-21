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
import com.skydoves.sandwich.retrofit.statusCode
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
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
) : ViewModel() {

    val uiState = MutableStateFlow<OverviewDashboardUiState>(OverviewDashboardUiState())

    private val _channel = Channel<CompanyDashboardUiEvent>()
    val channel = _channel.receiveAsFlow()


    init {
        fetchTeamInCompany()
        viewModelScope.launch {
//            delay(2000)

            uiState.update {
                it.copy(
                    attendanceRecord = AttendanceRecord(
                        1, 123, 456, 789,
                        LocalDate.now().atStartOfDay(),
                        LocalDate.now().atTime(23,59, 59)
                    )
                )
            }
        }
    }

    fun onAction(action: CompanyDashboardUiAction) {
        when (action) {
            CompanyDashboardUiAction.FetchTeamStatistic -> TODO()
            is CompanyDashboardUiAction.OnEndDateChange -> updateEndDate(action.date)
            is CompanyDashboardUiAction.OnPeriodChange -> TODO()
            is CompanyDashboardUiAction.OnStartDateChange -> updateStartDate(action.date)
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

    private fun fetchTeamStatistics() {

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

    data class OnStartDateChange(val date: LocalDate) : CompanyDashboardUiAction
    data class OnEndDateChange(val date: LocalDate) : CompanyDashboardUiAction

    data class OnPeriodChange(val period: Period) : CompanyDashboardUiAction
}

data class OverviewDashboardUiState(
    val start: LocalDate = LocalDate.now().withDayOfMonth(1),
    val end: LocalDate = YearMonth.now().atEndOfMonth(),

    val attendanceRecord: AttendanceRecord? = null,

    val teamStatistic: TeamStatistic? = null,

    val newHireEmployee: List<User> = emptyList(),
    val teams: List<Team> = emptyList(),
    val companyAttendanceRecord: List<AttendanceRecord> = emptyList(),
)