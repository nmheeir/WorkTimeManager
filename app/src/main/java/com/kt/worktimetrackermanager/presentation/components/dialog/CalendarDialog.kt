package com.kt.worktimetrackermanager.presentation.components.dialog

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.presentation.components.calendar.CalendarDisplay
import java.time.LocalDate

@Composable
fun CalendarDialog(
    date: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    DefaultDialog(
        onDismiss = onDismiss,
        buttons = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(stringResource(R.string.cancel))
            }
            TextButton(
                onClick = {
                    onDateChange(date)
                },
            ) {
                Text(stringResource(R.string.ok))
            }
        },
    ) {
        CalendarDisplay(date = date, onDateChange = onDateChange)
    }
}

