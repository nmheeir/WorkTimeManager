package com.kt.worktimetrackermanager.presentation.screens.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.utils.Gap
import com.kt.worktimetrackermanager.presentation.components.chip.ChipsRow
import com.kt.worktimetrackermanager.presentation.components.items.TaskCardItem
import com.kt.worktimetrackermanager.presentation.viewmodels.ProjectDetailViewModel
import com.kt.worktimetrackermanager.presentation.viewmodels.ProjectFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    navController: NavHostController,
    viewModel: ProjectDetailViewModel = hiltViewModel(),
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val project by viewModel.project.collectAsStateWithLifecycle()
    val taskFilter by viewModel.filter.collectAsStateWithLifecycle()
    val taskPage by remember(taskFilter) {
        derivedStateOf {
            taskFilter.let {
                viewModel.taskStateMap[it]
            }
        }
    }

    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.layoutInfo.visibleItemsInfo.any { it.key == "load_more" }
        }.collect { shouldShowLoadMore ->
            if (!shouldShowLoadMore) return@collect
            viewModel.loadMore()
        }
    }

    PullToRefreshBox(
        onRefresh = {
            viewModel.refresh()
        },
        isRefreshing = isRefreshing,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
            ) {
                if (project == null) {
                    item {
                        Text(
                            text = "No project"
                        )
                    }
                }

                project.takeIf { it != null }?.let { pDetail ->
                    item(
                        key = "top_bar"
                    ) {
                        TopAppBar(
                            title = {
                                Text(text = pDetail.name)
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = navController::navigateUp
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_keyboard_arrow_left),
                                        contentDescription = null
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = { navController.navigate("project/${pDetail.id}/task/create") }
                                ) {
                                    Icon(painterResource(R.drawable.ic_note_add), null)
                                }
                            }
                        )
                    }

                    item(
                        key = "project_summary"
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .hozPadding()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
                                modifier = Modifier
                                    .padding(vertical = MaterialTheme.padding.small)
                            ) {
                                Text(
                                    text = "Summary of your work",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.hozPadding()
                                )
                                Text(
                                    text = "Your current project progress",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.hozPadding()
                                )
                            }
                        }
                    }

                    item(
                        key = "filter_task"
                    ) {
                        ChipsRow(
                            chips = listOf(
                                ProjectFilter.All to stringResource(R.string.filter_all),
                                ProjectFilter.NotStarted to stringResource(R.string.filter_status_not_started),
                                ProjectFilter.Completed to stringResource(R.string.filter_status_complete),
                                ProjectFilter.OnHold to stringResource(R.string.filter_status_on_hold),
                                ProjectFilter.Cancelled to stringResource(R.string.filter_status_cancelled),
                                ProjectFilter.InProgress to stringResource(R.string.filter_status_in_progress)
                            ),
                            currentValue = taskFilter,
                            onValueUpdate = {
                                if (viewModel.filter.value != it) {
                                    viewModel.filter.value = it
                                }
                                /*scope.launch {
                                    lazyListState.animateScrollToItem(0)
                                }*/
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }


                    items(
                        items = taskPage.orEmpty(),
                        key = { it.id }
                    ) { task ->
                        TaskCardItem(
                            task = task,
                            onClick = {
                                navController.navigate("task_detail/${task.id}")
                            }
                        )
                        Gap(MaterialTheme.padding.small)
                    }

                    // TODO: Load more
                    if (viewModel.loadMoreStateMap[taskFilter] == true) {
                        item(
                            key = "load_more"
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        item {
                            Text(
                                text = "No result found"
                            )
                        }
                    }
                }
            }
        }
    }
}