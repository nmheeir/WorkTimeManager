package com.kt.worktimetrackermanager.presentation.screens.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus
import com.kt.worktimetrackermanager.presentation.components.chip.ChipsRow
import com.kt.worktimetrackermanager.presentation.components.items.TaskCardItem
import com.kt.worktimetrackermanager.presentation.viewmodels.ProjectDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    navController: NavHostController,
    viewModel: ProjectDetailViewModel = hiltViewModel(),
) {
    val project by viewModel.project.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val taskFilter by viewModel.filter.collectAsStateWithLifecycle()
    val taskPage by remember(taskFilter) {
        derivedStateOf {
            taskFilter?.let {
                viewModel.taskStateMap[it]
            }
        }
    }
    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle()

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

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize()
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

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
                            verticalAlignment = Alignment.CenterVertically,
                            contentPadding = PaddingValues(horizontal = MaterialTheme.padding.mediumSmall),
                        ) {
                            items(
                                items = ProjectStatus.entries,
                                key = { it.ordinal }
                            ) { status ->
                                val containerColor = when (status) {
                                    ProjectStatus.NotStarted -> MaterialTheme.colorScheme.outline
                                    ProjectStatus.InProgress -> MaterialTheme.colorScheme.primary
                                    ProjectStatus.Completed -> MaterialTheme.colorScheme.tertiary
                                    ProjectStatus.OnHold -> MaterialTheme.colorScheme.secondary
                                    ProjectStatus.Cancelled -> MaterialTheme.colorScheme.error
                                }
                                val textColor = when (status) {
                                    ProjectStatus.NotStarted -> MaterialTheme.colorScheme.surface
                                    ProjectStatus.InProgress -> MaterialTheme.colorScheme.onPrimary
                                    ProjectStatus.Completed -> MaterialTheme.colorScheme.onTertiary
                                    ProjectStatus.OnHold -> MaterialTheme.colorScheme.onSecondary
                                    ProjectStatus.Cancelled -> MaterialTheme.colorScheme.onError
                                }
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = containerColor
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
                                        horizontalAlignment = Alignment.Start,
                                        modifier = Modifier.padding(MaterialTheme.padding.small)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(
                                                MaterialTheme.padding.extraSmall
                                            ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                painter = painterResource(
                                                    when (status) {
                                                        ProjectStatus.NotStarted -> R.drawable.ic_hourglass_empty
                                                        ProjectStatus.InProgress -> R.drawable.ic_play_arrow
                                                        ProjectStatus.Completed -> R.drawable.ic_check_circle
                                                        ProjectStatus.OnHold -> R.drawable.ic_pause_circle
                                                        ProjectStatus.Cancelled -> R.drawable.ic_cancel
                                                    }
                                                ),
                                                tint = textColor,
                                                contentDescription = null
                                            )
                                            Text(
                                                text = status.title,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = textColor
                                            )
                                        }
                                        Text(
                                            text = pDetail.tasks.filter {
                                                it.status == status
                                            }.size.toString(),
                                            color = textColor
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item(
                key = "filter_task"
            ) {
                ChipsRow(
                    chips = listOf(
                        null to stringResource(R.string.filter_all),
                        ProjectStatus.NotStarted to stringResource(R.string.filter_status_not_started),
                        ProjectStatus.Completed to stringResource(R.string.filter_status_complete),
                        ProjectStatus.OnHold to stringResource(R.string.filter_status_on_hold),
                        ProjectStatus.Cancelled to stringResource(R.string.filter_status_cancelled),
                        ProjectStatus.InProgress to stringResource(R.string.filter_status_in_progress)
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

            if (taskFilter == null) {
                items(
                    items = allTasks
                ) { task ->
                    TaskCardItem(
                        task = task,
                        onClick = {}
                    )
                    Gap(MaterialTheme.padding.small)
                }
                if (allTasks.isEmpty()) {
                    item {
                        Text(
                            text = "No task found"
                        )
                    }
                }
            } else {
                items(
                    items = taskPage.orEmpty(),
                    key = { it.id }
                ) { task ->
                    TaskCardItem(
                        task = task,
                        onClick = {}
                    )
                    Gap(MaterialTheme.padding.small)
                }
                if (taskPage.isNullOrEmpty()) {
                    item {
                        Text(
                            text = "No task found"
                        )
                    }
                }
            }
        }
    }
}