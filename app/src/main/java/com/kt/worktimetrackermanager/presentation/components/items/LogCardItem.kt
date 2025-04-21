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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kt.worktimetrackermanager.core.ext.format3
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.remote.dto.enums.CheckType
import com.kt.worktimetrackermanager.data.remote.dto.response.LogModel
import com.kt.worktimetrackermanager.presentation.components.image.CircleImage
import com.kt.worktimetrackermanager.presentation.fakeLogs
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LogCardItem(
    modifier: Modifier = Modifier,
    log: LogModel,
    onClick: () -> Unit,
) {
    val badgeContainerColor = when (log.type) {
        CheckType.CheckIn -> MaterialTheme.colorScheme.primary
        CheckType.CheckOut -> MaterialTheme.colorScheme.error
    }
    val badgeContentColor = when (log.type) {
        CheckType.CheckIn -> MaterialTheme.colorScheme.onPrimary
        CheckType.CheckOut -> MaterialTheme.colorScheme.onError
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
            modifier = Modifier.padding(MaterialTheme.padding.small)
        ) {
            //User information
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
                    containerColor = badgeContainerColor,
                    contentColor = badgeContentColor
                ) {
                    Text(
                        text = log.type.value,
                        modifier = Modifier.padding(MaterialTheme.padding.extraSmall)
                    )
                }
            }
            Text(
                text = log.reason,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = log.checkTime.format3(),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Test() {
    WorkTimeTrackerManagerTheme {
        LogCardItem(
            log = fakeLogs[0]
        ) { }
    }
}