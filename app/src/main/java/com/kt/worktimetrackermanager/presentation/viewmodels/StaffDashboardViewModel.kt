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
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.summary.GetTeamAttendanceRecordEachTime
import com.kt.worktimetrackermanager.domain.use_case.summary.SummaryUseCase
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
import okhttp3.internal.userAgent
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class StaffDashboardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val summaryUseCase: SummaryUseCase,
    private val userUseCase: UserUseCase,
) : ViewModel() {

    private val token = context.dataStore[TokenKey]!!
    private val staffId = savedStateHandle.get<Int>("id")!!

    val uiState = MutableStateFlow(StaffDashboardUiState())

    init {
        fetchStaffDetail()
        fetchData()
    }

    fun onAction(action: StaffDashboardUiAction) {
        when (action) {
            StaffDashboardUiAction.FetchStaffStatistic -> {

            }

            StaffDashboardUiAction.FetchData -> {
                fetchData()
            }


            is StaffDashboardUiAction.OnEndDateChange -> {
                updateEndDate(action.date)
            }

            is StaffDashboardUiAction.OnStartDateChange -> {
                updateStartDate(action.date)
            }

            is StaffDashboardUiAction.OnPeriodChange -> {
                periodChange(action.period)
            }

        }
    }

    private fun fetchData() {
        fetchStaffAttendanceRecord()
        fetchStaffAttendanceRecordEachTime()
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
        fetchStaffAttendanceRecordEachTime()
    }

    private fun fetchStaffDetail() {
        viewModelScope.launch {
            userUseCase.getUserById(staffId)
                .suspendOnSuccess {
                    uiState.update {
                        it.copy(
                            staffDetail = this.data.data
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

    private fun fetchStaffAttendanceRecord() {
        viewModelScope.launch {
            summaryUseCase.getEmployeeAttendanceRecord(
                token = token,
                start = uiState.value.start.atStartOfDay(),
                end = uiState.value.end.atTime(23, 59, 59),
                userId = staffId
            )
                .suspendOnSuccess {
                    uiState.update {
                        it.copy(
                            attendanceRecord = this.data.data
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

    private fun fetchStaffAttendanceRecordEachTime() {
        viewModelScope.launch {
            summaryUseCase.getEmployeeAttendanceRecordEachTime(
                token = token,
                start = uiState.value.start.atStartOfDay(),
                end = uiState.value.end.atTime(23, 59, 59),
                period = uiState.value.period.time,
                userId = staffId
            )
                .suspendOnSuccess {
                    uiState.update {
                        it.copy(
                            staffAttendanceRecordEachTime = this.data.data ?: emptyList()
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

sealed interface StaffDashboardUiAction {
    data object FetchStaffStatistic : StaffDashboardUiAction
    data object FetchData : StaffDashboardUiAction

    data class OnStartDateChange(val date: LocalDate) : StaffDashboardUiAction
    data class OnEndDateChange(val date: LocalDate) : StaffDashboardUiAction

    data class OnPeriodChange(val period: Period) : StaffDashboardUiAction
}

data class StaffDashboardUiState(
    val start: LocalDate = LocalDate.now().withDayOfMonth(1),
    val end: LocalDate = YearMonth.now().atEndOfMonth(),

    val period: Period = Period.DAILY,

    val attendanceRecord: AttendanceRecord? = null,
    val staffDetail: User? = null,

    val staffAttendanceRecordEachTime: List<AttendanceRecord> = emptyList(),
)