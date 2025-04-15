package com.kt.worktimetrackermanager.presentation

import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import com.kt.worktimetrackermanager.data.remote.dto.response.Company
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import java.time.LocalDate

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
    employeeType = 1,  // Có thể là mã loại nhân viên
    id = 1001,
    phoneNumber = "+1234567890",
    role = Role.STAFF,  // Có thể là mã vai trò (ví dụ: admin, user, v.v.)
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
        employeeType = (index % 3) + 1,
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
        employeeType = 1,  // Có thể là mã loại nhân viên
        id = 1001 + it, // Tăng ID mỗi lần tạo user
        phoneNumber = "+1234567890",
        role = Role.STAFF,  // Có thể là mã vai trò (ví dụ: admin, user, v.v.)
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
    employeeType = 1,  // Có thể là mã loại nhân viên
    id = 1001,
    phoneNumber = "+1234567890",
    role = Role.MANAGER,  // Có thể là mã vai trò (ví dụ: admin, user, v.v.)
    teamId = 1,
    userFullName = "John Doe",
    userName = "johndoe",
    dayOfBirth = LocalDate.now().toString()
)