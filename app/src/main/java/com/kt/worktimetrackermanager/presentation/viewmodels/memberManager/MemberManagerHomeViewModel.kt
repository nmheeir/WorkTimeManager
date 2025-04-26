package com.kt.worktimetrackermanager.presentation.viewmodels.memberManager

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.response.UsersStatistic
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class MemberManagerHomeViewModel @Inject constructor(
    private val teamUseCase: TeamUseCase,
    private val userUseCase: UserUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {
    private val token = context.dataStore.get(TokenKey, "")

    private val _channel = Channel<MemberManagerHomeUIEvent>()
    val channel = _channel.receiveAsFlow()

    val uiState = MutableStateFlow(MemberMangerUiState())

    init {
        getCompanyTeams()
        getMembersStatistic()
    }

    private fun getMembersStatistic() {
        viewModelScope.launch {
            userUseCase
                .getUsersStatistic(token)
                .suspendOnSuccess {
                    uiState.value = uiState.value.copy(
                        memberTotal = this.data.data!!
                    )
                    _channel.send(MemberManagerHomeUIEvent.Success)
                }
                .suspendOnError {
                    Timber.d("getMembersStatistic: Error" + this.errorBody?.string())
                    _channel.send(MemberManagerHomeUIEvent.Failure(this.message()))
                }
                .suspendOnException {
                    Timber.d(
                        "getMembersStatistic: Exception" + this.message
                    )
                    _channel.send(MemberManagerHomeUIEvent.Failure(this.message ?: ""))
                }
        }
    }

    private fun getCompanyTeams() {
        viewModelScope.launch {
            teamUseCase
                .getCompanyTeams(token, pageSize = Int.MAX_VALUE)
                .suspendOnSuccess {
                    uiState.value = uiState.value.copy(
                        teamCount = this.data.totalItems
                    )
                    _channel.send(MemberManagerHomeUIEvent.Success)
                }
                .suspendOnError {
                    Timber.d("getCompanyTeams: Error" + this.errorBody?.string())
                    _channel.send(MemberManagerHomeUIEvent.Failure(this.message()))
                }
                .suspendOnException {
                    Timber.d(
                        "getCompanyTeams: Exception" + this.message
                    )
                    _channel.send(MemberManagerHomeUIEvent.Failure(this.message ?: ""))
                }
        }
    }
}

data class MemberMangerUiState(
    val memberTotal: UsersStatistic = UsersStatistic(0, 0, 0),
    val teamCount: Int = 0,
)

sealed interface MemberManagerHomeUIEvent {
    data object Success : MemberManagerHomeUIEvent
    data class Failure(val message: String) : MemberManagerHomeUIEvent
}