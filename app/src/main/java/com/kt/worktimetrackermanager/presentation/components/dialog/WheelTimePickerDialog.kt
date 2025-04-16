package com.kt.worktimetrackermanager.presentation.components.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kt.worktimetrackermanager.core.presentation.ui.components.TimeFormat
import com.kt.worktimetrackermanager.presentation.components.wheel.WheelTimePicker
import java.time.LocalTime

@Composable
fun WheelTimePickerDialog(
    taskTime: LocalTime,
    onTaskTimeChange: (LocalTime) -> Unit,
) {
    var time by remember { mutableStateOf(taskTime) }
    WheelTimePicker(
        modifier = Modifier.fillMaxWidth(),
        startTime = time,
        timeFormat = TimeFormat.HOUR_24,
        onSnappedTime = { newTime ->
            time = newTime
            onTaskTimeChange(newTime)
        }
    )
}