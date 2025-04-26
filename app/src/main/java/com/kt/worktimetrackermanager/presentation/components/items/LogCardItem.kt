package com.kt.worktimetrackermanager.presentation.components.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kt.worktimetrackermanager.core.ext.format3
import com.kt.worktimetrackermanager.core.presentation.clickable
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.ui.AppTheme
import com.kt.worktimetrackermanager.core.presentation.utils.Gap
import com.kt.worktimetrackermanager.data.remote.dto.enums.CheckType
import com.kt.worktimetrackermanager.data.remote.dto.enums.LogStatus
import com.kt.worktimetrackermanager.data.remote.dto.response.LogModel
import com.kt.worktimetrackermanager.presentation.components.dialog.DefaultDialog
import com.kt.worktimetrackermanager.presentation.components.image.CircleImage
import com.kt.worktimetrackermanager.presentation.fakeLogs
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import com.kt.worktimetrackermanager.presentation.viewmodels.TeamLogUiAction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LogCardItem(
    modifier: Modifier = Modifier,
    log: LogModel,
    onClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        LogContent(log = log, modifier = Modifier.hozPadding())
    }
}

@Composable
fun LogDetailDialog(
    log: LogModel,
    onDismiss: () -> Unit,
    onAction: (TeamLogUiAction) -> Unit,
) {
    DefaultDialog(
        onDismiss = onDismiss,
        buttons = {
            if (log.status == LogStatus.Pending) {
                TextButton(
                    onClick = {
                        onAction(TeamLogUiAction.UpdateStatus(log.id, LogStatus.Rejected))
                        onDismiss()
                    }
                ) {
                    Text(text = "Rejected")
                }
                TextButton(
                    onClick = {
                        onAction(TeamLogUiAction.UpdateStatus(log.id, LogStatus.Approved))
                        onDismiss()
                    }
                ) {
                    Text(text = "Approved")
                }
            }
        }
    ) {
        LogContent(log = log, isShowFull = true)
    }
}

@Composable
private fun LogContent(
    modifier: Modifier = Modifier,
    log: LogModel,
    isShowFull: Boolean = false,
) {
    val badgeTypeContainerColor = when (log.type) {
        CheckType.CheckIn -> MaterialTheme.colorScheme.primary
        CheckType.CheckOut -> MaterialTheme.colorScheme.error
    }
    val badgeTypeContentColor = when (log.type) {
        CheckType.CheckIn -> MaterialTheme.colorScheme.onPrimary
        CheckType.CheckOut -> MaterialTheme.colorScheme.onError
    }
    val badgeStatusContainerColor = when (log.status) {
        LogStatus.Approved -> MaterialTheme.colorScheme.primaryContainer
        LogStatus.Rejected -> MaterialTheme.colorScheme.errorContainer
        LogStatus.Pending -> MaterialTheme.colorScheme.secondaryContainer
    }
    val badgeStatusContentColor = when (log.status) {
        LogStatus.Approved -> MaterialTheme.colorScheme.onPrimaryContainer
        LogStatus.Rejected -> MaterialTheme.colorScheme.onErrorContainer
        LogStatus.Pending -> MaterialTheme.colorScheme.onSecondaryContainer
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.mediumSmall),
        modifier = modifier
    ) {
        //User information
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircleImage(
                    imageUrl = log.user.avatarUrl ?: "",
                    size = 36.dp
                )
                Text(
                    text = log.user.userFullName,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f)
                )
                Badge(
                    containerColor = badgeTypeContainerColor,
                    contentColor = badgeTypeContentColor
                ) {
                    Text(
                        text = log.type.value,
                        modifier = Modifier.padding(MaterialTheme.padding.extraSmall)
                    )
                }
                Badge(
                    containerColor = badgeStatusContainerColor,
                    contentColor = badgeStatusContentColor
                ) {
                    Text(
                        text = log.status.name,
                        modifier = Modifier.padding(MaterialTheme.padding.extraSmall)
                    )
                }
            }
            Gap(height = MaterialTheme.padding.small)
            Text(
                text = log.user.department + " - " + log.user.designation,
                style = MaterialTheme.typography.labelMedium
            )
        }

        if (isShowFull) {
            HorizontalDivider()
        }

        Text(
            text = log.reason,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (!isShowFull) 2 else Int.MAX_VALUE,
            overflow = TextOverflow.Ellipsis
        )

        if (isShowFull) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Check time",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = log.checkTime.format3(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Approval time",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = if (log.approvalTime != null) log.approvalTime.format3() else "N/A",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Create at",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = log.createAt.format3(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun Test() {
    WorkTimeTrackerManagerTheme(
        appTheme = AppTheme.MONET
    ) {
        LogCardItem(
            log = fakeLogs[0]
        ) { }
    }
}