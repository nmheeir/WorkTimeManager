package com.kt.worktimetrackermanager.presentation.screens.memberManager

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.presentation.exampleTeam
import com.kt.worktimetrackermanager.presentation.navigations.Screens
import com.kt.worktimetrackermanager.presentation.screens.memberManager.component.MyOutlineTextField
import com.kt.worktimetrackermanager.presentation.screens.memberManager.component.TeamListItem
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamListScreenUiState
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamListUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamListUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamListViewModel
import kotlinx.coroutines.flow.Flow


@Composable
fun TeamListScreen(
    viewModel: TeamListViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.channel) {
        when (it) {
            is TeamListUiEvent.Failure-> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }

            is TeamListUiEvent.Success -> {
            }
        }
    }


    // Nội dung của AddMemberLayout
    TeamListLayout(
        state = uiState,
        onAction = viewModel::onAction,
        onBack = {
            navController.popBackStack()
        },
        onNavigateTo = { screens ->
            navController.navigate(screens.route)
        }
    )

}


@Composable
fun TeamListLayout(
    state: TeamListScreenUiState,
    onAction: (TeamListUiAction) -> Unit,
    onBack: () -> Unit,
    onNavigateTo: (Screens) -> Unit
){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)

                ) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .clickable { onBack() }
                    )
                    Text(
                        stringResource(R.string.team_list),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(top = 20.dp, bottom = 0.dp)
                    )

                    // search textfield
                }

            }

            MyOutlineTextField(
                modifier = Modifier.padding(horizontal = 10.dp),
                value = state.searchValue,
                text = stringResource(R.string.search_team),
                icon = Icons.Default.Search,
                onValueChange = { onAction(TeamListUiAction.OnSearchValueChange(it)) }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                // Nội dung của AddMemberLayout
                TeamListContent (
                    teamList = state.teamList,
                    onNavigateTo = onNavigateTo,
                    onAction = onAction,
                    isLoading = state.isLoading
                )
            }
        }

}

@Composable
fun TeamListContent(
    teamList: List<Team>,
    onNavigateTo: (Screens) -> Unit,
    onAction: (TeamListUiAction) -> Unit,
    isLoading: Boolean,
) {
    val listState = rememberLazyListState()
    val isAtBottom = !listState.canScrollForward
    fun loadMoreItems() {
        if (isLoading) return
        onAction(TeamListUiAction.OnHitBottom)
    }
    LaunchedEffect(isAtBottom){
        if (isAtBottom) {
            loadMoreItems()
        }
    }

    LazyColumn(
        state = listState,
        modifier =
            Modifier.fillMaxSize()
                .padding(10.dp)
    ) {
        items(teamList) { item ->
            TeamListItem(item, onNavigate = {
                onNavigateTo(Screens.TeamInformation(item.id))
            })
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Hiển thị trạng thái loading khi đang tải
        item {
            if (isLoading) {
                Text(text = "${stringResource(R.string.loading)}...", modifier = androidx.compose.ui.Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TeamListLayoutPreview() {
    WorkTimeTrackerManagerTheme {
        TeamListLayout(
            state = TeamListScreenUiState(
                teamList = listOf(
                    exampleTeam
                )
            ),
            onAction = {},
            onNavigateTo = {},
            onBack = {}
        )
    }

}