package com.kt.worktimetrackermanager.presentation.components.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEach
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.utils.Gap
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.components.dialog.ListDialog
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme

@Composable
fun TeamCardItem(
    modifier: Modifier = Modifier,
    team: Team,
    users: List<User>,
    onClick: () -> Unit,
    showUsersInTeam: (Int) -> Unit,
    navigateToUserDashboard: (Int) -> Unit,
) {
    Column {
        var showUser by rememberSaveable { mutableStateOf(false) }
        Card(
            modifier = modifier
                .fillMaxWidth(),
            onClick = onClick
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .hozPadding()
            ) {
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        showUser = !showUser
                    }
                ) {
                    if (showUser) {
                        showUsersInTeam(team.id)
                        UsersInTeamDialog(
                            users = users,
                            onDismiss = { showUser = false },
                            navigateToUserDashboard = navigateToUserDashboard
                        )
                    }
                    Icon(
                        painter = painterResource(R.drawable.ic_more_vert),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun UsersInTeamDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    navigateToUserDashboard: (Int) -> Unit,
    users: List<User>,
) {
    ListDialog(
        onDismiss = onDismiss,
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = users,
            key = { it.id }
        ) { user ->
            UserCardItem(
                user = user,
                onClick = {
                    navigateToUserDashboard(user.id)
                },
                modifier = Modifier
                    .hozPadding()
            )
            Gap(MaterialTheme.padding.small)
        }
    }
}