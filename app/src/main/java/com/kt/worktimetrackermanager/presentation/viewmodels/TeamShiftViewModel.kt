package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.domain.PagingState
import com.kt.worktimetrackermanager.core.ext.getEndOfMonth
import com.kt.worktimetrackermanager.core.ext.getStartOfMonth
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.response.Shift
import com.kt.worktimetrackermanager.domain.use_case.shift.ShiftUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TeamShiftViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val shiftUseCase: ShiftUseCase,
) : ViewModel() {

    private val token = context.dataStore[TokenKey]!!
    private val teamId = savedStateHandle.get<Int>("id")
    private var pagingState = PagingState()

    val startDate = MutableStateFlow(LocalDateTime.now().getStartOfMonth())
    val endDate = MutableStateFlow(LocalDateTime.now().getEndOfMonth())

    val shifts = MutableStateFlow<List<Shift>>(emptyList())

    init {
        getShiftsInTeam()
    }

    private fun getShiftsInTeam() {
        viewModelScope.launch {
            shiftUseCase.getShiftsInTeam(
                token = token,
                teamId = teamId,
                pageNumber = pagingState.pageNumber,
                pageSize = pagingState.pageSize,
                start = startDate.value,
                end = endDate.value
            )
                .suspendOnSuccess {
                    shifts.value = this.data.data ?: emptyList()
                    Timber.d(this.data.data.toString())
                }
                .suspendOnFailure {
                    Timber.d("Failure ${this.message()}")
                }
                .suspendOnException {
                    Timber.d("Exception ${this.throwable.toString()}")
                }
        }
    }
}