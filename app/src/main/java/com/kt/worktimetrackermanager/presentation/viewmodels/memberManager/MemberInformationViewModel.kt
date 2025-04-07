package com.kt.worktimetrackermanager.presentation.viewmodels.memberManager

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.kt.worktimetrackermanager.presentation.fakeUser
import com.kt.worktimetrackermanager.presentation.sampleUser
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MemberInforViewModel @Inject constructor (
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(MemberInforUiState());

    private val _channel = Channel<MemberInforUiEvent>()
    val channel = _channel.receiveAsFlow()

    val uiState = _state
        .stateIn(viewModelScope, SharingStarted.Lazily, MemberInforUiState())

    init {
        getUserInfo(uiState.value.userId)
    }

    fun setUserId(id: Int) {
        _state.update {
            it.copy(
                userId = id
            )
        }
    }

    private fun getUserInfo(id: Int) {
        viewModelScope.launch {
            userUseCase.getUserById(id)
                .suspendOnSuccess {
                    _state.update {
                        it.copy(
                            user = this.data.data!!
                        )
                    }

                }
                .suspendOnError {
                    Timber.d("getUserInfo: Error" + this.errorBody?.string())
                    _channel.send(MemberInforUiEvent.Failure(this.message()))
                }
                .suspendOnException {
                    Timber.d(
                        "getUserInfo: Exception" + this.message
                    )
                    _channel.send(MemberInforUiEvent.Failure(this.message ?: ""))
                }
        }
    }
}

sealed interface MemberInforUiEvent {
    data object Success : MemberInforUiEvent
    data class Failure(val message: String) : MemberInforUiEvent
}

data class MemberInforUiState (
    var user: User = sampleUser,
    var userId: Int = 1
)

