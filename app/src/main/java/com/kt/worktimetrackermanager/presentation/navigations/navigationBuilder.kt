package com.kt.worktimetrackermanager.presentation.navigations

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.kt.worktimetrackermanager.presentation.screens.DashboardScreen
import com.kt.worktimetrackermanager.presentation.screens.HomeScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.LoginScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.RegisterScreen

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
        DashboardScreen(navController)
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
}