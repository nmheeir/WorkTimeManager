package com.kt.worktimetrackermanager.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import com.kt.worktimetrackermanager.presentation.viewmodels.MiddlewareRoleViewModel
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiddlewareRole(
    viewModel: MiddlewareRoleViewModel = hiltViewModel(),
    managerContent: @Composable () -> Unit,
    masterContent: @Composable () -> Unit,
) {
    val role by viewModel.role.collectAsStateWithLifecycle()

    val isRefresh by viewModel.isRefresh.collectAsStateWithLifecycle()

    Timber.d(role?.name)

    PullToRefreshBox(
        isRefreshing = isRefresh,
        onRefresh = {
            viewModel.reload()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        when (role) {
            Role.Master -> masterContent()
            Role.Manager -> managerContent()

            null -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            else -> {
                Text(text = "UNAUTHORIZED")
            }
        }
    }
}