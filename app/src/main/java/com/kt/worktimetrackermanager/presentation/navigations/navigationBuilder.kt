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
import com.kt.worktimetrackermanager.presentation.screens.NotificationScreen
import com.kt.worktimetrackermanager.presentation.screens.ProfileScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.CreateNewPasswordScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.ForgotPasswordScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.LoginScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.RegisterScreen
import com.kt.worktimetrackermanager.presentation.screens.auth.ResetPasswordSuccessScreen
import com.kt.worktimetrackermanager.presentation.screens.dashboard.StaffDashboardScreen
import com.kt.worktimetrackermanager.presentation.screens.dashboard.TeamDashboardScreen
import com.kt.worktimetrackermanager.presentation.screens.memberManager.AddMemberScreen
import com.kt.worktimetrackermanager.presentation.screens.memberManager.MemberInforScreen
import com.kt.worktimetrackermanager.presentation.screens.memberManager.MemberListScreen
import com.kt.worktimetrackermanager.presentation.screens.memberManager.MemberManagerHomeScreen
import com.kt.worktimetrackermanager.presentation.screens.memberManager.TeamCreateScreen
import com.kt.worktimetrackermanager.presentation.screens.memberManager.TeamInformationScreen
import com.kt.worktimetrackermanager.presentation.screens.memberManager.TeamListScreen
import com.kt.worktimetrackermanager.presentation.screens.project.CreateProjectScreen
import com.kt.worktimetrackermanager.presentation.screens.project.ProjectDetailScreen
import com.kt.worktimetrackermanager.presentation.screens.project.ProjectScreen
import com.kt.worktimetrackermanager.presentation.screens.shift.AssignShiftScreen
import com.kt.worktimetrackermanager.presentation.screens.shift.CompanyShiftScreen
import com.kt.worktimetrackermanager.presentation.screens.shift.TeamShiftScreen
import com.kt.worktimetrackermanager.presentation.screens.task.TaskDetailScreen
import com.kt.worktimetrackermanager.presentation.viewmodels.TeamDashboardViewModel
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.MemberInforViewModel
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamInformationViewModel
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
        route = Screens.NavigationBarScreens.Home.route
    ) {
        HomeScreen(navController)
    }

    composable(
        route = Screens.NavigationBarScreens.Dashboard.route
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
        route = Screens.Chat.route
    ) {
        Text(
            text = "Chat"
        )
    }

    composable(
        route = Screens.Notification.route
    ) {
        NotificationScreen(navController)
    }

    composable(
        route = Screens.NavigationBarScreens.Shift.route
    ) {
        MiddlewareRole(
            managerContent = {
                TeamShiftScreen(navController)
            },
            masterContent = {
                CompanyShiftScreen(navController)
            }
        )
    }

    composable(
        route = "shift/team/{id}",
        arguments = listOf(
            navArgument("id") {
                type = NavType.IntType
            }
        )
    ) {
        TeamShiftScreen(navController)
    }

    composable(
        route = "shift/assign_shift"
    ) {
        AssignShiftScreen(navController)
    }

    composable(
        route = Screens.Profile.route
    ) {
        ProfileScreen(navController)
    }

    composable(
        route = Screens.NavigationBarScreens.Project.route
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

    composable(
        route = Screens.NavigationBarScreens.Member.route
    ) {
        MemberManagerHomeScreen(navController = navController)
    }

    composable(
        route = Screens.AddMember.route,
    ) {
        AddMemberScreen(navController = navController)
    }

    composable(
        route = Screens.TeamCreate.route
    ) {
        TeamCreateScreen(navController = navController)
    }

    composable(
        route = Screens.MemberList().route,
    ) {
        MemberListScreen(navController = navController)
    }

    composable(
        route = "memberInfor/{userId}",
        arguments = listOf(navArgument("userId") { type = NavType.IntType })
    ) { backStackEntry ->
        MemberInforScreen(
            navController = navController,
        )

    }

    composable(
        route = Screens.TeamList.route
    ) {
        TeamListScreen(
            navController = navController
        )
    }

    composable(
        route = "teamInfor/{teamId}",
        arguments = listOf(navArgument("teamId") {
            type = NavType.IntType
        })
    ) { backStackEntry ->
        TeamInformationScreen(
            navController = navController,
        )
    }

}