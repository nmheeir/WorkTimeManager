package com.kt.worktimetrackermanager.domain.use_case.user

data class UserUseCase(
    val getUserProfile: GetUserProfile,
    val getUserByUsername: GetUserByUsername,
    val addUser: AddUser,
    val getUserInTeam: GetUserInTeam,
    val getUserById: GetUserById,
    val updateUser: UpdateUser,
    val getUsers: GetUsers,
    val getUsersStatistic: GetUsersStatistic,
)
