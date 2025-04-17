package com.kt.worktimetrackermanager.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.presentation.components.EmptyCardState
import com.kt.worktimetrackermanager.presentation.components.dialog.AlertDialog
import com.kt.worktimetrackermanager.presentation.components.items.NotificationCardItem
import com.kt.worktimetrackermanager.presentation.viewmodels.NotificationUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotificationScreen(
    navController: NavHostController,
    viewModel: NotificationViewModel = hiltViewModel(),
) {

    val haptic = LocalHapticFeedback.current

    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    var inSelectMode by remember { mutableStateOf(false) }
    val selection = rememberSaveable(
        saver = listSaver<MutableList<Int>, Int>(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    ) { mutableStateListOf() }
    val onExitSelectionMode = {
        inSelectMode = false
        selection.clear()
    }
    if (inSelectMode) {
        BackHandler(onBack = onExitSelectionMode)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (inSelectMode) {
                        Text(
                            pluralStringResource(
                                R.plurals.n_selected,
                                selection.size,
                                selection.size
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.notification)
                        )
                    }
                },
                navigationIcon = {
                    if (inSelectMode) {
                        IconButton(
                            onClick = onExitSelectionMode
                        ) {
                            Icon(painterResource(R.drawable.ic_close), null)
                        }
                    } else {
                        IconButton(
                            onClick = navController::navigateUp
                        ) {
                            Icon(painterResource(R.drawable.ic_keyboard_arrow_left), null)
                        }
                    }
                },
                actions = {
                    var showDeleteAllDialog by remember { mutableStateOf(false) }
                    var showMarkAllAsReadDialog by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = {
                            showDeleteAllDialog = true
                        },
                        enabled = notifications.isNotEmpty()
                    ) {
                        Icon(painterResource(R.drawable.ic_delete), null)
                        if (showDeleteAllDialog && notifications.isNotEmpty()) {
                            AlertDialog(
                                title = stringResource(R.string.alert_delete_all),
                                text = stringResource(R.string.alert_delete_all_desc),
                                onDismiss = { showDeleteAllDialog = false },
                                onConfirm = {
                                    if (!inSelectMode) {
                                        viewModel.onAction(NotificationUiAction.DeleteAll)
                                    } else {
                                        viewModel.onAction(
                                            NotificationUiAction.DeleteMultipleNotifications(
                                                selection
                                            )
                                        )
                                        onExitSelectionMode()
                                    }
                                    showDeleteAllDialog = false
                                }
                            )
                        }
                    }

                    IconButton(
                        onClick = { showMarkAllAsReadDialog = true },
                        enabled = notifications.isNotEmpty()
                    ) {
                        Icon(painterResource(R.drawable.ic_check_circle), null)
                        if (showMarkAllAsReadDialog && notifications.isNotEmpty()) {
                            AlertDialog(
                                title = stringResource(R.string.alert_mark_all_as_read),
                                onDismiss = { showMarkAllAsReadDialog = false },
                                onConfirm = {
                                    if (!inSelectMode) {
                                        viewModel.onAction(NotificationUiAction.MarkAllAsRead)
                                    } else {
                                        viewModel.onAction(
                                            NotificationUiAction.MarkMultipleAsRead(
                                                selection
                                            )
                                        )
                                        onExitSelectionMode()
                                    }
                                    showMarkAllAsReadDialog = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            if (notifications.isEmpty()) {
                item(
                    key = "no_data"
                ) {
                    EmptyCardState(
                        message = R.string.msg_no_data,
                        desc = R.string.msg_no_data_desc,
                        icon = R.drawable.img_nodata,
                        modifier = Modifier.hozPadding()
                    )
                }
            }

            items(
                items = notifications,
                key = { it.id }
            ) { notification ->
                val onCheckedChange: (Boolean) -> Unit = {
                    if (it) {
                        selection.add(notification.id)
                    } else {
                        selection.remove(notification.id)
                    }
                }

                NotificationCardItem(
                    notification = notification,
                    trailingContent = {
                        if (inSelectMode) {
                            Checkbox(
                                checked = notification.id in selection,
                                onCheckedChange = { selected ->
                                    onCheckedChange(selected)
                                }
                            )
                        } else if (notification.isRead) {
                            Icon(painterResource(R.drawable.ic_check), null)
                        } else {
                            TextButton(
                                onClick = {
                                    viewModel.onAction(
                                        NotificationUiAction.MarkAsRead(
                                            notification
                                        )
                                    )
                                }
                            ) {
                                Text(text = "Mark as read")
                            }
                        }
                    },
                    modifier = Modifier
                        .hozPadding()
                        .combinedClickable(
                            onClick = {
                                if (inSelectMode) {
                                    onCheckedChange(!selection.contains(notification.id))
                                }
                            },
                            onLongClick = {
                                if (!inSelectMode) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    inSelectMode = true
                                    selection.add(notification.id)
                                }
                            }
                        )
                )
            }
        }
    }
}