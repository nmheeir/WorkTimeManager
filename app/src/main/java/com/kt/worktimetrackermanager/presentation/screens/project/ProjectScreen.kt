package com.kt.worktimetrackermanager.presentation.screens.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.ui.components.HideOnScrollComponent
import com.kt.worktimetrackermanager.presentation.viewmodels.ProjectViewModel

@Composable
fun ProjectScreen(
    navController: NavHostController,
    viewModel: ProjectViewModel = hiltViewModel(),
) {
    val lazyListState = rememberLazyListState()
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item(
                key = "summary_your_work"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                    modifier = Modifier
                        .fillMaxWidth()
                        .hozPadding()
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        text = "Summary of your work",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Your current project progress",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..3) {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "Todo",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = i.toString(),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                }
            }

            item(
                key = "choices"
            ) {
                val choices = listOf("All", "In Progress", "Completed")
                SingleChoiceSegmentedButtonRow {
                    choices.fastForEach {title ->
                        SegmentedButton(
                            selected = false,
                            onClick = {},
                            shape = SegmentedButtonDefaults.baseShape
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        HideOnScrollComponent(
            lazyListState = lazyListState
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
