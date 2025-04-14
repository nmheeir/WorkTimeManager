package com.kt.worktimetrackermanager.data.remote.dto.response

data class DataResponse<T>(
    val data: T?,
    val message: String,
    val success: Boolean,
    val statusCode: String
)