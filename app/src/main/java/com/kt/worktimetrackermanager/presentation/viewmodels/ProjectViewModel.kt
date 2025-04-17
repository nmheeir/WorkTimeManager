package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.domain.PagingState
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.response.Project
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
class ProjectViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val projectUseCase: ProjectUseCase,
) : ViewModel() {

    private val token = context.dataStore[TokenKey] ?: ""


    val filter = MutableStateFlow<ProjectFilter>(ProjectFilter.All)
    val projectStateMap = mutableStateMapOf<ProjectFilter, List<Project>>()
    val loadMoreStateMap = mutableStateMapOf<ProjectFilter, Boolean>()
    val pagingState = mutableStateMapOf<ProjectFilter, PagingState>().apply {
        ProjectFilter.entries.forEach { put(it, PagingState()) }
    }

    val isLoading = MutableStateFlow(false)
    val isRefreshing = MutableStateFlow(false)

    init {
        isLoading.value = true
        viewModelScope.launch {
            filter.collect {
                if (projectStateMap[filter.value] == null) {
                    getProjects()
                }
            }
        }
    }

    private fun getProjects() {
        viewModelScope.launch {
            projectUseCase.getProjects(
                token = token,
                pageNumber = pagingState[filter.value]!!.pageNumber,
                pageSize = pagingState[filter.value]!!.pageSize,
                filter = filter.value.value
            )
                .suspendOnSuccess {
                    projectStateMap[filter.value] = this.data.data ?: emptyList()
                    Timber.d(this.data.data?.toString())
                }
                .suspendOnFailure {
                    Timber.d("Failure: %s", this.message())
                }
                .suspendOnException {
                    Timber.d("Exception: %s", this.message())
                }
            isLoading.value = false
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            // Sử dụng getOrPut để đảm bảo luôn có PagingState cho filter hiện tại.
            val currentFilter = filter.value
            val currentPaging = pagingState.getOrPut(currentFilter) { PagingState() }

            // Cập nhật paging state và gán lại vào map.
            val newPaging = currentPaging.copy(pageNumber = currentPaging.pageNumber + 1)
            pagingState[currentFilter] = newPaging

            viewModelScope.launch {
                projectUseCase.getProjects(
                    token = token,
                    pageNumber = newPaging.pageNumber,
                    pageSize = newPaging.pageSize,
                    filter = currentFilter.value
                )
                    .suspendOnSuccess {
                        val newTasks = this.data.data ?: emptyList()
                        val currentTasks = projectStateMap[currentFilter] ?: emptyList()
                        val updatedTasks = currentTasks.toMutableList().apply {
                            addAll(newTasks.filter { task -> none { it.id == task.id } })
                        }
                        projectStateMap[currentFilter] = updatedTasks

                        loadMoreStateMap[currentFilter] =
                            this.data.pageNumber < this.data.totalPages
                    }
                    .suspendOnFailure {
                        Timber.d("Load more failure: ${this.message()}")
                    }
                    .suspendOnException {
                        Timber.d("Load more exception: ${this.throwable}")
                    }
            }
        }
    }

    fun refresh() {
        isRefreshing.value = true
        viewModelScope.launch {
            pagingState[filter.value] = PagingState()
            getProjects()
            isRefreshing.value = false
        }
    }
}