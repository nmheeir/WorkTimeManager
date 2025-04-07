package com.kt.worktimetrackermanager.presentation.navigations

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.kt.worktimetrackermanager.R

@Immutable
sealed class Screens(
    val route: String
) {
    sealed class NavigationBarScreens(
        @StringRes val titleId: Int,
        @DrawableRes val iconId: Int,
        route: String
    ): Screens(route) {
        data object Home : NavigationBarScreens(R.string.home, R.drawable.ic_home, "home")
        data object Dashboard : NavigationBarScreens(R.string.dashboard, R.drawable.ic_analytics, "dashboard")
        data object Attendant : NavigationBarScreens(R.string.attendant, R.drawable.ic_date_range, "attendant")
        data object Group : NavigationBarScreens(R.string.group, R.drawable.ic_groups, "group")
        companion object {
            val MainScreens = listOf<NavigationBarScreens>(Home, Dashboard, Attendant, Group)
        }
    }

    data object Profile: Screens("profile")
    data object Chat: Screens("chat")
    data object Notification: Screens("notification")

    data object AddMember: Screens("addMember")
    data object TeamCreate: Screens("teamCreated")
    data object TeamList: Screens("teamList")

    data class UpdateMember(val userId: Int? = null) : Screens(
        route = buildString {
            append("addMember")
            userId?.let { append("?userId=$userId") }
        }
    )

    data class MemberInfor(val userId: Int) : Screens (
        route = buildString {
            append("memberInfor")
            append("/$userId")
        }
    )

    data class TeamInformation(val teamId: Int) : Screens (
        route = buildString {
            append("teamInfor")
            append("/$teamId")
        }
    )

    data class MemberList(
        val teamId: Int? = null,
        val role: Int? = null,
        val employeeType: Int? = null
    ) : Screens(
        route = buildString {
            append("MemberList")
            val params = mutableListOf<String>()

            teamId?.let { params.add("teamId=$it") }
            role?.let { params.add("role=$it") }
            employeeType?.let { params.add("employeeType=$it") }

            if (params.isNotEmpty()) {
                append("?${params.joinToString("&")}")
            }
        }
    )
}


