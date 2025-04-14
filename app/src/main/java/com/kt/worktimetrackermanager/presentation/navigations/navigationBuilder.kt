package com.kt.worktimetrackermanager.presentation.navigations

import android.util.Log
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.kt.worktimetrackermanager.core.utils.JwtUtils
import com.kt.worktimetrackermanager.presentation.components.MiddlewareRole
import com.kt.worktimetrackermanager.presentation.screens.dashboard.CompanyDashBoardScreen
import com.kt.worktimetrackermanager.presentation.screens.HomeScreen
import com.kt.worktimetrackermanager.presentation.screens.ProfileScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.CreateNewPasswordScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.ForgotPasswordScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.LoginScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.RegisterScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.ResetPasswordSuccessScreen
import com.kt.worktimetrackermanager.presentation.screens.dashboard.StaffDashboardScreen
import com.kt.worktimetrackermanager.presentation.screens.dashboard.TeamDashboardScreen
import com.kt.worktimetrackermanager.presentation.screens.project.CreateProjectScreen
import com.kt.worktimetrackermanager.presentation.screens.project.ProjectDetailScreen
import com.kt.worktimetrackermanager.presentation.screens.project.ProjectScreen
import com.kt.worktimetrackermanager.presentation.screens.task.TaskDetailScreen
import com.kt.worktimetrackermanager.presentation.viewmodels.TeamDashboardViewModel
import timber.log.Timber

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
        route = "forgotPassword"
    ) {
        ForgotPasswordScreen(navController)
    }

    composable(
        route = "forgotPassword/createNewPassword",
        deepLinks = listOf(
            navDeepLink {
                uriPattern =
                    "http://localhost:5260/api/Auth/resetPassword?token={token}&token2={token2}"
            }
        )
    ) { backStackEntry ->
        val token = backStackEntry.arguments?.getString("token")
        val token2 = backStackEntry.arguments?.getString("token2")
        if (token != null && token2 != null &&
            !JwtUtils.isTokenExpired(token) &&
            !JwtUtils.isTokenExpired(token2)
        ) {
            // Mở màn hình CreateNewPasswordScreen nếu token hợp lệ
            CreateNewPasswordScreen(
                navController = navController,
                token = token2
            )
        } else {
            Timber.d("token: " + JwtUtils.isTokenExpired(token!!).toString())
            Timber.d("token2: " + JwtUtils.isTokenExpired(token2!!).toString())
//            ErrorScreen()
        }
    }

    composable(
        route = "resetPasswordSuccess"
    ) {
        ResetPasswordSuccessScreen(
            navController
        )
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

    composable(
        route = "project/detail?id={id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            }
        )
    ) {
        ProjectDetailScreen(navController)
    }

    composable(
        route = "project/create"
    ) {
        CreateProjectScreen(navController)
    }

    composable(
        route = "task_detail/{id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            }
        )
    ) {
        TaskDetailScreen(navController)
    }
}