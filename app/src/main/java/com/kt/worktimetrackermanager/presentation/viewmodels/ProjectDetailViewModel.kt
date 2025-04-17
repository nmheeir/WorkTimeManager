package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.domain.PagingState
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.enums.ProjectStatus
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

    val isLoading = MutableStateFlow(false)
    val isRefreshing = MutableStateFlow(false)
    val project = MutableStateFlow<Project?>(null)
    val filter = MutableStateFlow<ProjectFilter>(ProjectFilter.All)

    val taskStateMap = mutableStateMapOf<ProjectFilter, List<Task>>()
    val loadMoreStateMap = mutableStateMapOf<ProjectFilter, Boolean>()
    val pagingState = mutableStateMapOf<ProjectFilter, PagingState>().apply {
        ProjectFilter.entries.forEach { put(it, PagingState()) }
    }

    init {
        fetchProjectDetail()

        viewModelScope.launch {
            filter.collect {
                if (taskStateMap[it] == null) {
                    fetchTasksFromProject()
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
                pageNumber = pagingState[filter.value]!!.pageNumber,
                pageSize = pagingState[filter.value]!!.pageSize,
                filter = filter.value.value
            )
                .suspendOnSuccess {
                    taskStateMap[filter.value] = this.data.data ?: emptyList()
                    loadMoreStateMap[filter.value] = this.data.pageNumber < this.data.totalPages
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
        // Sử dụng getOrPut để đảm bảo luôn có PagingState cho filter hiện tại.
        val currentFilter = filter.value
        val currentPaging = pagingState.getOrPut(currentFilter) { PagingState() }

        // Cập nhật paging state và gán lại vào map.
        val newPaging = currentPaging.copy(pageNumber = currentPaging.pageNumber + 1)
        pagingState[currentFilter] = newPaging

        viewModelScope.launch {
            projectUseCase.getTasksFromProject(
                token = token,
                id = projectId ?: return@launch, // Thoát nếu projectId null
                pageNumber = newPaging.pageNumber,
                pageSize = newPaging.pageSize,
                filter = currentFilter.value
            )
                .suspendOnSuccess {
                    val newTasks = this.data.data ?: emptyList()
                    val currentTasks = taskStateMap[currentFilter] ?: emptyList()
                    val updatedTasks = currentTasks.toMutableList().apply {
                        addAll(newTasks.filter { task -> none { it.id == task.id } })
                    }
                    taskStateMap[currentFilter] = updatedTasks

                    loadMoreStateMap[currentFilter] = this.data.pageNumber < this.data.totalPages
                }
                .suspendOnFailure {
                    Timber.d("Load more failure: ${this.message()}")
                }
                .suspendOnException {
                    Timber.d("Load more exception: ${this.throwable}")
                }
        }
    }


    fun refresh() {
        isRefreshing.value = true
        pagingState.entries.forEach {
            it.setValue(PagingState())
        }
        viewModelScope.launch {
            fetchTasksFromProject()
            isRefreshing.value = false
        }
    }
}

enum class ProjectFilter(val value: ProjectStatus?) {
    All(null),
    NotStarted(ProjectStatus.NotStarted),
    Completed(ProjectStatus.Completed),
    InProgress(ProjectStatus.InProgress),
    OnHold(ProjectStatus.OnHold),
    Cancelled(ProjectStatus.Cancelled)
}