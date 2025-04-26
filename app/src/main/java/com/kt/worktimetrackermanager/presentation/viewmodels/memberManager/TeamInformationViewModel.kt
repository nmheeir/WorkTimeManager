package com.kt.worktimetrackermanager.presentation.viewmodels.memberManager

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.domain.use_case.team.TeamUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.kt.worktimetrackermanager.presentation.exampleTeam
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TeamInformationViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase,
    private val userUseCase: UserUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val token = context.dataStore.get(TokenKey, "")

    private val _state = MutableStateFlow(TeamInformationUiState())
    val uiState = _state
        .stateIn(viewModelScope, SharingStarted.Lazily, TeamInformationUiState())
    // Prevent redundant emissions

    private val _channel = Channel<TeamInformationUiEvent>()
    val channel = _channel.receiveAsFlow()

    init {
        setTeamId(1)
    }

    fun setTeamId(id: Int) {
        if (_state.value.teamId != id) { // Avoid redundant updates
            _state.value = _state.value.copy(teamId = id)
            fetchTeamData() // Batch API calls
        }
    }

    private fun fetchTeamData() {
        viewModelScope.launch {
            launch { getTeamInformation(token) }
            launch { getUsersInTeam(token) }
        }
    }

    private suspend fun getTeamInformation(token: String) {
        teamUseCase.getCompanyTeamById(token, _state.value.teamId)
            .suspendOnSuccess {
                _state.value = _state.value.copy(team = this.data.data!!)
                _channel.send(TeamInformationUiEvent.Success)
            }
            .suspendOnError { handleError(this.errorBody.toString()) }
            .suspendOnException { handleError(this.throwable.message ?: "") }
    }

    private suspend fun getUsersInTeam(token: String) {
        userUseCase.getUsers(token, pageNumber = 1, pageSize = 10, teamId = _state.value.teamId)
            .suspendOnSuccess {
                _state.value = _state.value.copy(
                    team = _state.value.team.copy(users = this.data.data!!),
                    loading = false
                )
                _channel.send(TeamInformationUiEvent.Success)
            }
            .suspendOnError { handleError(this.errorBody.toString()) }
            .suspendOnException { handleError(this.throwable.message ?: "") }
    }

    private suspend fun handleError(message: String) {
        _channel.send(TeamInformationUiEvent.Failure(message))
    }
}

data class TeamInformationUiState(
    var team: Team = exampleTeam,
    var teamId: Int = 0,
    var loading: Boolean = true,
)

sealed interface TeamInformationUiEvent {
    data object Success : TeamInformationUiEvent
    data class Failure(val message: String) : TeamInformationUiEvent
}