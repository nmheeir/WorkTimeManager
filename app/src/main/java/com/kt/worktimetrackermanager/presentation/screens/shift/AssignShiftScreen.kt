package com.kt.worktimetrackermanager.presentation.screens.shift

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.ext.format3
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.remote.dto.enums.ShiftType
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.components.chip.ChipsRow
import com.kt.worktimetrackermanager.presentation.components.dialog.CalendarTimerDialog
import com.kt.worktimetrackermanager.presentation.components.image.CircleImage
import com.kt.worktimetrackermanager.presentation.viewmodels.AssignShiftError
import com.kt.worktimetrackermanager.presentation.viewmodels.AssignShiftUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.AssignShiftViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignShiftScreen(
    navController: NavHostController,
    viewModel: AssignShiftViewModel = hiltViewModel(),
) {
    val error by viewModel.error.collectAsStateWithLifecycle()
    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val endDate by viewModel.endDate.collectAsStateWithLifecycle()
    val users by viewModel.listUsers.collectAsStateWithLifecycle()
    val selectedShiftType by viewModel.shiftType.collectAsStateWithLifecycle()

    val isLoadingUsers by viewModel.isLoadingUsers.collectAsStateWithLifecycle()
    val isLoadingAssignShift by viewModel.isLoadingAssignShift.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Assign Shift"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = navController::navigateUp
                    ) {
                        Icon(painterResource(R.drawable.ic_keyboard_arrow_left), null)
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.onAction(AssignShiftUiAction.AssignShift)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                enabled = !isLoadingAssignShift
            ) {
                Text(text = "Assign Shift")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { contentPadding ->
        Box(
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = contentPadding,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item(
                    key = "employee_section"
                ) {
                    if (isLoadingUsers) {
                        CircularProgressIndicator()
                    } else {
                        EmployeeSection(
                            error = error,
                            users = users,
                            action = viewModel::onAction
                        )
                    }
                }

                item(
                    key = "shift_section"
                ) {
                    ChipsRow(
                        chips = listOf(
                            ShiftType.Normal to "Normal",
                            ShiftType.Overtime to "Overtime",
                            ShiftType.NightShift to "NightShift",
                        ),
                        currentValue = selectedShiftType,
                        onValueUpdate = {
                            viewModel.onAction(AssignShiftUiAction.OnShiftTypeChange(it))
                        },
                        modifier = Modifier.hozPadding()
                    )
                }

                item(
                    key = "work_time_selection"
                ) {
                    WorkTimeSelection(
                        startDate = startDate,
                        endDate = endDate,
                        onAction = viewModel::onAction
                    )
                }

            }

            if (isLoadingAssignShift) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun EmployeeSection(
    modifier: Modifier = Modifier,
    error: AssignShiftError,
    users: List<User>,
    action: (AssignShiftUiAction) -> Unit,
) {
    val errColor = MaterialTheme.colorScheme.onErrorContainer
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        if (error.employeeError.isNotEmpty()) {
            Text(
                text = error.employeeError,
                color = errColor
            )
        }
        val borderColor =
            if (error.employeeError.isNotEmpty()) errColor else MaterialTheme.colorScheme.onPrimaryContainer

        val clickedStates = remember {
            mutableStateMapOf<Int, Boolean>()
        }

        LazyColumn(
            state = rememberLazyListState(),
            contentPadding = PaddingValues(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .height(300.dp)
        ) {
            items(
                items = users,
                key = { it.id }
            ) { employee ->
                LazyListEmployeeItem(
                    employee = employee,
                    isClicked = { clickedStates[employee.id] == true },
                    onClick = { isSelected ->
                        clickedStates[employee.id] = isSelected
                        if (isSelected) {
                            action(AssignShiftUiAction.AssignEmployee(employee))
                        } else {
                            action(AssignShiftUiAction.RemoveEmployee(employee))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun LazyListEmployeeItem(
    modifier: Modifier = Modifier,
    employee: User,
    isClicked: () -> Boolean,
    onClick: (Boolean) -> Unit,
) {
    val bgColor = if (isClicked()) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        Color.Transparent
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .clickable {
                onClick(!isClicked())
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            CircleImage(
                imageUrl = employee.avatarUrl ?: "",
                size = 48.dp
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = employee.userFullName, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "(${employee.companyTeam?.name})",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Text(
                    text = "@${employee.userName}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun WorkTimeSelection(
    modifier: Modifier = Modifier,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    onAction: (AssignShiftUiAction) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth()
            .hozPadding()
    ) {
        Box {
            var showDialog by remember { mutableStateOf(false) }
            TextButton(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                contentPadding = PaddingValues(MaterialTheme.padding.small),
                onClick = {
                    showDialog = true
                }
            ) {
                Text(
                    text = startDate.format3(),
                )
                if (showDialog) {
                    CalendarTimerDialog(
                        onDismiss = { showDialog = false },
                        currentDateTime = startDate,
                        onCurrentDateTimeChange = {
                            onAction(AssignShiftUiAction.OnStartDateTimeChange(it))
                            showDialog = false
                        }
                    )
                }
            }
        }

        Box {
            var showDialog by remember { mutableStateOf(false) }
            TextButton(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                contentPadding = PaddingValues(MaterialTheme.padding.small),
                onClick = {
                    showDialog = true
                }
            ) {
                Text(
                    text = endDate.format3()
                )
                if (showDialog) {
                    CalendarTimerDialog(
                        onDismiss = { showDialog = false },
                        currentDateTime = endDate,
                        onCurrentDateTimeChange = {
                            onAction(AssignShiftUiAction.OnEndDateTimeChange(it))
                            showDialog = false
                        }
                    )
                }
            }
        }
    }
}

