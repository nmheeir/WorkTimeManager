package com.kt.worktimetrackermanager.presentation.screens.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.presentation.viewmodels.StaffDashboardViewModel
import timber.log.Timber

@Composable
fun StaffDashboard(
    navController: NavHostController,
    viewModel: StaffDashboardViewModel = hiltViewModel()
) {
    Timber.d("Staff dashboard")
}