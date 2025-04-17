package com.kt.worktimetrackermanager.presentation.components.chip

import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kt.worktimetrackermanager.data.remote.dto.enums.ProjectStatus

@Composable
fun StatusChip(
    modifier: Modifier = Modifier,
    status: ProjectStatus,
    onClick: () -> Unit,
) {
    val bgColor = when (status) {
        ProjectStatus.NotStarted -> MaterialTheme.colorScheme.outline
        ProjectStatus.InProgress -> MaterialTheme.colorScheme.primary
        ProjectStatus.Completed -> MaterialTheme.colorScheme.tertiary
        ProjectStatus.OnHold -> MaterialTheme.colorScheme.secondary
        ProjectStatus.Cancelled -> MaterialTheme.colorScheme.error
    }
    val labelColor = when (status) {
        ProjectStatus.NotStarted -> MaterialTheme.colorScheme.surface
        ProjectStatus.InProgress -> MaterialTheme.colorScheme.onPrimary
        ProjectStatus.Completed -> MaterialTheme.colorScheme.onTertiary
        ProjectStatus.OnHold -> MaterialTheme.colorScheme.onSecondary
        ProjectStatus.Cancelled -> MaterialTheme.colorScheme.onError
    }

    SuggestionChip(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        label = {
            Text(
                text = status.title,
                style = MaterialTheme.typography.bodySmall
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = bgColor,
            labelColor = labelColor
        ),
        modifier = modifier
    )
}