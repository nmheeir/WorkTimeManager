package com.kt.worktimetrackermanager.presentation.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.ext.parse
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.presentation.components.EmptyCardState
import com.kt.worktimetrackermanager.presentation.components.chart.AttendanceEachTime
import com.kt.worktimetrackermanager.presentation.components.chart.AttendanceRateChart
import com.kt.worktimetrackermanager.presentation.components.dialog.CalendarDialog
import com.kt.worktimetrackermanager.presentation.viewmodels.StaffDashboardUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.StaffDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffDashboardScreen(
    navController: NavHostController,
    viewModel: StaffDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    var showStartDateDialog by remember { mutableStateOf(false) }
    var showEndDateDialog by remember { mutableStateOf(false) }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        state = lazyListState,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item(
            key = "top_bar"
        ) {
            val staffDetail by remember {
                derivedStateOf { uiState.staffDetail }
            }

            TopAppBar(
                title = {
                    Text(
                        text = staffDetail?.userName ?: "Unknown user"
                    )
                }
            )
        }

        item(
            key = "time_query"
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    TextButton(
                        onClick = {
                            showStartDateDialog = true
                        }
                    ) {
                        val startDate by remember {
                            derivedStateOf { uiState.start }
                        }
                        Text(
                            text = startDate.parse(),
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (showStartDateDialog) {
                            CalendarDialog(
                                date = startDate,
                                onDateChange = {
                                    viewModel.onAction(StaffDashboardUiAction.OnStartDateChange(it))
                                    showStartDateDialog = false
                                },
                                onDismiss = { showStartDateDialog = false }
                            )
                        }

                    }

                    TextButton(
                        onClick = {
                            showEndDateDialog = true
                        }
                    ) {
                        val endDate by remember {
                            derivedStateOf { uiState.end }
                        }
                        Text(
                            text = endDate.parse(),
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (showEndDateDialog) {
                            CalendarDialog(
                                date = endDate,
                                onDateChange = {
                                    viewModel.onAction(StaffDashboardUiAction.OnEndDateChange(it))
                                    showEndDateDialog = false
                                },
                                onDismiss = { showEndDateDialog = false }
                            )
                        }
                    }
                }

                TextButton(
                    onClick = {
                        viewModel.onAction(StaffDashboardUiAction.FetchData)
                    }
                ) {
                    Text(text = "Query")
                }
            }
        }

        item(
            key = "attendance_record"
        ) {
            val attendanceRecord by remember {
                derivedStateOf { uiState.attendanceRecord }
            }

            if (attendanceRecord == null) {
                EmptyCardState(
                    message = R.string.msg_no_task,
                    desc = R.string.attendant,
                    icon = R.drawable.img_task
                )
            } else {
                AttendanceRateChart(
                    modifier = Modifier.padding(horizontal = MaterialTheme.padding.mediumSmall),
                    title = "Attendance Rate",
                    attendanceRecord = attendanceRecord!!
                )
            }
        }

        item(
            key = "attendance_each_time"
        ) {
            val recordEachTime by remember {
                derivedStateOf { uiState.staffAttendanceRecordEachTime }
            }
            val period by remember {
                derivedStateOf { uiState.period }
            }
            AttendanceEachTime(
                data = recordEachTime,
                action = {
                    viewModel.onAction(StaffDashboardUiAction.OnPeriodChange(it))
                },
                period = period,
                modifier = Modifier.padding(horizontal = MaterialTheme.padding.mediumSmall)
            )
        }
    }
}