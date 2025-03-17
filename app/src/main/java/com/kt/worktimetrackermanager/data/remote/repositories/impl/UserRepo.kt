package com.kt.worktimetrackermanager.data.remote.repositories.impl

import com.kt.worktimetrackermanager.data.remote.api.UserApi
import com.kt.worktimetrackermanager.data.remote.dto.request.AddUserRequest
import com.kt.worktimetrackermanager.data.remote.dto.request.UpdateUserRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.data.remote.dto.response.UsersStatistic
import com.kt.worktimetrackermanager.data.remote.repositories.IUserRepo
import com.skydoves.sandwich.ApiResponse

class UserRepo(
    private val userApi: UserApi
) : IUserRepo {
    override suspend fun getUserByUsername(username: String): ApiResponse<DataResponse<User>> {
        return userApi.getUserByUsername(username)
    }

    override suspend fun getUserById(id: Int): ApiResponse<DataResponse<User>> {
        return userApi.getUserById(id)
    }

    override suspend fun addUser(
        token: String,
        user: AddUserRequest
    ): ApiResponse<DataResponse<Any>> {
        return userApi.addUser("Bearer $token", user)
    }


    override suspend fun updateUser(
        token: String,
        user: UpdateUserRequest
    ): ApiResponse<DataResponse<Any>> {
        return userApi.updateUser("Bearer $token", user)
    }

    override suspend fun getUsersInTeam(
        token: String,
        pageNumber: Int,
        pageSize: Int,
        username: String,
        role: Int?,
        employeeType: Int?,
        active: Boolean,
        teamId: Int?
    ): ApiResponse<PagedDataResponse<List<User>>> {
        return userApi.getUsersInTeam(
            token = token,
            pageNumber = pageNumber,
            pageSize = pageSize,
            username = username,
            role = role,
            active = active,
            teamId = teamId
        )
    }

    override suspend fun getUsers(
        token: String,
        pageNumber: Int,
        pageSize: Int,
        username: String,
        role: Int?,
        employeeType: Int?,
        active: Boolean,
        teamId: Int?
    ): ApiResponse<PagedDataResponse<List<User>>> {
        return userApi.getUsers(
            "Bearer $token",
            pageNumber,
            pageSize,
            username,
            role,
            employeeType,
            active,
            teamId
        )

    }

    override suspend fun getUsersStatistic(token: String): ApiResponse<DataResponse<UsersStatistic>> {
        return userApi.getUsersStatistic("Bearer $token")
    }
}