package com.kt.worktimetrackermanager.presentation.viewmodels.memberManager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.data.local.LocalUserManager
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.domain.use_case.team.TeamUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class TeamListViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase,
    private val localUserManager: LocalUserManager,
) : ViewModel() {
    private val _state = MutableStateFlow(TeamListScreenUiState())
    val uiState = _state
        .stateIn(viewModelScope, SharingStarted.Lazily, TeamListScreenUiState())
    private val _channel = Channel<TeamListUiEvent>()
    val channel = _channel.receiveAsFlow()

    init {
        getCompanyTeams()
    }


    fun onAction(action: TeamListUiAction) {
        when (action) {
            is TeamListUiAction.OnSearchValueChange -> {
                _state.value = _state.value.copy(searchValue = action.value)
                getCompanyTeams()
            }

            is TeamListUiAction.OnHitBottom -> {
                if (_state.value.totalPage > _state.value.pageNumber) {
                    _state.value = _state.value.copy(
                        pageNumber = _state.value.pageNumber + 1
                    )
                    loadMoreTeam()
                }
            }
        }
    }

    private fun getCompanyTeams() {
        viewModelScope.launch {
            val token = localUserManager.readAccessToken()
            _state.value = _state.value.copy(isLoading = true, pageNumber = 1)
            teamUseCase
                .getCompanyTeams(
                    token = token,
                    pageNumber = _state.value.pageNumber,
                    searchValue = _state.value.searchValue
                )
                .suspendOnSuccess {
                    _state.value = _state.value.copy(
                        teamList = this.data.data!!,
                        isLoading = false
                    )
                    _channel.send(TeamListUiEvent.Success)
                }
                .suspendOnError {
                    Timber.d("getCompanyTeams: Error" + this.errorBody?.string())
                    _channel.send(TeamListUiEvent.Failure(this.message()))
                }
                .suspendOnException {
                    Timber.d(
                        "getCompanyTeams: Exception" + this.throwable.message
                    )
                    _channel.send(TeamListUiEvent.Failure(this.throwable.message?: ""))
                }
        }
    }

    private fun loadMoreTeam() {
        viewModelScope.launch {
            val token = localUserManager.readAccessToken()
            _state.value = _state.value.copy(isLoading = true)
            teamUseCase
                .getCompanyTeams(
                    token = token,
                    pageNumber = _state.value.pageNumber,
                    searchValue = _state.value.searchValue
                )
                .suspendOnSuccess {
                    _state.value = _state.value.copy(
                        teamList = _state.value.teamList + this.data.data!!,
                        isLoading = false
                    )
                    _channel.send(TeamListUiEvent.Success)
                }
                .suspendOnError {
                    Timber.d("loadMoreTeam: Error" + this.errorBody?.string())
                    _channel.send(TeamListUiEvent.Failure(this.message()))
                }
                .suspendOnException {
                    Timber.d(
                        "loadMoreTeam: Exception" + this.throwable.message
                    )
                    _channel.send(TeamListUiEvent.Failure(this.throwable.message?: ""))
                }
        }
    }


}
sealed interface TeamListUiAction {
    data class OnSearchValueChange(var value: String) : TeamListUiAction
    data object OnHitBottom : TeamListUiAction
}

data class TeamListScreenUiState (
    var teamList: List<Team> = emptyList(),
    var searchValue: String = "",
    var pageNumber: Int = 1,
    var totalPage: Int = 1,
    var isLoading : Boolean = false
)

sealed interface TeamListUiEvent {
    data object Success : TeamListUiEvent
    data class Failure(val message: String) : TeamListUiEvent
}