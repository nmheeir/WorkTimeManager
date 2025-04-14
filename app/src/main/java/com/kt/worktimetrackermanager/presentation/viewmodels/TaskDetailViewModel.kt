package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.response.Task
import com.kt.worktimetrackermanager.domain.use_case.task.TaskUseCase
import com.kt.worktimetrackermanager.presentation.fakeTasks
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext context: Context,
    private val taskUseCase: TaskUseCase,
) : ViewModel() {
    val taskId = savedStateHandle.get<Int>("id")!!
    val token = context.dataStore[TokenKey]!!

    val task = MutableStateFlow<Task?>(null)
    val isLoading = MutableStateFlow(false)

    init {
        fetchTaskDetail()
    }

    private fun fetchTaskDetail() {
        isLoading.value = true
        viewModelScope.launch {
            taskUseCase.getTaskDetail(token, taskId)
                .suspendOnSuccess {
                    task.value = this.data.data
                    Timber.d(this.data.data.toString())
                }
                .suspendOnFailure {
                    Timber.d(this.message())
                }
                .suspendOnException {
                    Timber.d(this.throwable)
                }

            isLoading.value = false
        }
    }
}