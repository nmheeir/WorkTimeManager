package com.kt.worktimetrackermanager.presentation.components.items

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kt.worktimetrackermanager.core.ext.format2
import com.kt.worktimetrackermanager.core.ext.format3
import com.kt.worktimetrackermanager.core.ext.parseDate
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.remote.dto.response.Shift
import com.kt.worktimetrackermanager.presentation.components.image.CircleImage
import com.kt.worktimetrackermanager.presentation.fakeShifts
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import java.nio.file.WatchEvent

@Composable
fun ShiftCardItem(
    modifier: Modifier = Modifier,
    shift: Shift,
) {
    var showCheckRecord by rememberSaveable { mutableStateOf(false) }
    Card(
        onClick = {
            showCheckRecord = !showCheckRecord
        },
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.padding.small)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
        ) {
            if (shift.user != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircleImage(
                        imageUrl = shift.user.avatarUrl ?: "",
                        size = 24.dp
                    )
                    Text(
                        text = shift.user.userFullName,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = MaterialTheme.padding.small)
                    )
                }
            }

            ShiftTimeSection(
                label1 = "Start",
                label2 = "End",
                value1 = shift.start.format3(),
                value2 = shift.end.format3()
            )

            AnimatedVisibility(
                visible = showCheckRecord
            ) {
                if (shift.checkIn != null && shift.checkOut != null) {
                    ShiftTimeSection(
                        label1 = "Check In",
                        label2 = "Check Out",
                        value1 = shift.checkIn.format3(),
                        value2 = shift.checkOut.format3()
                    )
                } else {
                    Text(
                        text = "No check"
                    )
                }
            }
        }
    }
}

@Composable
private fun ShiftTimeSection(
    modifier: Modifier = Modifier,
    label1: String,
    label2: String,
    value1: String,
    value2: String,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label1
            )
            Text(
                text = value1
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label2
            )
            Text(
                text = value2
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Test() {
    WorkTimeTrackerManagerTheme {
        ShiftCardItem(
            shift = fakeShifts[0]
        )
    }
}
