package com.kt.worktimetrackermanager.presentation.navigations

import androidx.compose.material3.Text
import androidx.navigation.NavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kt.worktimetrackermanager.presentation.components.MiddlewareRole
import com.kt.worktimetrackermanager.presentation.screens.dashboard.CompanyDashBoardScreen
import com.kt.worktimetrackermanager.presentation.screens.HomeScreen
import com.kt.worktimetrackermanager.presentation.screens.ProfileScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.LoginScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.RegisterScreen
import com.kt.worktimetrackermanager.presentation.screens.dashboard.StaffDashboardScreen
import com.kt.worktimetrackermanager.presentation.screens.dashboard.TeamDashboardScreen
import com.kt.worktimetrackermanager.presentation.screens.project.ProjectScreen
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
                TeamDashboardScreen(navController)
            }
        )
    }

    composable(
        route = "dashboard/team?id={id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            }
        )
    ) {
        TeamDashboardScreen(navController)
    }

    composable(
        route = "dashboard/staff?id={id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            }
        )
    ) {
        StaffDashboardScreen(navController)
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

    composable(
        route = "project"
    ) {
        ProjectScreen(navController)
    }
}