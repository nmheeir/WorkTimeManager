package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.enums.Priority
import com.kt.worktimetrackermanager.data.remote.dto.request.CreateTaskDto
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.task.TaskUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    private val userUseCase: UserUseCase,
    private val taskUseCase: TaskUseCase,
) : ViewModel() {

    private val projectId = savedStateHandle.get<Int>("projectId")
    private val token = context.dataStore.get(TokenKey, "")

    private val _channel = Channel<CreateTaskUiEvent>()
    val channel = _channel.receiveAsFlow()

    val isLoading = MutableStateFlow(false)

    val users = MutableStateFlow<List<User>>(emptyList())
    val assignUsers = MutableStateFlow<List<User>>(emptyList())
    val title = MutableStateFlow<String>("")
    val description = MutableStateFlow<String>("")

    val priority = MutableStateFlow<Priority?>(null)

    init {
        fetchUsers()
    }

    fun onAction(action: CreateTaskUiAction) {
        when (action) {
            is CreateTaskUiAction.AssignUser -> {
                assignUsers.value = assignUsers.value.toMutableList().apply {
                    if (contains(action.user)) {
                        remove(action.user)
                    } else {
                        add(action.user)
                    }
                }
            }

            is CreateTaskUiAction.SetPriority -> {
                priority.value = action.priority
            }

            is CreateTaskUiAction.CreateTask -> {
                createTask(action.title, action.description)
            }
        }
    }

    private fun createTask(title: String, description: String) {
        isLoading.value = true
        viewModelScope.launch {
            val createTask = CreateTaskDto(
                projectId = projectId!!,
                name = title,
                description = description,
                priority = priority.value!!,
                assigneeIds = assignUsers.value.map { it.id },
                dueDate = LocalDateTime.now()
            )
            taskUseCase.createTask(
                token = token,
                createTaskDto = createTask
            )
                .suspendOnSuccess {
                    _channel.send(CreateTaskUiEvent.Success)
                    Timber.d("Success: %s", this.data)
                }
                .suspendOnFailure {
                    Timber.d("Failure: %s", this.message())
                }
                .suspendOnException {
                    Timber.d("Exception: %s", this.throwable.message)
                }
            delay(1000)
            isLoading.value = false
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            userUseCase.getUsers(
                token,
                pageNumber = 1,
                pageSize = 10,
            ).suspendOnSuccess {
                users.value = this.data.data!!
            }
                .suspendOnFailure {
                    Timber.d("Failure: %s", this.message())
                }
                .suspendOnException {
                    Timber.d("Exception: %s", this.throwable.message)
                }
        }
    }
}

sealed interface CreateTaskUiEvent {
    data object Success : CreateTaskUiEvent
}

sealed interface CreateTaskUiAction {
    data class SetPriority(val priority: Priority) : CreateTaskUiAction
    data class AssignUser(val user: User) : CreateTaskUiAction
    data class CreateTask(val title: String, val description: String) : CreateTaskUiAction
}