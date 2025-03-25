package com.kt.worktimetrackermanager.presentation

import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.User

val fakeUser = User(
    address = "123 Đường Nguyễn Huệ, Quận 1, TP.HCM",
    avatarUrl = "https://randomuser.me/api/portraits/men/1.jpg",
    company = null,
    companyId = 101,
    companyTeam = null,
    createdAt = "2024-03-25",
    dayOfBirth = "1995-07-15",
    department = "Phòng Kỹ thuật",
    designation = "Kỹ sư phần mềm",
    email = "nguyenvana@example.com",
    employeeType = 1,
    id = 1001,
    phoneNumber = "0987654321",
    role = Role.MASTER,
    teamId = 5,
    userFullName = "Nguyễn Văn A",
    userName = "nguyenvana"
)

val fakeUsers = List(10) { index ->
    User(
        address = "Địa chỉ số ${index + 1}, Quận ${index % 10}, TP.HCM",
        avatarUrl = "https://randomuser.me/api/portraits/men/${(index % 99) + 1}.jpg",
        company = null,
        companyId = (index % 5) + 1,
        companyTeam = null,
        createdAt = "2024-03-${(index % 28) + 1}",
        dayOfBirth = "199${(index % 9)}-0${(index % 9) + 1}-1${index % 9}",
        department = "Phòng ban ${index % 5}",
        designation = "Nhân viên cấp ${index % 3}",
        email = "user${index + 1}@example.com",
        employeeType = (index % 3) + 1,
        id = index + 1,
        phoneNumber = "09876543${(index % 10)}",
        role = Role.fromInt(index % 3),
        teamId = (index % 3) + 1,
        userFullName = "Người dùng ${index + 1}",
        userName = "user${index + 1}"
    )
}

val fakeTeams = List(10) { index ->
    Team(
        id = index + 1,
        name = "Team ${index + 1}",
        description = "Mô tả về team ${index + 1}",
        companyId = (index % 3) + 1,
        company = null,
        users = fakeUsers,
        latitude = 10.76 + (index * 0.01),
        longitude = 106.67 + (index * 0.01),
        createdAt = "2024-03-${(index % 28) + 1}"
    )
}