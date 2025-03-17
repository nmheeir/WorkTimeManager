package com.kt.worktimetrackermanager.data.remote.dto.response

data class User(
    val address: String,
    val avatarUrl: String? = null,
    val company: Company? = null,
    val companyId: Int? = null,
    val companyTeam: Team? = null,
    val createdAt: String,
    val dayOfBirth: String,
    val department: String,
    val designation: String,
    val email: String,
    val employeeType: Int,
    val id: Int,
    val phoneNumber: String,
    val role: Int,
    val teamId: Int? = null,
    val userFullName: String,
    val userName: String
)

