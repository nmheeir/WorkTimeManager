package com.kt.worktimetrackermanager.presentation.screens.task

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.presentation.viewmodels.TaskDetailViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.ext.parseDate
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.ui.EmptyBox
import com.kt.worktimetrackermanager.core.presentation.ui.EmptyBoxAction
import com.kt.worktimetrackermanager.presentation.components.EmptyCardState
import com.kt.worktimetrackermanager.presentation.components.chip.PriorityChip
import com.kt.worktimetrackermanager.presentation.components.chip.StatusChip
import com.kt.worktimetrackermanager.presentation.components.dialog.DefaultDialog
import com.kt.worktimetrackermanager.presentation.components.image.CoilImage
import com.kt.worktimetrackermanager.presentation.components.items.ReportCardItem
import kotlinx.collections.immutable.persistentListOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavHostController,
    viewModel: TaskDetailViewModel = hiltViewModel(),
) {
    val task by viewModel.task.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val lazyListState = rememberLazyListState()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            // TODO: Shimmer
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                if (task == null) {
                    item {
                        Text(
                            text = "No task"
                        )
                    }
                }
                task.takeIf { it != null }?.let { task ->
                    item(
                        key = "top_bar"
                    ) {
                        TopAppBar(
                            title = {
                                Text(
                                    text = task.name
                                )
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
                                    enabled = task.reports.isEmpty(),
                                    onClick = {

                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_edit),
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                    }

                    item(
                        key = "header"
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                            modifier = Modifier.hozPadding()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = task.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Created " + task.createdAt.parseDate(),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            StatusChip(status = task.status) { }
                        }
                    }

                    item(
                        key = "description"
                    ) {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.background
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .hozPadding()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier.padding(MaterialTheme.padding.small)
                            ) {
                                Text(
                                    text = "Description",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                                Text(
                                    text = task.description,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    item(
                        key = "priority"
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .hozPadding()
                        ) {
                            Text(
                                text = stringResource(R.string.label_priority),
                                style = MaterialTheme.typography.titleMedium
                            )
                            PriorityChip(priority = task.priority) { }
                        }
                    }

                    item {
                        Text(
                            text = "Assignees",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.hozPadding()
                        )
                    }

                    if (task.assignees.isEmpty()) {
                        item {
                            EmptyBox(
                                stringRes = R.string.msg_no_assignees,
                                descRes = R.string.msg_no_assignees_desc,
                                modifier = Modifier
                                    .hozPadding()
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        MaterialTheme.shapes.small
                                    ),
                                actions = persistentListOf(
                                    EmptyBoxAction(
                                        stringRes = R.string.label_add_assignee,
                                        icon = R.drawable.ic_edit,
                                        onClick = {

                                        }
                                    )
                                )
                            )
                        }
                    } else {
                        items(
                            items = task.assignees,
                            key = {
                                it.userName
                            }
                        ) { profile ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .hozPadding()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                                    modifier = Modifier.padding(MaterialTheme.padding.small)
                                ) {
                                    CoilImage(
                                        imageUrl = profile.avatarUrl,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(32.dp)
                                    )
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
                                        horizontalAlignment = Alignment.Start,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = profile.userName
                                        )
                                        Text(
                                            text = profile.userFullName
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Reports",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.hozPadding()
                        )
                    }

                    if (task.reports.isEmpty()) {
                        item {
                            EmptyBox(
                                stringRes = R.string.msg_no_assignees,
                                descRes = R.string.msg_no_assignees_desc,
                                modifier = Modifier
                                    .hozPadding()
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        MaterialTheme.shapes.small
                                    ),
                                actions = persistentListOf(
                                    EmptyBoxAction(
                                        stringRes = R.string.label_add_assignee,
                                        icon = R.drawable.ic_edit,
                                        onClick = {

                                        }
                                    )
                                )
                            )
                        }
                    } else {
                        items(
                            items = task.reports,
                            key = { it.id }
                        ) { report ->
                            ReportCardItem(
                                report = report,
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.setDataAndType(
                                        report.reportUrl.toUri(),
                                        "application/pdf"
                                    )
                                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                                    context.startActivity(intent)
                                },
                                modifier = Modifier.hozPadding()
                            )
                        }
                    }
                }
            }
        }
    }
}