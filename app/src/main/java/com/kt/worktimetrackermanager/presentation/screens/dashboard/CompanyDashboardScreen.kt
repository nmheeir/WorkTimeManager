package com.kt.worktimetrackermanager.presentation.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.ext.parse
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.presentation.components.EmptyCardState
import com.kt.worktimetrackermanager.presentation.components.chart.AttendanceEachTime
import com.kt.worktimetrackermanager.presentation.components.chart.AttendanceRateChart
import com.kt.worktimetrackermanager.presentation.components.dialog.CalendarDialog
import com.kt.worktimetrackermanager.presentation.components.items.TeamCardItem
import com.kt.worktimetrackermanager.presentation.components.widget.PreferenceGroupHeader
import com.kt.worktimetrackermanager.presentation.viewmodels.CompanyDashBoardViewModel
import com.kt.worktimetrackermanager.presentation.viewmodels.CompanyDashboardUiAction
import timber.log.Timber

@Composable
fun CompanyDashBoardScreen(
    navController: NavHostController,
    viewModel: CompanyDashBoardViewModel = hiltViewModel(),
) {
    val lazyListState = rememberLazyListState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                                    viewModel.onAction(CompanyDashboardUiAction.OnStartDateChange(it))
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
                                    viewModel.onAction(CompanyDashboardUiAction.OnEndDateChange(it))
                                    showEndDateDialog = false
                                },
                                onDismiss = { showEndDateDialog = false }
                            )
                        }
                    }
                }

                TextButton(
                    onClick = {
                        viewModel.onAction(CompanyDashboardUiAction.FetchData)
                    }
                ) {
                    Text(
                        text = "Query"
                    )
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
                derivedStateOf { uiState.companyAttendanceRecord }
            }
            val period by remember {
                derivedStateOf { uiState.period }
            }
            AttendanceEachTime(
                data = recordEachTime,
                action = {
                    viewModel.onAction(CompanyDashboardUiAction.OnPeriodChange(it))
                },
                period = period,
                modifier = Modifier.padding(horizontal = MaterialTheme.padding.mediumSmall)
            )
        }

        item {
            HorizontalDivider()
            PreferenceGroupHeader(stringResource(R.string.label_teams))
        }

        item(
            key = "teams_in_company"
        ) {
            val teams by remember {
                derivedStateOf { uiState.teams }
            }
            val usersInCompany by remember {
                derivedStateOf { uiState.usersInCompany }
            }

            teams.fastForEach { team ->
                TeamCardItem(
                    team = team,
                    users = usersInCompany.second.filter { it.teamId == team.id },
                    onClick = {
                        navController.navigate("dashboard/team?id=${team.id}") {
                            launchSingleTop = true
                        }
                    },
                    showUsersInTeam = {
                        viewModel.onAction(CompanyDashboardUiAction.FetchUserInTeam(it))
                    },
                    navigateToUserDashboard = {
                        navController.navigate("dashboard/staff?id=$it") {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .hozPadding()
                )
                Spacer(Modifier.height(MaterialTheme.padding.mediumSmall))
            }
            Timber.d(teams.toString())
        }
    }
}