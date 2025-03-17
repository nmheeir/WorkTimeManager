package com.kt.worktimetrackermanager.data.remote.dto.request

data class UserLoginRequest(
    val username: String,
    val password: String,
    val deviceToken: String
)