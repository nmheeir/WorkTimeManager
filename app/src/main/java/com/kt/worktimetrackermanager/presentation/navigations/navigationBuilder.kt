package com.kt.worktimetrackermanager.presentation.navigations

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.kt.worktimetrackermanager.presentation.components.MiddlewareRole
import com.kt.worktimetrackermanager.presentation.screens.dashboard.CompanyDashBoardScreen
import com.kt.worktimetrackermanager.presentation.screens.HomeScreen
import com.kt.worktimetrackermanager.presentation.screens.ProfileScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.LoginScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.RegisterScreen
import com.kt.worktimetrackermanager.presentation.screens.dashboard.TeamDashboardScreen
import com.kt.worktimetrackermanager.presentation.viewmodels.TeamDashboardViewModel

fun NavGraphBuilder.navigationBuilder(
    navController: NavHostController,
) {
    composable(route = "login") {
        LoginScreen(navController)
    }

    composable(
        route = "register"
    ) {
        RegisterScreen(navController)
    }

    composable(
        route = "home"
    ) {
        HomeScreen(navController)
    }

    composable(
        route = "dashboard"
    ) {
        MiddlewareRole(
            masterContent = {
                CompanyDashBoardScreen(navController)
            },
            managerContent = {
                TeamDashboardScreen()
            }
        )
    }

    composable(
        route = "chat"
    ) {
        Text(
            text = "Chat"
        )
    }

    composable(
        route = "notification"
    ) {
        Text(
            text = "Notification"
        )
    }

    composable(
        route = "profile"
    ) {
        ProfileScreen(navController)
    }
}