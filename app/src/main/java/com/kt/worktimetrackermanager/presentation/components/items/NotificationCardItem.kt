package com.kt.worktimetrackermanager.presentation.components.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.ext.format3
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.local.entities.NotificationEntity
import com.kt.worktimetrackermanager.data.remote.dto.enums.NotificationType
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import java.time.LocalDateTime

@Composable
fun NotificationCardItem(
    modifier: Modifier = Modifier,
    notification: NotificationEntity,
    trailingContent: @Composable () -> Unit = {},
) {
    val icon = when (notification.type) {
        NotificationType.AddLog -> R.drawable.ic_check_circle
        NotificationType.UpdateProfile -> R.drawable.ic_person
        NotificationType.ApprovedLog -> R.drawable.ic_check_circle
        NotificationType.DeniedLog -> R.drawable.ic_cancel
        NotificationType.UpdateShift -> R.drawable.ic_edit
        null -> R.drawable.ic_error
    }
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.medium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.padding.small)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = notification.description,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = notification.receivedAt.format3(),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            trailingContent()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Test() {
    WorkTimeTrackerManagerTheme {
        NotificationCardItem(
            notification = NotificationEntity(
                receivedUsername = "con cac",
                id = 1,
                title = "conasdc",
                description = "adkfjalkdjflakd",
                type = NotificationType.AddLog,
                isRead = false,
                receivedAt = LocalDateTime.now(),
            ),
            trailingContent = {
                Text(
                    text = "Mark as read"
                )
            }
        )
    }
}