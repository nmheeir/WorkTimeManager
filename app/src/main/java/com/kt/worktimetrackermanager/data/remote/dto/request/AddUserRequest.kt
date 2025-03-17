package com.kt.worktimetrackermanager.data.remote.dto.request

import java.util.Date

data class AddUserRequest(
    val address: String,
    val avatarURL: String? = null,
    val companyId: Int? = null,
    val createdAt: Date = Date(),
    val dayOfBirth: String = "",
    val department: String = "None",
    val designation: String = "None",
    val email: String = "None",
    val employeeType: Int,
    val password: String,
    val phoneNumber: String = "None",
    val role: Int,
    val teamId: Int? = null,
    val userFullName: String = "None",
    val userName: String
)
