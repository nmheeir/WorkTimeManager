package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.domain.PagingState
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.enums.CheckType
import com.kt.worktimetrackermanager.data.remote.dto.enums.LogStatus
import com.kt.worktimetrackermanager.data.remote.dto.response.LogModel
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.log.LogUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TeamLogViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext context: Context,
    private val logUseCase: LogUseCase,
    private val userUseCase: UserUseCase,
) : ViewModel() {

    private val token = context.dataStore[TokenKey]!!
    private val teamId = savedStateHandle.get<Int>("id")

    private val pagingState = MutableStateFlow(PagingState())

    val isLoading = MutableStateFlow(false)
    val isUpdateLoading = MutableStateFlow(false)
    val isLoadMore = MutableStateFlow(true)

    val statusFilter = MutableStateFlow<LogStatus?>(null)
    val checkTypeFilter = MutableStateFlow<CheckType?>(null)

    val logs = MutableStateFlow<List<LogModel>>(emptyList())

    init {
        isLoading.value = true
        viewModelScope.launch {
            getTeamLogs()
            isLoading.value = false
        }
    }

    fun onAction(action: TeamLogUiAction) {
        when (action) {
            is TeamLogUiAction.ChangeStatusFilter -> {
                statusFilter.value = action.status
            }

            is TeamLogUiAction.ChangeCheckTypeFilter -> {
                checkTypeFilter.value = action.type
            }

            TeamLogUiAction.ResetFilter -> {
                statusFilter.value = null
                checkTypeFilter.value = null
            }

            TeamLogUiAction.Search -> {
                getTeamLogs()
            }

            is TeamLogUiAction.UpdateStatus -> {
                updateStatus(action.id, action.status)
            }
        }
    }

    private fun updateStatus(id: Int, status: LogStatus) {
        isUpdateLoading.value = true
        viewModelScope.launch {
            logUseCase.updateLogStatus(token, id, status)
                .suspendOnSuccess {
                    Timber.d(this.data.data.toString())
                }
                .suspendOnFailure {
                    Timber.d(this.toString())
                }
                .suspendOnException {
                    Timber.d(this.toString())
                }
            isUpdateLoading.value = false
        }
    }

    private fun getTeamLogs() {
        viewModelScope.launch {
            pagingState.value = PagingState()
            logUseCase.getTeamLogs(
                token = token,
                teamId = teamId,
                pageNumber = pagingState.value.pageNumber,
                pageSize = pagingState.value.pageSize,
                type = checkTypeFilter.value,
                status = statusFilter.value,
            )
                .suspendOnSuccess {
                    logs.value = this.data.data ?: emptyList()
                    isLoadMore.value = this.data.pageNumber <= this.data.totalPages
                    Timber.d(this.data.toString())
                }
                .suspendOnFailure {
                    Timber.d(this.toString())
                }
                .suspendOnException {
                    Timber.d(this.toString())
                }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            pagingState.value = pagingState.value.copy(
                pageNumber = pagingState.value.pageNumber + 1
            )
            logUseCase.getTeamLogs(
                token = token,
                teamId = teamId,
                pageNumber = pagingState.value.pageNumber,
                pageSize = pagingState.value.pageSize,
                type = checkTypeFilter.value,
                status = statusFilter.value,
            )
                .suspendOnSuccess {
                    val newLogs = this.data.data ?: emptyList()
                    val currentLog = logs.value
                    logs.value = currentLog.toMutableList().apply {
                        addAll(newLogs.filter { log -> none { it.id == log.id } })
                    }
                    isLoadMore.value = this.data.pageNumber <= this.data.totalPages
                    Timber.d(this.data.toString())
                }
                .suspendOnFailure {
                    Timber.d(this.toString())
                }
                .suspendOnException {
                    Timber.d(this.toString())
                }
        }
    }
}

sealed interface TeamLogUiAction {
    class ChangeStatusFilter(val status: LogStatus?) : TeamLogUiAction
    class ChangeCheckTypeFilter(val type: CheckType?) : TeamLogUiAction
    object ResetFilter : TeamLogUiAction
    object Search : TeamLogUiAction
    class UpdateStatus(val id: Int, val status: LogStatus) : TeamLogUiAction
}