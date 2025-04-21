package com.kt.worktimetrackermanager.presentation.screens.log

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.ui.EmptyBox
import com.kt.worktimetrackermanager.core.presentation.utils.HandleLoadMoreTrigger
import com.kt.worktimetrackermanager.data.remote.dto.enums.CheckType
import com.kt.worktimetrackermanager.data.remote.dto.enums.LogStatus
import com.kt.worktimetrackermanager.presentation.components.items.LogCardItem
import com.kt.worktimetrackermanager.presentation.components.items.LogDetailDialog
import com.kt.worktimetrackermanager.presentation.components.preference.PreferenceGroupTitle
import com.kt.worktimetrackermanager.presentation.viewmodels.TeamLogUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.TeamLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamLogScreen(
    navController: NavHostController,
    viewModel: TeamLogViewModel = hiltViewModel(),
) {
    val lazyListState = rememberLazyListState()

    val logs by viewModel.logs.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isLoadMore by viewModel.isLoadMore.collectAsStateWithLifecycle()

    val checkTypeFilter by viewModel.checkTypeFilter.collectAsStateWithLifecycle()
    val statusFilter by viewModel.statusFilter.collectAsStateWithLifecycle()

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.layoutInfo.visibleItemsInfo.any { it.key == "load_more" }
        }.collect { shouldShowLoadMore ->
            if (!shouldShowLoadMore) return@collect
            viewModel.loadMore()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Team logs"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navController::navigateUp
                    ) {
                        Icon(painterResource(R.drawable.ic_keyboard_arrow_left), null)
                    }
                },
                actions = {
                    var showFilterSheet by remember { mutableStateOf(false) }
                    IconButton(
//                        enabled = logs.isNotEmpty(),
                        onClick = { showFilterSheet = true }
                    ) {
                        Icon(painterResource(R.drawable.ic_page_info), null)
                    }
                    if (showFilterSheet) {
                        TabFilterSheet(
                            onDismiss = {
                                showFilterSheet = false
                            },
                            onAction = viewModel::onAction,
                            typeFilter = checkTypeFilter,
                            statusFilter = statusFilter
                        )
                    }
                }
            )
        }
    ) { pv ->
        LazyColumn(
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.mediumSmall),
            contentPadding = pv
        ) {
            if (logs.isEmpty()) {
                item(
                    key = "no_data"
                ) {
                    EmptyBox(
                        stringRes = R.string.msg_no_data,
                        descRes = R.string.msg_no_data_desc,
                        iconRes = R.drawable.img_nodata
                    )
                }
            }

            items(
                items = logs
            ) { log ->
                Box {
                    var showDetailDialog by remember { mutableStateOf(false) }
                    LogCardItem(
                        log = log,
                        onClick = {
                            showDetailDialog = true
                        },
                        modifier = Modifier
                    )
                    if (showDetailDialog) {
                        LogDetailDialog(
                            log = log,
                            onDismiss = { showDetailDialog = false },
                            onAction = viewModel::onAction
                        )
                    }
                }
            }

            if (isLoadMore && logs.isNotEmpty()) {
                item(
                    key = "load_more"
                ) {
                    CircularProgressIndicator(modifier = Modifier.padding(vertical = MaterialTheme.padding.mediumSmall))
                }
            } else {
                item {
                    Text(
                        text = "No result found",
                        modifier = Modifier.padding(bottom = MaterialTheme.padding.mediumSmall)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TabFilterSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    typeFilter: CheckType?,
    statusFilter: LogStatus?,
    onAction: (TeamLogUiAction) -> Unit,
) {

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        PreferenceGroupTitle(title = "Status")

        LogStatus.entries.fastForEach { status ->
            FilterRow(
                title = status.name,
                onClick = {
                    if (status != statusFilter) {
                        onAction(TeamLogUiAction.ChangeStatusFilter(status))
                    } else {
                        onAction(TeamLogUiAction.ChangeStatusFilter(null))
                    }
                },
                trailingContent = {
                    Checkbox(
                        checked = statusFilter != null && status == statusFilter,
                        onCheckedChange = { isCheck ->
                            if (isCheck) {
                                onAction(TeamLogUiAction.ChangeStatusFilter(status))
                            } else {
                                onAction(TeamLogUiAction.ChangeStatusFilter(null))
                            }
                        }
                    )
                }
            )
        }

        PreferenceGroupTitle(title = "Check Type")
        CheckType.entries.fastForEach { type ->
            FilterRow(
                title = type.value,
                onClick = {
                    if (type != typeFilter) {
                        onAction(TeamLogUiAction.ChangeCheckTypeFilter(type))
                    } else {
                        onAction(TeamLogUiAction.ChangeCheckTypeFilter(null))
                    }
                },
                trailingContent = {
                    Checkbox(
                        checked = typeFilter != null && type == typeFilter,
                        onCheckedChange = { isCheck ->
                            if (isCheck) {
                                onAction(TeamLogUiAction.ChangeCheckTypeFilter(type))
                            } else {
                                onAction(TeamLogUiAction.ChangeCheckTypeFilter(null))
                            }
                        }
                    )
                }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            TextButton(
                shape = MaterialTheme.shapes.small,
                onClick = {
                    onAction(TeamLogUiAction.ResetFilter)
                },
                modifier = Modifier
                    .weight(1f)
                    .hozPadding()
            ) {
                Text(text = "Reset")
            }
            TextButton(
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                onClick = {
                    onAction(TeamLogUiAction.Search)
                },
                modifier = Modifier
                    .weight(1f)
                    .hozPadding()
            ) {
                Text(text = "Apply")
            }
        }
    }
}

@Composable
private fun FilterRow(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    trailingContent: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .hozPadding()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
        trailingContent()
    }
}