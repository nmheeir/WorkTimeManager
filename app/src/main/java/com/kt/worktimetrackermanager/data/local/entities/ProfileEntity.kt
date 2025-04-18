package com.kt.worktimetrackermanager.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kt.worktimetrackermanager.data.remote.dto.enums.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enums.Role
import com.kt.worktimetrackermanager.data.remote.dto.response.Company
import com.kt.worktimetrackermanager.data.remote.dto.response.Team

@Entity(tableName = "profile")
data class ProfileEntity(
    val address: String,
    val avatarUrl: String? = null,
    val companyId: Int? = null,
    val createdAt: String,
    val dayOfBirth: String,
    val department: String,
    val designation: String,
    val email: String,
    val employeeType: EmployeeType,
    @PrimaryKey(autoGenerate = false) val id: Int,
    val phoneNumber: String,
    val role: Role,
    val teamId: Int? = null,
    val userFullName: String,
    val userName: String,
)