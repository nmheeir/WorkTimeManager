package com.kt.worktimetrackermanager.data.remote.dto.response

data class Company(
    val id: Int,
    val companyName: String,
    val users: List<User>,
    val teams: List<Team>
)
