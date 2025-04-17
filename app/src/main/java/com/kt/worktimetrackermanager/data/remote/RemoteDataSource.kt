package com.kt.worktimetrackermanager.data.remote

import com.google.gson.GsonBuilder
import com.kt.worktimetrackermanager.BuildConfig
import com.kt.worktimetrackermanager.data.remote.adapters.EmployeeTypeAdapter
import com.kt.worktimetrackermanager.data.remote.adapters.LocalDateTimeAdapter
import com.kt.worktimetrackermanager.data.remote.adapters.PriorityAdapter
import com.kt.worktimetrackermanager.data.remote.adapters.ProjectStatusAdapter
import com.kt.worktimetrackermanager.data.remote.adapters.RoleAdapter
import com.kt.worktimetrackermanager.data.remote.adapters.ShiftTypeAdapter
import com.kt.worktimetrackermanager.data.remote.dto.enums.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enums.Priority
import com.kt.worktimetrackermanager.data.remote.dto.enums.ProjectStatus
import com.kt.worktimetrackermanager.data.remote.dto.enums.Role
import com.kt.worktimetrackermanager.data.remote.dto.enums.ShiftType
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RemoteDataSource @Inject constructor() {
    fun <Api> buildApi(
        api: Class<Api>,
    ): Api {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .registerTypeAdapter(Role::class.java, RoleAdapter())
            .registerTypeAdapter(ProjectStatus::class.java, ProjectStatusAdapter())
            .registerTypeAdapter(EmployeeType::class.java, EmployeeTypeAdapter())
            .registerTypeAdapter(Priority::class.java, PriorityAdapter())
            .registerTypeAdapter(ShiftType::class.java, ShiftTypeAdapter())
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
            .create(api)
    }
}