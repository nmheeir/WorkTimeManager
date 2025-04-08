package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus
import com.kt.worktimetrackermanager.data.remote.dto.response.Project
import com.kt.worktimetrackermanager.data.remote.dto.response.Task
import com.kt.worktimetrackermanager.domain.use_case.project.ProjectUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val projectUseCase: ProjectUseCase,
) : ViewModel() {

    private val projectId = savedStateHandle.get<Int>("id")
    private val token = context.dataStore[TokenKey]!!
    private var pageNumber = 1
    private var pageSize = 10

    private val pagingState = MutableStateFlow(
        ProjectStatus.entries.associateWith { PagingState() }
    )

    val project = MutableStateFlow<Project?>(null)
    val isLoading = MutableStateFlow(false)
    val filter = MutableStateFlow<ProjectStatus?>(null)
    val allTasks = MutableStateFlow<List<Task>>(emptyList())
    val taskStateMap = mutableStateMapOf<ProjectStatus, List<Task>>()

    init {
        Timber.d(projectId.toString())
        fetchProjectDetail()
//        fetchTasksFromProject()

        viewModelScope.launch {
            filter.collect {
                if (it == null) {
                    if (allTasks.value.isEmpty()) {
                        fetchTasksFromProject()
                    }
                } else {
                    if (taskStateMap[filter.value] == null) {
                        fetchTasksFromProject()
                    }
                }
            }
        }
    }

    private fun fetchProjectDetail() {
        isLoading.value = true
        viewModelScope.launch {
            projectUseCase.getProject(token, projectId!!)
                .suspendOnSuccess {
                    project.value = this.data.data
                    Timber.d(this.data.data.toString())
                }
                .suspendOnFailure {
                    Timber.d("Failure: $this")
                }
                .suspendOnException {
                    Timber.d("Exception: $this")
                }
            isLoading.value = false
        }
    }

    private fun fetchTasksFromProject() {
        viewModelScope.launch {
            projectUseCase.getTasksFromProject(
                token = token,
                id = projectId!!,
                pageNumber = pageNumber,
                pageSize = pageSize,
                filter = filter.value
            )
                .suspendOnSuccess {
                    if (filter.value == null) {
                        allTasks.value = this.data.data!!.distinctBy { it.id }
                        Timber.d(this.data.data.toString())
                    } else {
                        taskStateMap[filter.value!!] = this.data.data!!
                    }
                }
                .suspendOnFailure {
                    Timber.d("Failure: $this")
                }
                .suspendOnException {
                    Timber.d("Exception: $this")
                }
        }
    }

    fun loadMore() {
        isLoading.value = true
        viewModelScope.launch {
            if (filter.value == null) {
                val newPageNumber = pageNumber + 1
                val newPageSize = pageSize

                projectUseCase.getTasksFromProject(
                    token = token,
                    id = projectId!!,
                    pageNumber = newPageNumber,
                    pageSize = newPageSize,
                    filter = null
                )
                    .suspendOnSuccess {
                        allTasks.value = this.data.data!!.distinctBy { it.id }
                    }
                    .suspendOnFailure {
                        Timber.d(this.message())
                    }
                    .suspendOnException {
                        Timber.d(this.throwable)
                    }

            } else {
                val newFilterPageNumber = pagingState.value[filter.value]!!.pageNumber + 1
                val filterPageSize = pagingState.value[filter.value]!!.pageSize

                pagingState.value[filter.value]!!.copy(
                    pageNumber = newFilterPageNumber,
                    pageSize = filterPageSize
                )

                projectUseCase.getTasksFromProject(
                    token = token,
                    id = projectId!!,
                    pageNumber = newFilterPageNumber,
                    pageSize = filterPageSize,
                    filter = filter.value
                )
                    .suspendOnSuccess {
                        taskStateMap[filter.value!!] = this.data.data!!
                    }
                    .suspendOnFailure {
                        Timber.d(this.message())
                    }
                    .suspendOnException {
                        Timber.d(this.throwable)
                    }
            }

        }
    }
}

data class PagingState(
    val pageNumber: Int = 1,
    val pageSize: Int = 10,
)