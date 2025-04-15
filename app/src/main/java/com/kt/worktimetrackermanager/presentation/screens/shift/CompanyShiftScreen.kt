package com.kt.worktimetrackermanager.presentation.screens.shift

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.ui.EmptyBox
import com.kt.worktimetrackermanager.presentation.components.items.TeamShiftStatCardItem
import com.kt.worktimetrackermanager.presentation.viewmodels.CompanyShiftViewModel

@Composable
fun CompanyShiftScreen(
    navController: NavHostController,
    viewModel: CompanyShiftViewModel = hiltViewModel(),
) {
    val teamShiftsStat by viewModel.teamShiftsStat.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopBar()
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (teamShiftsStat.isEmpty()) {
                item(
                    key = "no_team"
                ) {
                    EmptyBox(
                        stringRes = R.string.msg_no_team,
                        descRes = R.string.msg_no_team_desc
                    )
                }
            }

            items(
                items = teamShiftsStat,
                key = { it.team.id }
            ) { stat ->
                TeamShiftStatCardItem(
                    stat = stat,
                    onClick = {
                        navController.navigate("shift/team/${stat.team.id}")
                    }
                )
            }
        }
    }
}

@Composable
private fun TopBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.Red)
    )
}