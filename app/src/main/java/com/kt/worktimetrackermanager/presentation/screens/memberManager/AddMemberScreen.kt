package com.kt.worktimetrackermanager.presentation.screens.memberManager

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.data.remote.dto.enums.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enums.Role
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.presentation.components.calendar.MyCalendarDialog
import com.kt.worktimetrackermanager.presentation.components.dropdownMenu.DropdownMenuForEnum
import com.kt.worktimetrackermanager.presentation.components.dropdownMenu.DropdownMenuForList
import com.kt.worktimetrackermanager.presentation.components.scaffold.MyScaffold
import com.kt.worktimetrackermanager.presentation.screens.memberManager.component.MyOutlineTextField
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.AddMemberUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.AddMemberUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.AddMemberUiState
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.AddMemberViewModel
import timber.log.Timber
import java.time.LocalDate


@Composable
fun AddMemberScreen(
    viewModel: AddMemberViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.channel) {
        Timber.d( "AddmemberScreenObserve: $it")
        when (it) {
            is AddMemberUiEvent.Failure -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }

            is AddMemberUiEvent.Success -> {
                Toast.makeText(context, "Add member successful", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }



    AddMemberLayout(
        state = uiState,
        onAction = viewModel::onAction,
        onBack = { navController.popBackStack() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberLayout(
    state: AddMemberUiState,
    onAction: (AddMemberUiAction) -> Unit,
    onBack: () -> Unit,
) {
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 3

    MyScaffold(
        onBack = onBack,
        title = stringResource(R.string.member_manager_home_action_add_member),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Progress indicator
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Step $currentStep of $totalSteps",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = getStepName(currentStep),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { currentStep.toFloat() / totalSteps },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            }

            // Form content
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    when (currentStep) {
                        1 -> AccountInformationForm(state, onAction)
                        2 -> PersonalInformationForm(state, onAction)
                        3 -> WorkInformationForm(state, onAction)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Navigation buttons
                    NavigationButtons(
                        currentStep = currentStep,
                        totalSteps = totalSteps,
                        onPrevious = { currentStep-- },
                        onNext = { currentStep++ },
                        onSubmit = { onAction(AddMemberUiAction.AddMember) }
                    )
                }
            }
        }
    }
}


@Composable
fun NavigationButtons(
    currentStep: Int,
    totalSteps: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSubmit: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (currentStep > 1) {
            OutlinedButton(onClick = onPrevious) {
                Text("Previous")
            }
        } else {
            Spacer(modifier = Modifier.width(1.dp))
        }

        if (currentStep < totalSteps) {
            Button(onClick = onNext) {
                Text("Next")
            }
        } else {
            Button(onClick = onSubmit) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit")
                }
            }
        }
    }
}


@Composable
fun AccountInformationForm(
    state: AddMemberUiState,
    onAction: (AddMemberUiAction) -> Unit,
) {
    val accountFields = listOf(
        Triple("userName", state.userName to state.usernameError, Pair(stringResource(R.string.username), Icons.Default.Person)),
        Triple("password", state.password to state.passwordError, Pair(stringResource(R.string.password), Icons.Default.Lock)),
        Triple("email", state.email to state.emailError, Pair(stringResource(R.string.email), Icons.Default.MailOutline))
    )
    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
            Text(
                text = stringResource(R.string.account),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            accountFields.forEach { field ->
                Text(
                    text = field.third.first,
                    modifier = Modifier.padding(4.dp)
                )
                MyOutlineTextField(
                    value = field.second.first,
                    onValueChange = { onAction(AddMemberUiAction.OnFieldChange(field.first, it)) },
                    text = field.third.first,
                    icon = field.third.second
                )
                field.second.second?.let { error ->
                    Text(text = error, color = Color.Red)
                }
            }
        }
}

@Composable
fun PersonalInformationForm(
    state: AddMemberUiState,
    onAction: (AddMemberUiAction) -> Unit,
) {
    val personalInforField = listOf(
        Triple("userFullName", state.userFullName, Pair(stringResource(R.string.full_name), Icons.Default.Create)),
        Triple("address", state.address, Pair(stringResource(R.string.address), Icons.Default.Home)),
        Triple("phoneNumber", state.phoneNumber, Pair(stringResource(R.string.phone_number), Icons.Default.Phone))
    )


        // Personal information
    Column {
        Text(stringResource(R.string.personal_infor), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)

        personalInforField.forEach { field ->
            Text(
                text = field.third.first,
                modifier = Modifier.padding(4.dp)
            )
            MyOutlineTextField(
                value = field.second,
                onValueChange = { onAction(AddMemberUiAction.OnFieldChange(field.first, it)) },
                text = field.third.first,
                icon = field.third.second
            )
        }
        DateField (onValueChange ={ onAction(AddMemberUiAction.OnFieldChange("dayOfBirth", state.dayOfBirth)) }, )

    }
}

