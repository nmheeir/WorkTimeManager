package com.kt.worktimetrackermanager.presentation.screens.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.ui.components.HideOnScrollComponent
import com.kt.worktimetrackermanager.core.presentation.utils.Gap
import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus
import com.kt.worktimetrackermanager.presentation.components.EmptyCardState
import com.kt.worktimetrackermanager.presentation.components.items.ProjectCardItem
import com.kt.worktimetrackermanager.presentation.viewmodels.ProjectViewModel

@Composable
fun ProjectScreen(
    navController: NavHostController,
    viewModel: ProjectViewModel = hiltViewModel(),
) {
    val lazyListState = rememberLazyListState()
    var selectedChoice by rememberSaveable { mutableStateOf(ProjectStatus.NotStarted) }

    val projects by viewModel.projects.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    // TODO: Handle loading animation

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
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
                                            text = projects.filter {
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
                key = "choices"
            ) {
                val choices = ProjectStatus.entries
                LazyRow(
                    contentPadding = PaddingValues(horizontal = MaterialTheme.padding.mediumSmall),
                ) {
                    item {
                        SingleChoiceSegmentedButtonRow {
                            choices.fastForEachIndexed { index, title ->
                                SegmentedButton(
                                    selected = index == selectedChoice.ordinal,
                                    onClick = {
                                        selectedChoice = title
                                    },
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = index,
                                        count = ProjectStatus.entries.size
                                    )
                                ) {
                                    Text(
                                        text = title.title,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // TODO: Need to change to HorizontalPager
            val filterProjects by derivedStateOf {
                projects.filter {
                    it.status == selectedChoice
                }
            }

            if (filterProjects.isNotEmpty()) {
                items(
                    items = filterProjects,
                    key = {
                        it.id
                    }
                ) { project ->
                    ProjectCardItem(
                        project = project,
                        onClick = {
                            navController.navigate("project/detail?id=${project.id}")
                        }
                    )
                    Gap(MaterialTheme.padding.small)
                }
            } else {
                item(
                    key = "no_project"
                ) {
                    EmptyCardState(
                        message = R.string.msg_no_project,
                        desc = R.string.msg_no_project_desc,
                        icon = R.drawable.img_nodata
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
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Crate new project"
                )
            }
        }
    }
}
