package com.kt.worktimetrackermanager.data.remote.dto.response

import com.kt.worktimetrackermanager.data.remote.dto.enum.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enum.Role

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
    val employeeType: EmployeeType,
    val id: Int,
    val phoneNumber: String,
    val role: Role,
    val teamId: Int? = null,
    val userFullName: String,
    val userName: String,
)

