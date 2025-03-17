package com.kt.worktimetrackermanager.presentation.navigations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.kt.worktimetrackermanager.presentation.screens.DashboardScreen
import com.kt.worktimetrackermanager.presentation.screens.HomeScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.LoginScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.RegisterScreen

fun NavGraphBuilder.navigationBuilder(
    navController: NavHostController
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
        route = Screens.Home.route
    ) {
        HomeScreen(navController)
    }

    composable(
        route = Screens.Dashboard.route
    ) {
        DashboardScreen(navController)
    }
}