package com.kt.worktimetrackermanager.data.remote.dto.response

import com.kt.worktimetrackermanager.data.local.entities.ProfileEntity
import com.kt.worktimetrackermanager.data.remote.dto.enums.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enums.Role

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

fun User.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        address = address,
        avatarUrl = avatarUrl,
        companyId = companyId,
        createdAt = createdAt,
        dayOfBirth = dayOfBirth,
        department = department,
        designation = designation,
        email = email,
        employeeType = employeeType,
        id = id,
        phoneNumber = phoneNumber,
        role = role,
        teamId = teamId,
        userFullName = userFullName,
        userName = userName,
    )
}


