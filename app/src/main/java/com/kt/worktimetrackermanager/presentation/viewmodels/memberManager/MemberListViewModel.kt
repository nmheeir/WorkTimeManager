package com.kt.worktimetrackermanager.presentation.viewmodels.memberManager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.data.local.LocalUserManager
import com.kt.worktimetrackermanager.data.remote.dto.enums.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enums.Role

import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.team.TeamUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
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
class MemberListViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val teamUseCase: TeamUseCase,
    private val localUserManager: LocalUserManager,
): ViewModel() {

    private val _channel = Channel<MemberListUiEvent>()
    val channel = _channel.receiveAsFlow()


    private val _state = MutableStateFlow(MemberListUiState())
    val uiState = _state
        .stateIn(viewModelScope, SharingStarted.Lazily, MemberListUiState())

    init {
        getMembersInCompany()
        getCompanyTeams()
    }

    fun onAction(event: MemberListUiAction) {
        when(event) {
            is MemberListUiAction.OnFieldChange -> {
                onFieldChange(event.fieldName, event.value)
            }
            is MemberListUiAction.OnScrollToBottom -> {
                if(_state.value.pageNumber < _state.value.totalPage) {
                    _state.value = _state.value.copy(
                        pageNumber = _state.value.pageNumber + 1
                    )
                    loadMoreMember()
                }
            }
        }

    }

    fun setFilter(teamId: Int?, role: Int?, employeeType: Int?) {
        _state.value = _state.value.copy(
            teamId = teamId,
            role = role?.let { Role.fromInt(it) },
            employeeType = employeeType?.let { EmployeeType.fromInt(employeeType) }
        )
    }

    private fun onFieldChange(fieldName: String, value: Any?) {
        _state.value = when (fieldName) {
            "role" -> _state.value.copy(role = value as Role?)
            "teamId" -> _state.value.copy(teamId = (value as Team?)?.id)
            "employeeType" -> _state.value.copy(employeeType = value as EmployeeType?)
            "searchValue" -> _state.value.copy(searchValue = value as String)
            else -> _state.value
        }
        getMembersInCompany()
    }
    private fun getMembersInCompany() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, pageNumber = 1) // Đúng cách

            val token = localUserManager.readAccessToken()

            userUseCase
                .getUsers(token,
                    pageNumber = _state.value.pageNumber,
                    pageSize = 10,
                    role = _state.value.role?.ordinal,
                    employeeType = _state.value.employeeType?.value,
                    username = _state.value.searchValue,
                    teamId = _state.value.teamId
                )
                .suspendOnSuccess {
                    _state.value = _state.value.copy(
                        memberList = this.data.data!!,
                        loading = false,
                        totalPage = this.data.totalPages
                    )

                    _channel.send(MemberListUiEvent.Success)
                }
                .suspendOnError {
                    Timber.d("getMember: Error" + this.errorBody?.string())
                    _channel.send(MemberListUiEvent.Failure(this.message()))
                }
                .suspendOnException {
                    Timber.d(
                        "getMember: Exception" + this.throwable.message
                    )
                    _channel.send(MemberListUiEvent.Failure(this.throwable.message ?: ""))
                }
        }
    }
    private fun loadMoreMember() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true) // Đúng cách

            val token = localUserManager.readAccessToken()

            userUseCase
                .getUsers(token,
                    pageNumber = _state.value.pageNumber,
                    pageSize = 10,
                    role = _state.value.role?.ordinal,
                    employeeType = _state.value.employeeType?.value,
                    username = _state.value.searchValue,
                    teamId = _state.value.teamId
                )
                .suspendOnSuccess {
                    _state.value = _state.value.copy(
                        memberList = _state.value.memberList + this.data.data!!,
                        loading = false,
                        totalPage = this.data.totalPages
                    )

                    _channel.send(MemberListUiEvent.Success)
                }
                .suspendOnError {
                    Timber.d("loadMoreMember: Error" + this.errorBody?.string())
                    _channel.send(MemberListUiEvent.Failure(this.message()))
                }
                .suspendOnException {
                    Timber.d(
                        "loadMoreMember: Exception" + this.throwable.message
                    )
                    _channel.send(MemberListUiEvent.Failure(this.throwable.message ?: ""))
                }
        }
    }
    private fun getCompanyTeams() {
        viewModelScope.launch {
            val token = localUserManager.readAccessToken()

            teamUseCase
                .getCompanyTeams(token, pageSize = Int.MAX_VALUE)
                .suspendOnSuccess {
                    _state.value = _state.value.copy(
                        teamOptionList = this.data.data!!
                    )
                    _channel.send(MemberListUiEvent.Success)
                }
                .suspendOnError {
                    Timber.d("getCompanyTeams: Error" + this.errorBody?.string())
                    _channel.send(MemberListUiEvent.Failure(this.message()))
                }
                .suspendOnException {
                    Timber.d(
                        "getCompanyTeams: Exception" + this.throwable.message
                    )
                    _channel.send(MemberListUiEvent.Failure(this.throwable.message ?: ""))
                }
        }
    }
}

data class MemberListUiState (
    var memberList: List<User> = emptyList(),
    var role: Role? = null,
    var teamId: Int? = null,
    var employeeType: EmployeeType? = null,


    var teamOptionList: List<Team> = emptyList(),
    var searchValue: String = "",

    var loading: Boolean = true,
    var pageNumber: Int = 1,
    val totalPage: Int = 1,
)

sealed interface MemberListUiEvent {
    data object Success : MemberListUiEvent
    data class Failure(val message: String) : MemberListUiEvent
}

sealed interface MemberListUiAction {
    data class OnFieldChange(var fieldName: String, var value: Any?) : MemberListUiAction
    data object OnScrollToBottom : MemberListUiAction
}