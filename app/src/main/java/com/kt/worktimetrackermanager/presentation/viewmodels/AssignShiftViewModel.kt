package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.enums.ShiftType
import com.kt.worktimetrackermanager.data.remote.dto.request.AddShiftRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.shift.ShiftUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AssignShiftViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val userUseCase: UserUseCase,
    private val shiftUseCase: ShiftUseCase,
) : ViewModel() {
    private val token = context.dataStore[TokenKey]!!
    private val teamId = MutableStateFlow<Int?>(null)

    val isLoadingUsers = MutableStateFlow(false)
    val isLoadingAssignShift = MutableStateFlow(false)

    val listUsers = MutableStateFlow<List<User>>(emptyList())
    val selectedUsers = MutableStateFlow<List<User>>(emptyList())

    val startDate = MutableStateFlow(LocalDateTime.now().with(LocalTime.MIN))
    val endDate = MutableStateFlow(LocalDateTime.now().with(LocalTime.MAX))

    val shiftType = MutableStateFlow<ShiftType>(ShiftType.Normal)

    val error = MutableStateFlow<AssignShiftError>(AssignShiftError())

    private val _channel = Channel<AssignShiftUiEvent>()
    val channel = _channel.receiveAsFlow()

    init {
        isLoadingUsers.value = true
        viewModelScope.launch {
            userUseCase.getUserProfile(token).suspendOnSuccess {
                teamId.value = this.data.data?.teamId
            }

            userUseCase.getUsers(
                token = token,
                teamId = teamId.value
            )
                .suspendOnSuccess {
                    listUsers.value = this.data.data ?: emptyList()
                }
                .suspendOnFailure {
                    Timber.d(this.toString())
                }
                .suspendOnException {
                    Timber.d(this.toString())
                }
            isLoadingUsers.value = false
        }
    }

    fun onAction(action: AssignShiftUiAction) {
        when (action) {
            is AssignShiftUiAction.AssignEmployee -> {
                selectedUsers.value = selectedUsers.value.toMutableList().apply {
                    add(action.employee)
                }
            }

            AssignShiftUiAction.AssignShift -> {
                assignShift()
            }

            is AssignShiftUiAction.OnStartDateTimeChange -> {
                startDate.value = action.newStartDate
            }

            is AssignShiftUiAction.OnEndDateTimeChange -> {
                endDate.value = action.newEndDate
            }

            is AssignShiftUiAction.OnShiftTypeChange -> {
                shiftType.value = action.shiftType
            }


            is AssignShiftUiAction.RemoveEmployee -> {
                selectedUsers.value = selectedUsers.value.toMutableList().apply {
                    remove(action.employee)
                }
            }
        }
    }

    private fun assignShift() {
        isLoadingAssignShift.value = true
        viewModelScope.launch {
            val shifts = convertToAddShiftRequest()
            shiftUseCase.addShift(
                token = token,
                shifts = shifts
            )
                .suspendOnSuccess {
                    Timber.d(this.data.data.toString())
                }
                .suspendOnFailure {
                    Timber.d(this.toString())
                }
                .suspendOnException {
                    Timber.d(this.toString())
                }

            _channel.send(AssignShiftUiEvent.Success)
            isLoadingAssignShift.value = false
        }
    }

    private fun convertToAddShiftRequest(): List<AddShiftRequest> {
        val list = selectedUsers.value.map { user ->
            AddShiftRequest(
                start = startDate.value,
                end = endDate.value,
                shiftType = shiftType.value,
                userId = user.id
            )
        }
        return list
    }
}

sealed interface AssignShiftUiAction {
    data object AssignShift : AssignShiftUiAction

    data class OnStartDateTimeChange(val newStartDate: LocalDateTime) : AssignShiftUiAction
    data class OnEndDateTimeChange(val newEndDate: LocalDateTime) : AssignShiftUiAction

    data class AssignEmployee(val employee: User) : AssignShiftUiAction
    data class RemoveEmployee(val employee: User) : AssignShiftUiAction

    data class OnShiftTypeChange(val shiftType: ShiftType) : AssignShiftUiAction
}

sealed interface AssignShiftUiEvent {
    data object Success : AssignShiftUiEvent
    data class Failure(val message: String) : AssignShiftUiEvent

    data object FetchUserInTeamSuccess : AssignShiftUiEvent
}

data class AssignShiftError(
    val timeError: String = "",
    val shiftError: String = "",
    val employeeError: String = "",
)