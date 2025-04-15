package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.domain.PagingState
import com.kt.worktimetrackermanager.core.ext.getEndOfMonth
import com.kt.worktimetrackermanager.core.ext.getStartOfMonth
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.TeamShiftStats
import com.kt.worktimetrackermanager.data.remote.repositories.impl.TeamRepo
import com.kt.worktimetrackermanager.domain.use_case.summary.SummaryUseCase
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
class CompanyShiftViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val summaryUseCase: SummaryUseCase,
) : ViewModel() {

    private val token = context.dataStore[TokenKey]!!
    private var pagingState = PagingState()

    val teamShiftsStat = MutableStateFlow<List<TeamShiftStats>>(emptyList())
    val startDate = MutableStateFlow<LocalDateTime>(LocalDateTime.now().getStartOfMonth())
    val endDate = MutableStateFlow<LocalDateTime>(LocalDateTime.now().getEndOfMonth())

    init {
    }

    private fun getShiftStatsByTeam() {
        viewModelScope.launch {
            summaryUseCase.getTeamShiftsStat(token, startDate.value, endDate.value)
                .suspendOnSuccess {
                    teamShiftsStat.value = this.data.data ?: emptyList()
                }
                .suspendOnFailure {
                    Timber.d(this.message())
                }
                .suspendOnException {
                    Timber.d(this.throwable.toString())
                }
        }
    }

    /*private fun loadMore() {
        viewModelScope.launch {

        }
    }*/
}