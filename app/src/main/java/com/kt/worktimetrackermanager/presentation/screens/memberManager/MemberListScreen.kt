package com.kt.worktimetrackermanager.presentation.screens.memberManager

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.kt.worktimetrackermanager.data.remote.dto.enum.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.navigations.Screens
import com.kt.worktimetrackermanager.presentation.sampleUser
import com.kt.worktimetrackermanager.presentation.components.dropdownMenu.DropdownMenuForEnum
import com.kt.worktimetrackermanager.presentation.components.dropdownMenu.DropdownMenuForList
import com.kt.worktimetrackermanager.presentation.components.scaffold.MyScaffold
import com.kt.worktimetrackermanager.presentation.screens.memberManager.component.MemberListItem
import com.kt.worktimetrackermanager.presentation.screens.memberManager.component.MyOutlineTextField
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.MemberListUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.MemberListUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.MemberListUiState
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.MemberListViewModel

@Composable
fun MemberListScreen(
    viewModel: MemberListViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.channel) { event ->
        when (event) {
            is MemberListUiEvent.Failure -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
            is MemberListUiEvent.Success -> { /* Handle success */ }
        }
    }

    MyScaffold(
        onBack = { navController.popBackStack() },
        title = stringResource(R.string.member_list),
    ) { paddingValues ->
        MemberListLayout(
            state = uiState,
            onNavigateTo = { screen -> navController.navigate(screen.route) },
            onAction = viewModel::onAction,
            onBack = { navController.popBackStack() },
            paddingValues = paddingValues
        )
    }
}

@Composable
fun MemberListLayout(
    state: MemberListUiState,
    onAction: (MemberListUiAction) -> Unit,
    onBack: () -> Unit,
    onNavigateTo: (Screens) -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier.padding(paddingValues),
    ) {
        MemberListHeader(
            state = state,
            onAction = onAction,
            onBack = onBack
        )
        MemberListContent(
            memberList = state.memberList,
            isLoading = state.loading,
            onNavigateTo = onNavigateTo,
            onAction = onAction
        )
    }
}

@Composable
fun MemberListHeader(
    state: MemberListUiState,
    onAction: (MemberListUiAction) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        MyOutlineTextField(
            value = state.searchValue,
            text = stringResource(R.string.search_member),
            icon = Icons.Default.Search,
            onValueChange = {
                onAction(MemberListUiAction.OnFieldChange("searchValue", it))
            }
        )
        MemberListFilters(
            state = state,
            onAction = onAction
        )
    }
}

@Composable
fun MemberListFilters(
    state: MemberListUiState,
    onAction: (MemberListUiAction) -> Unit
) {
    val teamInitial = state.teamOptionList.find { it.id == state.teamId }
    Row(modifier = Modifier.padding(vertical = 16.dp)) {
        DropdownMenuForList(
            text = stringResource(R.string.team),
            list = state.teamOptionList,
            propertyName = "name",
            initialValue = teamInitial,
            onItemSelected = {
                onAction(MemberListUiAction.OnFieldChange("teamId", it))
            }
        )
        DropdownMenuForEnum(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = stringResource(R.string.EmpType),
            enumValues = EmployeeType.entries.toTypedArray(),
            isNullable = true,
            onItemSelected = {
                onAction(MemberListUiAction.OnFieldChange("employeeType", it))
            }
        )
        DropdownMenuForEnum(
            text = stringResource(R.string.role),
            enumValues = Role.entries.toTypedArray(),
            isNullable = true,
            onItemSelected = {
                onAction(MemberListUiAction.OnFieldChange("role", it))
            }
        )
    }
}

@Composable
fun MemberListContent(
    memberList: List<User>,
    isLoading: Boolean,
    onNavigateTo: (Screens) -> Unit,
    onAction: (MemberListUiAction) -> Unit
) {
    val listState = rememberLazyListState()
    val isAtBottom = !listState.canScrollForward

    LaunchedEffect(isAtBottom) {
        if (isAtBottom && !isLoading) {
            onAction(MemberListUiAction.OnScrollToBottom)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(memberList) { item ->
            MemberListItem(item) {
                onNavigateTo(Screens.MemberInfor(item.id))
            }
        }
        if (isLoading) {
            item { Text(text = "Loading", modifier = Modifier.fillMaxWidth()) }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun MemberListLayoutPreview() {
    WorkTimeTrackerManagerTheme {
        MemberListLayout(
            state = MemberListUiState(
                memberList = listOf(
                    sampleUser,
                    sampleUser,
                    sampleUser,
                    sampleUser,

                )
            ),
            onAction = {},
            onNavigateTo = {},
            onBack = {},
            paddingValues = PaddingValues(0.dp, 0.dp, 0.dp, 0.dp)
        )
    }
}