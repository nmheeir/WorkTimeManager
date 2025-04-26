package com.kt.worktimetrackermanager.presentation.viewmodels.memberManager

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.request.CreateTeamRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.team.TeamUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class TeamCreateViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase,
    private val userUseCase: UserUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val token = context.dataStore.get(TokenKey, "")

    private val _state = MutableStateFlow(TeamCreateUiState())
    val uiState = _state
        .stateIn(viewModelScope, SharingStarted.Lazily, TeamCreateUiState())

    private val _channel = Channel<TeamCreateUiEvent>()
    val channel = _channel.receiveAsFlow()

    val isLoading = MutableStateFlow(false)

    fun onAction(action: TeamCreateUiAction) {
        when (action) {
            is TeamCreateUiAction.OnFieldChange -> {
                onFieldChange(action.fieldName, action.value)
            }

            is TeamCreateUiAction.OnChooseManager -> {
                _state.value = _state.value.copy(
                    teamManager = action.manager
                )
            }

            is TeamCreateUiAction.OnGetUsers -> {
                getMembersInCompany()
            }

            is TeamCreateUiAction.OnCreateTeam -> {
                createTeam()
            }

            else -> {}
        }
    }

    private fun onFieldChange(fieldName: String, value: Any?) {
        _state.value = when (fieldName) {
            "latitude" -> {
                _state.value.copy(teamLatitude = value as Double)
            }

            "longitude" -> {
                _state.value.copy(teamLongitude = value as Double)
            }

            "username" -> {
                _state.value.copy(userName = value as String)
            }

            "teamname" -> {
                _state.value.copy(teamName = value as String)
            }

            else -> _state.value
        }
    }


    private fun getMembersInCompany() {
        viewModelScope.launch {
            userUseCase
                .getUsers(
                    token,
                    pageNumber = 1,
                    pageSize = 3,
                    username = _state.value.userName,
                )
                .suspendOnSuccess {
                    _state.value = _state.value.copy(
                        memList = this.data.data!!,
                    )
//                    _channel.send(TeamCreateUiEvent.Success)
                }
                .suspendOnError {
                    Timber.d("GetMembers: Error" + this.errorBody?.string())
                    _channel.send(TeamCreateUiEvent.Success)
//                    _channel.send(TeamCreateUiEvent.Failure(this.message()))
                }
                .suspendOnException {
                    Timber.d(
                        "GetMembers: Exception" + (this.throwable.message)
                    )
                    _channel.send(TeamCreateUiEvent.Success)
//                    _channel.send(TeamCreateUiEvent.Failure((this.throwable.message ?: "")))
                }
        }
    }

    private fun createTeam() {
        isLoading.value = true
        viewModelScope.launch {
            delay(2000)

            // Kiểm tra các giá trị đầu vào và log nếu có giá trị null
            if (_state.value.teamLongitude == null) {
                Timber.e("Longitude is null")
                return@launch
            }
            if (_state.value.teamLatitude == null) {
                Timber.e("Latitude is null")
                return@launch
            }
            if (_state.value.teamManager == null) {
                Timber.e("UserId is null")
                return@launch
            }
            if (_state.value.teamName.isEmpty()) {
                Timber.e("Team Name is null")
                return@launch
            }

            val newTeam = CreateTeamRequest(
                longitude = _state.value.teamLongitude!!,
                latitude = _state.value.teamLatitude!!,
                userId = _state.value.teamManager!!.id,
                name = _state.value.teamName
            )

            teamUseCase
                .createTeam(token, newTeam)
                .suspendOnSuccess {
                    _channel.send(TeamCreateUiEvent.Success)
//                    _channel.send(TeamCreateUiEvent.Success)
                }
                .suspendOnError {
                    Timber.d("createTeam: Error" + this.errorBody?.string())
                    _channel.send(TeamCreateUiEvent.Success)
//                    _channel.send(TeamCreateUiEvent.Failure(this.message()))
                }
                .suspendOnException {
                    Timber.d(
                        "createTeam: Exception" + (this.throwable.message)
                    )
                    _channel.send(TeamCreateUiEvent.Success)
//                    _channel.send(TeamCreateUiEvent.Failure((this.throwable.message ?: "")))
                }
            isLoading.value = false
        }
    }

}

data class TeamCreateUiState(
    val teamName: String = "",
    val teamLatitude: Double? = null,
    val teamLongitude: Double? = null,
    val userName: String = "",
    val teamManager: User? = null,
    val memList: List<User> = emptyList(),
)

sealed interface TeamCreateUiEvent {
    data object Success : TeamCreateUiEvent
    data class Failure(val message: String) : TeamCreateUiEvent
}


sealed interface TeamCreateUiAction {
    data object OnCreateTeam : TeamCreateUiAction
    data class OnFieldChange(var fieldName: String, var value: Any?) : TeamCreateUiAction
    data class OnChooseManager(var manager: User) : TeamCreateUiAction
    data object OnGetUsers : TeamCreateUiAction
}