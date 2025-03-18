package com.kt.worktimetrackermanager.data.remote.repositories

import com.kt.worktimetrackermanager.data.remote.dto.request.AddUserRequest
import com.kt.worktimetrackermanager.data.remote.dto.request.UpdateUserRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.data.remote.dto.response.UsersStatistic
import com.skydoves.sandwich.ApiResponse

interface IUserRepo {

    suspend fun getUserByUsername(
        username: String
    ): ApiResponse<DataResponse<User>>

    suspend fun getUserById(
        id: Int
    ): ApiResponse<DataResponse<User>>

    suspend fun addUser(
        token: String,
        user: AddUserRequest
    ): ApiResponse<DataResponse<Any>>

    suspend fun updateUser(
        token: String,
        user: UpdateUserRequest
    ): ApiResponse<DataResponse<Any>>

    suspend fun getUsersInTeam(
        token: String,
        pageNumber: Int = 1,
        pageSize: Int = 20,
        username: String = "",
        role: Int? = null,
        employeeType: Int? = null,
        active: Boolean = true,
        teamId: Int? = null
    ): ApiResponse<PagedDataResponse<List<User>>>

    suspend fun getUsers(
        token: String,
        pageNumber: Int = 1,
        pageSize: Int = 20,
        username: String = "",
        role: Int? = null,
        employeeType: Int? = null,
        active: Boolean = true,
        teamId: Int? = null
    ): ApiResponse<PagedDataResponse<List<User>>>

    suspend fun getUsersStatistic(
        token: String
    ): ApiResponse<DataResponse<UsersStatistic>>
}