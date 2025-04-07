package com.kt.worktimetrackermanager.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.clickable
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.presentation.activities.LocalMainViewModel
import com.kt.worktimetrackermanager.presentation.components.EmptyCardState
import com.kt.worktimetrackermanager.presentation.components.image.CoilImage
import com.kt.worktimetrackermanager.presentation.navigations.Screens
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import com.kt.worktimetrackermanager.presentation.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
//    Timber.d(mainViewModel.startDestination.value)
    HomeLayout(
        onNavigate = {
            screens -> navController.navigate(screens.route)
        }
    )
}
@Composable
fun HomeLayout(
    onNavigate : (Screens) -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                modifier = Modifier,
                onNavigate = onNavigate
            )
        },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onSurface)
            .fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            item(
                key = "summary"
            ) {
                Card(
                    shape = MaterialTheme.shapes.small,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Text(
                            text = "Summary",
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Text(
                            text = "Today task & activities",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item(
                key = "meeting"
            ) {
                EmptyCardState(
                    message = R.string.msg_no_meeting,
                    desc = R.string.msg_no_meeting_desc,
                    icon = R.drawable.img_meeting
                ) { }
            }

            item(
                key = "task"
            ) {
                EmptyCardState(
                    message = R.string.msg_no_task,
                    desc = R.string.msg_no_task_desc,
                    icon = R.drawable.img_task
                ) { }
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    modifier: Modifier = Modifier,
    onNavigate: (Screens) -> Unit,
) {
    val mainViewModel = LocalMainViewModel.current
    val user by mainViewModel.user.collectAsStateWithLifecycle()
    Text(
        text = "Profile",
        modifier = Modifier
            .clickable {
                onNavigate(Screens.Profile)
            }
    )
    user?.let {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
        ) {
            CoilImage(
                imageUrl = it.avatarUrl!!,
                contentDescription = null,
                modifier = modifier
                    .clip(CircleShape)
                    .size(54.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = it.userName,
                )
                Text(
                    text = it.department
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
            ) {
                IconButton(
                    onClick = {
                        onNavigate(Screens.Chat)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chat),
                        contentDescription = null
                    )
                }
                IconButton(
                    onClick = {
                        onNavigate(Screens.Notification)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_notifications),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeLayoutPreview() {
    WorkTimeTrackerManagerTheme {
        HomeLayout (
            onNavigate = {}
        )
    }
}

