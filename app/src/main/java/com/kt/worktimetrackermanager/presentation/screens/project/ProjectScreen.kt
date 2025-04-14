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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.kt.worktimetrackermanager.core.presentation.ui.EmptyBox
import com.kt.worktimetrackermanager.core.presentation.ui.components.HideOnScrollComponent
import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus
import com.kt.worktimetrackermanager.presentation.components.chip.ChipsRow
import com.kt.worktimetrackermanager.presentation.components.items.ProjectCardItem
import com.kt.worktimetrackermanager.presentation.viewmodels.ProjectFilter
import com.kt.worktimetrackermanager.presentation.viewmodels.ProjectViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    navController: NavHostController,
    viewModel: ProjectViewModel = hiltViewModel(),
) {
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val projectPage by remember(filter) {
        derivedStateOf {
            filter.let {
                viewModel.projectStateMap[it]
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                item(
                    key = "summary_your_work"
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

                            // TODO: Summary all project
                        }
                    }
                }

                item(
                    key = "choices"
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
                        currentValue = filter,
                        onValueUpdate = {
                            if (viewModel.filter.value != it) {
                                viewModel.filter.value = it
                            }
                            scope.launch {
                                lazyListState.animateScrollToItem(0)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                items(
                    items = projectPage.orEmpty()
                ) {
                    ProjectCardItem(
                        project = it,
                        onClick = {
                            navController.navigate("project/detail?id=${it.id}")
                        }
                    )
                }
                if (projectPage.isNullOrEmpty()) {
                    item {
                        EmptyBox(
                            stringRes = R.string.msg_no_project,
                            descRes = R.string.msg_no_project_desc
                        )
                    }
                }
            }


            HideOnScrollComponent(
                lazyListState = lazyListState
            ) {
                Button(
                    onClick = {
                        navController.navigate("project/create") {
                            launchSingleTop = true
                        }
                    },

                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth()
                        .hozPadding()
                ) {
                    Text(
                        text = "Crate new project"
                    )
                }
            }
        }
    }
}