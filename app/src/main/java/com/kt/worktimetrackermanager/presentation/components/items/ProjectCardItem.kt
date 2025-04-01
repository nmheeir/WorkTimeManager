package com.kt.worktimetrackermanager.presentation.components.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ChipColors
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.ext.parseDate
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus
import com.kt.worktimetrackermanager.data.remote.dto.response.Project
import com.kt.worktimetrackermanager.presentation.fakeProjects
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import java.sql.RowId

@Composable
fun ProjectCardItem(
    project: Project,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .hozPadding()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(MaterialTheme.padding.small)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bolt),
                        contentDescription = null
                    )
                    Text(
                        text = project.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                SuggestionChip(
                    onClick = {},
                    shape = MaterialTheme.shapes.large,
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = when (project.status) {
                            ProjectStatus.NotStarted -> MaterialTheme.colorScheme.outline
                            ProjectStatus.InProgress -> MaterialTheme.colorScheme.primary
                            ProjectStatus.Completed -> MaterialTheme.colorScheme.tertiary
                            ProjectStatus.OnHold -> MaterialTheme.colorScheme.secondary
                            ProjectStatus.Cancelled -> MaterialTheme.colorScheme.error
                        },
                        labelColor =
                            when (project.status) {
                                ProjectStatus.NotStarted -> MaterialTheme.colorScheme.surface
                                ProjectStatus.InProgress -> MaterialTheme.colorScheme.onPrimary
                                ProjectStatus.Completed -> MaterialTheme.colorScheme.onTertiary
                                ProjectStatus.OnHold -> MaterialTheme.colorScheme.onSecondary
                                ProjectStatus.Cancelled -> MaterialTheme.colorScheme.onError
                            }
                    ),
                    label = {
                        Text(
                            text = project.status.name,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                )
            }


            if (project.tasks.isNotEmpty()) {
                Text(
                    text = "Completed: ${project.tasks.filter { it.status == ProjectStatus.Completed }.size} / ${project.tasks.size}",
                    style = MaterialTheme.typography.bodyMedium
                )
                LinearProgressIndicator(
                    progress = {
                        project.tasks.filter {
                            it.status == ProjectStatus.Completed
                        }.size.toFloat() / project.tasks.size.toFloat()
                    },
                    color = ProgressIndicatorDefaults.linearColor,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = "No tasks",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_calendar_2),
                    contentDescription = null
                )
                Text(
                    text = "Due: ${project.endDate.parseDate()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Test() {
    WorkTimeTrackerManagerTheme {
        ProjectCardItem(
            project = fakeProjects[0],
            onClick = {}
        )
    }
}