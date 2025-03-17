package com.kt.worktimetrackermanager.data.remote.dto.request

data class CreateTeamRequest (
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val userId: Int,
)