package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.DeviceTokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.UsernameKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.deletes
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.local.AppDatabase
import com.kt.worktimetrackermanager.data.local.entities.ProfileEntity
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: AppDatabase,
    private val userUseCase: UserUseCase,
) : ViewModel() {
    private val token = context.dataStore[TokenKey]!!

    val user = MutableStateFlow<User?>(null)

    val isLoading = MutableStateFlow(false)
    val isLogout = MutableStateFlow(false)

    private val _chanel = Channel<ProfileUiEvent>()
    val channel = _chanel.receiveAsFlow()

    init {
        getProfile()
    }

    private fun getProfile() {
        isLoading.value = true
        viewModelScope.launch {
            userUseCase.getUserProfile(token)
                .suspendOnSuccess {
                    user.value = this.data.data
                    Timber.d(this.data.data.toString())
                }
                .suspendOnFailure {
                    Timber.d(this.toString())
                }
                .suspendOnException {
                    Timber.d(this.toString())
                }

            isLoading.value = false
        }
    }

    fun logout() {
        isLogout.value = true
        viewModelScope.launch {
            context.dataStore.deletes(listOf(TokenKey, UsernameKey))
            database.clearProfile()
            _chanel.send(ProfileUiEvent.LogoutSuccess)
            isLogout.value = false
        }
    }
}

sealed interface ProfileUiEvent {
    object LogoutSuccess : ProfileUiEvent
}
