package com.kt.worktimetrackermanager.presentation

import com.kt.worktimetrackermanager.data.remote.dto.enum.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enum.Priority
import com.kt.worktimetrackermanager.data.remote.dto.enum.ProjectStatus
import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import com.kt.worktimetrackermanager.data.remote.dto.response.Company
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import java.time.LocalDate
import com.kt.worktimetrackermanager.data.remote.dto.response.Project
import com.kt.worktimetrackermanager.data.remote.dto.response.Report
import com.kt.worktimetrackermanager.data.remote.dto.response.Task
import com.kt.worktimetrackermanager.data.remote.dto.response.TaskStatistics
import com.kt.worktimetrackermanager.data.remote.dto.response.UserProfileDto
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
    employeeType = EmployeeType.PERNAMENT,
    id = 1001,
    phoneNumber = "0987654321",
    role = Role.Master,
    teamId = 5,
    userFullName = "Nguyễn Văn A",
    userName = "nguyenvana"
)
val sampleUser = User(
    address = "123 Main Street, Cityville",
    avatarUrl = "https://example.com/avatar.jpg",
    company = null,  // Bạn có thể thay thế giá trị này bằng một đối tượng Company nếu cần
    companyId = 1,
    companyTeam = Team(
        id = 1,
        companyId = 1,
        company = null,
        latitude = 0.0,
        longitude = 0.0,
        name = "UI/UX Team",
        users = emptyList(),
        description = "test description",
        createdAt = "2023-10-25T12:34:56",
    ),  // Bạn có thể thay thế giá trị này bằng một đối tượng Team nếu cần
    createdAt = "2023-10-25T12:34:56",
    department = "Engineering",
    designation = "Software Engineer",
    email = "user@example.com",
    employeeType = EmployeeType.PERNAMENT,  // Có thể là mã loại nhân viên
    id = 1001,
    phoneNumber = "+1234567890",
    role = Role.Staff,  // Có thể là mã vai trò (ví dụ: admin, user, v.v.)
    teamId = 1,
    userFullName = "John Doe",
    userName = "johndoe",
    dayOfBirth = LocalDate.now().toString()
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

val exampleUsers = List(1) {
    User(
        address = "123 Main Street, Cityville",
        avatarUrl = "https://example.com/avatar.jpg",
        company = null,  // Có thể thay thế giá trị này bằng một đối tượng Company nếu cần
        companyId = 1,
        companyTeam = null,  // Có thể thay thế giá trị này bằng một đối tượng Team nếu cần
        createdAt = "2023-10-25T12:34:56",
        department = "Engineering",
        designation = "Software Engineer",
        email = "user@example.com",
        employeeType = EmployeeType.PERNAMENT,  // Có thể là mã loại nhân viên
        id = 1001 + it, // Tăng ID mỗi lần tạo user
        phoneNumber = "+1234567890",
        role = Role.Staff,  // Có thể là mã vai trò (ví dụ: admin, user, v.v.)
        teamId = 1,
        userFullName = "John Doe $it", // Tạo tên người dùng theo chỉ số
        userName = "johndoe$it", // Tạo tên người dùng theo chỉ số
        dayOfBirth = LocalDate.now().toString()
    )
}
val exampleCompany = Company(
    id = 1,
    companyName = "test company",
    users = emptyList(),
    teams = emptyList()
)
val exampleTeam = Team(
    id = 1,
    name = "Development Team",
    companyId = 1,
    company = exampleCompany, // Gán company nếu có dữ liệu
    users = exampleUsers, // Danh sách người dùng
    latitude = 10.8231,
    longitude = 106.6297,
    createdAt = "2023-10-25T12:34:56",
    description = "Test description"
)

val exampleUser1 = User(
    address = "123 Main Street, Cityville",
    avatarUrl = "https://example.com/avatar.jpg",
    company = null,  // Bạn có thể thay thế giá trị này bằng một đối tượng Company nếu cần
    companyId = 1,
    companyTeam = null,  // Bạn có thể thay thế giá trị này bằng một đối tượng Team nếu cần
    createdAt = "2023-10-25T12:34:56",
    department = "Engineering",
    designation = "Software Engineer",
    email = "user@example.com",
    employeeType = EmployeeType.FULL_TIME,  // Có thể là mã loại nhân viên
    id = 1001,
    phoneNumber = "+1234567890",
    role = Role.Manager,  // Có thể là mã vai trò (ví dụ: admin, user, v.v.)
    teamId = 1,
    userFullName = "John Doe",
    userName = "johndoe",
    dayOfBirth = LocalDate.now().toString()
)


val fakeUserProfiles = List(10) {
    UserProfileDto(
        id = it,
        userName = "User $it",
        userFullName = "UserFullName $it",
        avatarUrl = "https://randomuser.me/api/portraits/men/$it.jpg"
    )
}

val fakeReport = List(5) {
    Report(
        id = it,
        title = "Report $it",
        description = "Description for Report $it",
        createdAt = LocalDateTime.now().minusDays(Random.nextLong(1, 30)),
        taskId = 1,
        author = fakeUserProfiles[0],
        reportUrl = "https://mkranrxtwhxvvafxwomq.supabase.co/storage/v1/object/public/reports/1/1/20250404134956_1.pdf"
    )
}

val fakeTasks = List(10) { index ->
    Task(
        id = index + 1,
        name = "Task ${index + 1}",
        description = "Description for Task ${index + 1}",
        projectId = Random.nextInt(1, 10), // Giả sử có 10 dự án
        createdAt = LocalDateTime.now().minusDays(Random.nextLong(1, 30)),
        dueDate = LocalDateTime.now().plusDays(Random.nextLong(1, 30)),
        status = ProjectStatus.entries.random(),
        priority = Priority.entries.random(),
        reports = fakeReport
    )
}

val fakeTaskStatistic = TaskStatistics(
    totalTask = 20,
    completedTask = 5,
    onHoldTask = 2,
    cancelledTask = 1,
    inProgressTask = 3,
    notStartedTask = 1
)

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
        statistics = fakeTaskStatistic
    )
}
