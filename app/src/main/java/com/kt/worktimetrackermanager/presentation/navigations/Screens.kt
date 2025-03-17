package com.kt.worktimetrackermanager.presentation.navigations

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.kt.worktimetrackermanager.R

@Immutable
sealed class Screens(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int,
    val route: String
) {

    data object Home : Screens(R.string.home, R.drawable.ic_home, "home")
    data object Dashboard : Screens(R.string.dashboard, R.drawable.ic_analytics, "dashboard")
    data object Attendant : Screens(R.string.attendant, R.drawable.ic_date_range, "attendant")
    data object Group : Screens(R.string.group, R.drawable.ic_groups, "group")

    companion object {
        val MainScreens = listOf<Screens>(Home, Dashboard, Attendant, Group)
    }
}