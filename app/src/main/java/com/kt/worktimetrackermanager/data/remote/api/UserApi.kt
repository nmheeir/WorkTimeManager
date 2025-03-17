package com.kt.worktimetrackermanager.data.remote.api

import com.kt.worktimetrackermanager.data.remote.dto.request.AddUserRequest
import com.kt.worktimetrackermanager.data.remote.dto.request.UpdateUserRequest
import com.kt.worktimetrackermanager.data.remote.dto.response.DataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.PagedDataResponse
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.data.remote.dto.response.UsersStatistic
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApi {

    @GET("Users/GetUserByUserName")
    suspend fun getUserByUsername(
        @Query("userName") userName: String
    ): ApiResponse<DataResponse<User>>

    @GET("Users/GetUserById")
    suspend fun getUserById(
        @Query("id") id: Int
    ): ApiResponse<DataResponse<User>>

    @POST("Users/addUser")
    suspend fun addUser(
        @Header("Authorization") token: String,
        @Body user: AddUserRequest
    ): ApiResponse<DataResponse<Any>>

    @PUT("Users/update")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body user: UpdateUserRequest
    ): ApiResponse<DataResponse<Any>>

    @GET("Users/GetUsersInCompany")
    suspend fun getUsersInTeam(
        @Header("Authorization") token: String,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("username") username: String = "",
        @Query("role") role: Int? = null,
        @Query("employeeType") employeeType: Int? = null,
        @Query("active") active: Boolean = true,
        @Query("teamId") teamId: Int? = null
    ): ApiResponse<PagedDataResponse<List<User>>>

    @GET("Users/GetUsersInCompany")
    suspend fun getUsers(
        @Header("Authorization") token: String,
        @Query("pageNumber") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("username") username: String = "",
        @Query("role") role: Int? = null,
        @Query("employeeType") employeeType: Int? = null,
        @Query("active") active: Boolean = true,
        @Query("teamId") teamId: Int? = null
    ): ApiResponse<PagedDataResponse<List<User>>>

    @GET("Users/GetUsersStatistic")
    suspend fun getUsersStatistic(
        @Header("Authorization") token: String,
    ): ApiResponse<DataResponse<UsersStatistic>>
}