@Composable
fun WorkInformationForm(
    state: AddMemberUiState,
    onAction: (AddMemberUiAction) -> Unit,
) {
    val workFields = listOf(
        Triple("department", state.department, Pair(stringResource(R.string.department), Icons.Default.Menu)),
        Triple("designation", state.designation, Pair(stringResource(R.string.designation), Icons.Default.Home))
    )
    Column {
        // Work Information
        Text(
            stringResource(R.string.work_infor),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        workFields.forEach { field ->
            Text(
                text = field.third.first,
                modifier = Modifier.padding(4.dp)
            )
            MyOutlineTextField(
                value = field.second,
                onValueChange = { onAction(AddMemberUiAction.OnFieldChange(field.first, it)) },
                text = field.third.first,
                icon = field.third.second
            )
        }

        // Work information, drop down
        val teamInitial: Team? = if (state.companyTeams.isNotEmpty() && state.teamId != null) {
            state.companyTeams.find { it.id == state.teamId }
        } else {
            null
        }
        Text(
            text = stringResource(R.string.team),
            modifier = Modifier.padding(4.dp)
        )
        DropdownMenuForList(
            text = stringResource(R.string.team),
            onItemSelected = { onAction(AddMemberUiAction.OnFieldChange("teamId", it)) },
            list = state.companyTeams,
            propertyName = "name",
            initialValue = teamInitial,
            content = { displayText, onClick ->
                MyOutlineTextField(
                    value = displayText,
                    onClick = { onClick() },
                    text = stringResource(R.string.team),
                    icon = Icons.Filled.SupervisorAccount,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        )
        Text(
            text = stringResource(R.string.EmpType),
            modifier = Modifier.padding(4.dp)
        )
        DropdownMenuForEnum(
            text = stringResource(R.string.EmpType),
            enumValues = EmployeeType.entries.toTypedArray(),
            onItemSelected = { onAction(AddMemberUiAction.OnFieldChange("employeeType", it)) },
            initialValue = state.employeeType,
            content = { displayText, onClick ->
                MyOutlineTextField(
                    value = displayText,
                    onClick = { onClick() },
                    text = stringResource(R.string.EmpType),
                    icon = Icons.Filled.Accessibility,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        )
        Text(
            text = stringResource(R.string.role),
            modifier = Modifier.padding(4.dp)
        )
        DropdownMenuForEnum(
            text = stringResource(R.string.role),
            onItemSelected = { onAction(AddMemberUiAction.OnFieldChange("role", it)) },
            enumValues = Role.entries.toTypedArray(),
            initialValue = state.role,
            content = { displayText, onClick->
                MyOutlineTextField(
                    value = displayText,
                    onClick = { onClick() },
                    text = stringResource(R.string.role),
                    icon = Icons.Filled.Accessibility,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        )
    }
}

fun getStepName(step: Int): String {
    return when (step) {
        1 -> "Account Information"
        2 -> "Personal Information"
        3 -> "Work Information"
        else -> ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun DateField(onValueChange: (LocalDate) -> Unit) {
    var stateDay by remember { mutableStateOf(LocalDate.now()) }
    var expanded by remember { mutableStateOf(false) }

    MyOutlineTextField(
        value = stateDay.toString(),
        onClick = { expanded = true },
        text = stringResource(R.string.day_of_birth),
        icon = Icons.Default.DateRange,
        modifier = Modifier.padding(top = 4.dp)
    )
    if (expanded) {
        MyCalendarDialog(
            event = { selectedDate ->
                selectedDate?.let {
                    onValueChange(selectedDate)
                    stateDay = selectedDate
                }
                expanded = false
            },
            showDialog = {
                expanded
            },
            selectedDate = stateDay
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddMemberPreview() {
    WorkTimeTrackerManagerTheme {
        AddMemberLayout(
            state = AddMemberUiState(
                isLoading = false,
                address = "",
                avatarURL = "",
                companyId = null,
                createdAt = "",
                dayOfBirth = LocalDate.now(),
                department = "",
                designation = "",
                email = "",
                employeeType = EmployeeType.entries.random(),
                password = "",
                phoneNumber = "",
                role = Role.Staff,
                teamId = null,
                userFullName = "",
                userName = "",
                companyTeams = emptyList(),

                // lỗi
                usernameError = null,
                emailError = null,
                passwordError = null,

                // id user for update
                userId = null
            ),
            onAction = {},
            onBack = {}
        )
    }
}
