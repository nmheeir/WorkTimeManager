package com.kt.worktimetrackermanager.presentation.components.chip

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kt.worktimetrackermanager.data.remote.dto.enum.Priority
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme

@Composable
fun PriorityChip(
    modifier: Modifier = Modifier,
    priority: Priority,
    onClick: () -> Unit,
) {
    val bgColor = when (priority) {
        Priority.Low -> MaterialTheme.colorScheme.secondaryContainer
        Priority.Medium -> MaterialTheme.colorScheme.tertiaryContainer
        Priority.High -> MaterialTheme.colorScheme.errorContainer
    }
    val labelColor = when (priority) {
        Priority.Low -> MaterialTheme.colorScheme.onSecondaryContainer
        Priority.Medium -> MaterialTheme.colorScheme.onTertiaryContainer
        Priority.High -> MaterialTheme.colorScheme.onErrorContainer
    }
    SuggestionChip(
        onClick = onClick,
        label = {
            Text(
                text = priority.name,
                style = MaterialTheme.typography.bodySmall
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = bgColor,
            labelColor = labelColor
        ),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun Test() {
    WorkTimeTrackerManagerTheme {
        PriorityChip(
            priority = Priority.High,
            onClick = {

            }
        )
    }
}