package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MiddlewareRoleViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userUseCase: UserUseCase,
) : ViewModel() {

    private val token = context.dataStore[TokenKey]

    val role = MutableStateFlow<Role?>(null)
    val isRefresh = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            checkRole()
        }
    }

    fun reload() {
        isRefresh.value = true
        viewModelScope.launch {
            checkRole()
            isRefresh.value = false
        }
    }

    private fun checkRole() {
        if (token == null) {
            role.value = Role.UNAUTHORIZED
            return
        }
        viewModelScope.launch {
            userUseCase.getUserProfile(token)
                .suspendOnSuccess {
                    val roleIndex = this.data.data!!.role
                    when (roleIndex) {
                        0 -> role.value = Role.MASTER
                        1 -> role.value = Role.MANAGER
                        else -> role.value = Role.UNAUTHORIZED
                    }
                }
                .suspendOnFailure {
                    Timber.d("Failure: %s", this.message())
                    role.value = Role.UNAUTHORIZED
                }
                .suspendOnException {
                    Timber.d("Exception: %s", this.message())
                    role.value = Role.UNAUTHORIZED
                }
        }
    }
}
