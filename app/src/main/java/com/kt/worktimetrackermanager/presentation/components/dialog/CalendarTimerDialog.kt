package com.kt.worktimetrackermanager.presentation.components.dialog

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.presentation.components.calendar.CalendarDisplay
import java.time.LocalDateTime

@Composable
fun CalendarTimerDialog(
    currentDateTime: LocalDateTime = LocalDateTime.now(),
    onDismiss: () -> Unit,
    onCurrentDateTimeChange: (LocalDateTime) -> Unit,
) {
    var currentStep by remember { mutableIntStateOf(0) }

    val (dateTime, onDateTimeChange) = remember { mutableStateOf(currentDateTime) }

    DefaultDialog(
        onDismiss = {
            if (currentStep == 0) onDismiss() else currentStep--
        },
        buttons = {
            TextButton(
                onClick = {
                    if (currentStep == 0) onDismiss() else currentStep--
                }
            ) {
                Text(
                    text = stringResource(if (currentStep == 0) R.string.cancel else R.string.back)
                )
            }
            TextButton(
                onClick = {
                    if (currentStep == 0) currentStep++
                    else {
                        onCurrentDateTimeChange(dateTime)
                        onDismiss()
                    }
                }
            ) {
                Text(text = stringResource(if (currentStep == 0) R.string.next else R.string.ok))
            }
        }
    ) {
        when (currentStep) {
            0 -> {
                CalendarDisplay(
                    date = dateTime.toLocalDate(),
                    onDateChange = {
                        onDateTimeChange(dateTime.with(it))
                    }
                )
            }

            1 -> {
                WheelTimePickerDialog(
                    taskTime = dateTime.toLocalTime(),
                    onTaskTimeChange = {
                        onDateTimeChange(dateTime.with(it))
                    }
                )
            }
        }
    }
}