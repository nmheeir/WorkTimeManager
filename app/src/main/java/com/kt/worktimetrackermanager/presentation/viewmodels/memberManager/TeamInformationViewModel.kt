package com.kt.worktimetrackermanager.presentation.viewmodels.memberManager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.data.local.LocalUserManager
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.domain.use_case.team.TeamUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.kt.worktimetrackermanager.presentation.exampleTeam
import com.skydoves.sandwich.message
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.retrofit.statusCode
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
class TeamInformationViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase,
    private val userUseCase: UserUseCase,
    private val localUserManager: LocalUserManager,
): ViewModel() {
    private val _state = MutableStateFlow(TeamInformationUiState())
    val uiState = _state
        .stateIn(viewModelScope, SharingStarted.Lazily, TeamInformationUiState())

    private val _channel = Channel<TeamInformationUiEvent>()
    val channel = _channel.receiveAsFlow()

    init {
        getTeamInformation()
        getUsersInTeam()
    }

    fun setTeamId (id: Int) {
        _state.value = _state.value.copy(teamId = id)
    }

    fun getTeamInformation() {
        viewModelScope.launch {
            val token = localUserManager.readAccessToken()

            teamUseCase
                .getCompanyTeamById(token, _state.value.teamId)
                .suspendOnSuccess {
                    _state.value = _state.value.copy(
                        team = this.data.data!!
                    )
                    _channel.send(TeamInformationUiEvent.Success)
                }
                .suspendOnError {
                    _channel.send(
                        TeamInformationUiEvent.Failure(
                            this.errorBody.toString()
                        )
                    )
                    Timber.d("getTeamInformation: " + this.message() + this.statusCode.toString())
                }
                .suspendOnException {
                    _channel.send(TeamInformationUiEvent.Failure(this.throwable.message ?: ""))
                    Timber.d(
                        "getTeamInformation: " + this.throwable.message
                    )
                }
        }
    }

    fun getUsersInTeam() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true) // Đúng cách
            val token = localUserManager.readAccessToken()

            userUseCase
                .getUsers(token,
                    pageNumber = 1,
                    pageSize = 10,
                    teamId = _state.value.teamId
                )
                .suspendOnSuccess {
                    _state.value = _state.value.copy(
                        team = _state.value.team.copy(
                            users = this.data.data!!
                        ),
                        loading = false
                    )

                    _channel.send(TeamInformationUiEvent.Success)
                }
                .suspendOnError {
                    _channel.send(
                        TeamInformationUiEvent.Failure(
                            this.errorBody.toString()
                        )
                    )
                    Timber.d("getUsersInTeam: " + this.message() + this.statusCode.toString())
                }
                .suspendOnException {
                    _channel.send(TeamInformationUiEvent.Failure(this.throwable.message ?: ""))
                    Timber.d(
                        "getUsersInTeam: " + this.throwable.message
                    )
                }

        }
    }
}

data class TeamInformationUiState(
    var team: Team = exampleTeam,
    var teamId: Int = 0,
    var loading: Boolean = true
)

sealed interface TeamInformationUiEvent {
    data object Success : TeamInformationUiEvent
    data class Failure(val message: String) : TeamInformationUiEvent
}