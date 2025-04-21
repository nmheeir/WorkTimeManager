package com.kt.worktimetrackermanager.presentation.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.clickable
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.components.EmptyCardState
import com.kt.worktimetrackermanager.presentation.components.image.CircleImage
import com.kt.worktimetrackermanager.presentation.components.preference.PreferenceEntry
import com.kt.worktimetrackermanager.presentation.navigations.Screens
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import com.kt.worktimetrackermanager.presentation.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
//    Timber.d(mainViewModel.startDestination.value)

    val user by viewModel.user.collectAsStateWithLifecycle()

    HomeLayout(
        user = user,
        onNavigate = { screens ->
            navController.navigate(screens.route)
        }
    )
}

@Composable
fun HomeLayout(
    user: User?,
    onNavigate: (Screens) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item(
            key = "top_bar"
        ) {
            HomeTopBar(
                modifier = Modifier,
                user = user,
                onNavigate = onNavigate
            )
        }
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
            PreferenceEntry(
                title = { Text("Logs") },
                trailingContent = {
                    Row {
                        Text("See more")
                        Icon(
                            painterResource(R.drawable.ic_keyboard_arrow_right),
                            null
                        )
                    }
                },
                onClick = {
                    onNavigate(Screens.Logs)
                }
            )
            EmptyCardState(
                message = R.string.msg_no_task,
                desc = R.string.msg_no_task_desc,
                icon = R.drawable.img_task
            ) { }
        }
    }
}

@Composable
private fun HomeTopBar(
    modifier: Modifier = Modifier,
    user: User?,
    onNavigate: (Screens) -> Unit,
) {

    if (user == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        )
    }

    user?.let {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
            modifier = modifier
                .clickable {
                    onNavigate(Screens.Profile)
                }
                .padding(MaterialTheme.padding.small)
        ) {
            CircleImage(
                imageUrl = it.avatarUrl ?: "",
                size = 48.dp
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


val LocalNavAnimatedVisibilityScope =
    compositionLocalOf<AnimatedVisibilityScope> { error("No LocalNavAnimatedVisibilityScope Scope found") }

