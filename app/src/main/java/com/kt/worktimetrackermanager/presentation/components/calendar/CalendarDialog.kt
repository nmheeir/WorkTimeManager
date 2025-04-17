package com.kt.worktimetrackermanager.presentation.components.calendar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import timber.log.Timber
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCalendarDialog(
    modifier: Modifier = Modifier,
    event: (LocalDate?) -> Unit,
    showDialog: () -> Boolean,
    selectedDate: LocalDate?
) {
    val _selectedDate = remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    CalendarDialog(
        state = rememberUseCaseState(
            visible = showDialog(),
            embedded = true,
            onCloseRequest = {
                event(null)
            },
            onFinishedRequest = {
                event(_selectedDate.value)
                Timber.d( "selectedTime: ${_selectedDate.value}")
            }
        ),
        config = CalendarConfig(
            yearSelection = true,
            style = CalendarStyle.MONTH,
        ),
        selection = CalendarSelection.Date(
            selectedDate = selectedDate
        ) { newDate ->
            _selectedDate.value = newDate
        },
    )
}