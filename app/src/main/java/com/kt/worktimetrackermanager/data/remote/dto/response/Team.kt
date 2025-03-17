package com.kt.worktimetrackermanager.data.remote.dto.response

data class Team(
    val id: Int,
    val name: String,
    val description: String,
    val companyId: Int,
    val company: Company?,
    var users: List<User>?,
    val latitude: Double,
    val longitude: Double,
    val createdAt: String,
)