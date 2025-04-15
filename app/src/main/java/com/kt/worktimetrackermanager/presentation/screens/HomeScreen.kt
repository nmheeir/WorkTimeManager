package com.kt.worktimetrackermanager.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.clickable
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.utils.Gap
import com.kt.worktimetrackermanager.presentation.activities.LocalMainViewModel
import com.kt.worktimetrackermanager.presentation.components.EmptyCardState
import com.kt.worktimetrackermanager.presentation.components.image.CircleImage
import com.kt.worktimetrackermanager.presentation.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = {
            HomeTopBar(navController = navController)
        },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.onSurface)
            .fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    navController: NavHostController,
) {
    val mainViewModel = LocalMainViewModel.current
    val user by mainViewModel.user.collectAsStateWithLifecycle()

    user.takeIf { it != null }?.let { user ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
            modifier = Modifier
                .fillMaxWidth()
                .hozPadding()
                .clickable {
                    navController.navigate("profile")
                }
        ) {
            CircleImage(
                imageUrl = user.avatarUrl ?: "",
                size = 48.dp
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = user.userName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = user.department,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = {
                    navController.navigate("notification")
                }
            ) {
                Icon(painterResource(R.drawable.ic_notifications), null)
            }
        }
    }
}