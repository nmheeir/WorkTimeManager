package com.kt.worktimetrackermanager.data.remote.dto.response

data class UsersStatistic(
    val totalMember: Int,
    val managerCount: Int,
    val staffCount: Int
)

data class TeamStatistic(
    val totalTeam: Int,
    val totalMember: Int,
    val managerCount: Int,
    val staffCount: Int
)
