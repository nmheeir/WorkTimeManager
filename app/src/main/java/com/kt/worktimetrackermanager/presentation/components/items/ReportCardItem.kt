package com.kt.worktimetrackermanager.presentation.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.remote.dto.response.Report
import com.kt.worktimetrackermanager.presentation.components.dialog.DefaultDialog

@Composable
fun ReportCardItem(
    modifier: Modifier = Modifier,
    report: Report,
    onClick: () -> Unit,
) {
    Card(
        shape = MaterialTheme.shapes.extraSmall,
        onClick = onClick,
        modifier = modifier
    ) {
        var showReportDetail by remember { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
            modifier = Modifier.padding(MaterialTheme.padding.extraSmall)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_pdf),
                contentDescription = null
            )
            Text(
                text = report.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { showReportDetail = true }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_more_horiz),
                    contentDescription = null
                )
                if (showReportDetail) {
                    DefaultDialog(
                        onDismiss = { showReportDetail = false }
                    ) {
                        Text(
                            text = report.title
                        )
                        Text(
                            text = report.description
                        )
                        Text(
                            text = report.createdAt.toString()
                        )
                    }
                }
            }
        }
    }
}