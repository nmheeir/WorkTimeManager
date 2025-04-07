package com.kt.worktimetrackermanager.presentation.screens.memberManager

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.data.remote.dto.enum.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.presentation.components.dropdownMenu.DropdownMenuForEnum
import com.kt.worktimetrackermanager.presentation.components.dropdownMenu.DropdownMenuForList
import com.kt.worktimetrackermanager.presentation.components.customButton.GlowingButton
import com.kt.worktimetrackermanager.presentation.components.calendar.MyCalendarDialog
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
        onAction = viewModel::onAction
    )
}

@Composable
fun AddMemberLayout(
    state: AddMemberUiState,
    onAction: (AddMemberUiAction) -> Unit,
) {
    val personalInforField = listOf(
        Triple("userFullName", state.userFullName, Pair(stringResource(R.string.full_name), Icons.Default.Create)),
        Triple("address", state.address, Pair(stringResource(R.string.address), Icons.Default.Home)),
        Triple("phoneNumber", state.phoneNumber, Pair(stringResource(R.string.phone_number), Icons.Default.Phone))
    )

    val accountFields = listOf(
        Triple("userName", state.userName to state.usernameError, Pair(stringResource(R.string.username), Icons.Default.Person)),
        Triple("password", state.password to state.passwordError, Pair(stringResource(R.string.password), Icons.Default.Lock)),
        Triple("email", state.email to state.emailError, Pair(stringResource(R.string.email), Icons.Default.MailOutline))
    )

    val workFields = listOf(
        Triple("department", state.department, Pair(stringResource(R.string.department), Icons.Default.Menu)),
        Triple("designation", state.designation, Pair(stringResource(R.string.designation), Icons.Default.Home))
    )




    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = stringResource(R.string.member_manager_home_action_add_member),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight(550)
        )
        // Account information
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = stringResource(R.string.account), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
        accountFields.forEach { field ->
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

        // Personal information
        Text(stringResource(R.string.personal_infor), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
        personalInforField.forEach { field ->
            MyOutlineTextField(
                value = field.second,
                onValueChange = { onAction(AddMemberUiAction.OnFieldChange(field.first, it)) },
                text = field.third.first,
                icon = field.third.second
            )
        }
        DateField (onValueChange ={ onAction(AddMemberUiAction.OnFieldChange("dayOfBirth", state.dayOfBirth)) }, )

        // Work Information
        Text(stringResource(R.string.work_infor), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
        workFields.forEach { field ->
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
        DropdownMenuForList(
            text = stringResource(R.string.team),
            onItemSelected = {onAction(AddMemberUiAction.OnFieldChange("teamId", it))},
            list = state.companyTeams,
            propertyName = "name",
            initialValue = teamInitial
        )
        DropdownMenuForEnum(
            text = stringResource(R.string.EmpType),
            enumValues = EmployeeType.entries.toTypedArray(),
            onItemSelected =  {onAction(AddMemberUiAction.OnFieldChange("employeeType", it))},
            initialValue = state.employeeType
        )
        DropdownMenuForEnum(
            text = stringResource(R.string.role),
            onItemSelected = {onAction(AddMemberUiAction.OnFieldChange("role", it))},
            enumValues = Role.entries.toTypedArray(),
            initialValue = state.role
        )

        // Commit Button
        GlowingButton(
            onClick = {onAction(AddMemberUiAction.AddMember)},
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(
                text = stringResource(R.string.member_manager_home_action_add_member),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



@Composable
fun DateField(onValueChange: (LocalDate) -> Unit) {
    var stateDay by remember { mutableStateOf(LocalDate.now()) }
    var expanded by remember { mutableStateOf(false) }

    MyOutlineTextField(
        value = stateDay.toString(),
        onClick = { expanded = true },
        text = stringResource(R.string.day_of_birth),
        icon = Icons.Default.DateRange
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
                employeeType = EmployeeType.FULL_TIME,
                password = "",
                phoneNumber = "",
                role = Role.STAFF,
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
            onAction = {}
        )
    }
}
