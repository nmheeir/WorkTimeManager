package com.kt.worktimetrackermanager.presentation.components.items

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.remote.dto.response.TeamShiftStats

@Composable
fun TeamShiftStatCardItem(
    modifier: Modifier = Modifier,
    stat: TeamShiftStats,
    onClick: () -> Unit,
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
            modifier = Modifier.padding(MaterialTheme.padding.small)
        ) {
            Text(
                text = stat.team.name,
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stat.team.latitude.toString()
                )
                Text(
                    text = stat.team.longitude.toString()
                )
            }

            HorizontalDivider()
            StatRow(
                label = "Total Shift: ",
                value = stat.totalShifts.toString()
            )
            StatRow(
                label = "Normal Shift: ",
                value = stat.normalShifts.toString()
            )
            StatRow(
                label = "Night Shift: ",
                value = stat.nightShifts.toString()
            )
        }
    }
}

@Composable
private fun StatRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f)
        )
    }
}