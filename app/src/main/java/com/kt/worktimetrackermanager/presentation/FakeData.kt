package com.kt.worktimetrackermanager.presentation

import com.kt.worktimetrackermanager.data.remote.dto.enum.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus
import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import com.kt.worktimetrackermanager.data.remote.dto.response.Project
import com.kt.worktimetrackermanager.data.remote.dto.response.Task
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import java.time.LocalDateTime
import kotlin.random.Random

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
    employeeType = EmployeeType.Pernament,
    id = 1001,
    phoneNumber = "0987654321",
    role = Role.Master,
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
        employeeType = EmployeeType.entries.random(),
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

val fakeTasks = List(10) { index ->
    Task(
        id = index + 1,
        name = "Task ${index + 1}",
        description = "Description for Task ${index + 1}",
        assigneeId = Random.nextInt(1, 100), // Giả sử có 100 user
        projectId = Random.nextInt(1, 10), // Giả sử có 10 dự án
        createdAt = LocalDateTime.now().minusDays(Random.nextLong(1, 30)),
        dueDate = LocalDateTime.now().plusDays(Random.nextLong(1, 30)),
        status = ProjectStatus.entries.random()
    )
}

val fakeProjects = List(10) { index ->
    val startDate = LocalDateTime.now().minusDays(Random.nextLong(10, 100))
    val endDate = startDate.plusDays(Random.nextLong(30, 200))
    val updatedAt = startDate.plusDays(Random.nextLong(1, 50))

    Project(
        id = index + 1,
        name = "Project ${index + 1}",
        description = "Description for Project ${index + 1}",
        managerId = Random.nextInt(1, 10),
        createdAt = startDate,
        startDate = startDate,
        endDate = endDate,
        updatedAt = updatedAt,
        status = ProjectStatus.entries.random(),
        tasks = fakeTasks.filter { it.projectId == index + 1 }
    )
}