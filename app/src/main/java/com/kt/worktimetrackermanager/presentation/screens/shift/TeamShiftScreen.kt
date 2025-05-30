package com.kt.worktimetrackermanager.presentation.screens.shift

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.presentation.viewmodels.TeamShiftViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.ui.EmptyBox
import com.kt.worktimetrackermanager.core.presentation.ui.components.HideOnScrollComponent
import com.kt.worktimetrackermanager.presentation.components.items.ShiftCardItem


@Composable
fun TeamShiftScreen(
    navController: NavHostController,
    viewModel: TeamShiftViewModel = hiltViewModel(),
) {
    val shifts by viewModel.shifts.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopBar()
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            val lazyListState = rememberLazyListState()
            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
                modifier = Modifier
                    .hozPadding()
            ) {
                if (shifts.isEmpty()) {
                    item(
                        key = "no_shift"
                    ) {
                        EmptyBox(
                            stringRes = R.string.msg_no_data,
                            descRes = R.string.msg_no_data_desc
                        )
                    }
                }

                // TODO: Filter Shift

                items(
                    items = shifts,
                    key = { it.id }
                ) { shift ->
                    ShiftCardItem(
                        shift = shift

                    )
                }
            }

            HideOnScrollComponent(lazyListState = lazyListState) {
                Button(
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("shift/assign_shift")
                    }
                ) {
                    Text(
                        text = "Add Shift"
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {

    }
}