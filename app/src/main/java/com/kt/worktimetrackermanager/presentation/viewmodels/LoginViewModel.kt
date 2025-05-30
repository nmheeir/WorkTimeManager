package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.DeviceTokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.UsernameKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.delete
import com.kt.worktimetrackermanager.core.presentation.utils.deletes
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.core.presentation.utils.set
import com.kt.worktimetrackermanager.data.local.AppDatabase
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.data.remote.dto.response.toProfileEntity
import com.kt.worktimetrackermanager.domain.use_case.AuthUseCase
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.message
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.retrofit.statusCode
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val database: AppDatabase,
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase,
) : ViewModel() {

    private val token = context.dataStore[TokenKey]

    val uiState = MutableStateFlow(LoginUiState())

    private val _channel = Channel<LoginUiEvent>()
    val channel = _channel.receiveAsFlow()

    val isLoading = MutableStateFlow(false)

    fun onAction(action: LoginUiAction) {
        when (action) {
            LoginUiAction.Login -> {
                login()
            }

            is LoginUiAction.OnPasswordChange -> {
                uiState.update {
                    it.copy(
                        password = action.password
                    )
                }
            }

            is LoginUiAction.OnUsernameChange -> {
                uiState.update {
                    it.copy(
                        username = action.username
                    )
                }
            }

            is LoginUiAction.UpdateError -> {
                uiState.update {
                    it.copy(
                        error = action.error,
                        isError = true
                    )
                }
            }

            is LoginUiAction.OnRememberLogin -> {
                uiState.update {
                    it.copy(
                        rememberLogin = action.isRemember
                    )
                }
            }
        }
    }

    init {
        Timber.d("LoginViewModel initialized")
        checkTokeExpire()
    }

    private fun login() {
        isLoading.value = true
        viewModelScope.launch {
            val deviceToken = context.dataStore[DeviceTokenKey]
            if (deviceToken == null) {
                _channel.send(LoginUiEvent.Failure("Device token is null"))
                return@launch
            }

            authUseCase.login(uiState.value.username, uiState.value.password, deviceToken)
                .suspendOnSuccess {
                    Timber.d("Success: ${this.data}")
                    Timber.d("Token: ${this.data.data!!.token}")
                    context.dataStore.set(TokenKey, this.data.data!!.token)
                    _channel.send(LoginUiEvent.Success)
                }
                .suspendOnError {
                    when (this.statusCode) {
                        StatusCode.BadRequest -> {
                            val error = this.errorBody?.string()
                            error.let {
                                Timber.d("LoginScreen BadRequest: $it")
                            }
                            _channel.send(LoginUiEvent.WrongPassword("Wrong password"))
                        }

                        StatusCode.NotFound -> {
                            _channel.send(LoginUiEvent.UserNotFound("User not found"))
                        }

                        else -> {
                            Timber.d("Login error else: " + this.statusCode + this.message())
                        }
                    }
                    Timber.d("Failure: ${this.message()}")
                }
                .suspendOnException {
                    _channel.send(LoginUiEvent.Failure(this.message ?: ""))
                    Timber.d("Exception: ${this.message}")
                }

            isLoading.value = false
        }
    }

    private fun checkTokeExpire() {
        if (token.isNullOrEmpty()) {
            return
        } else {
            isLoading.value = true
            viewModelScope.launch {
                userUseCase.getUserProfile(token)
                    .suspendOnSuccess {
                        Timber.d("Success: ${this.data}")
                        insertProfile(this.data.data!!)
                        _channel.send(LoginUiEvent.Success)
                    }
                    .suspendOnFailure {
                        clearData()
                        Timber.d("Failure:%s", this.message())
                        context.dataStore.delete(TokenKey)
                    }
                    .suspendOnException {
                        Timber.d("Exception:%s", this.message())
                        clearData()
                        context.dataStore.delete(TokenKey)
                    }
            }
            isLoading.value = false
        }
    }

    private fun clearData() {
        viewModelScope.launch {
            database.clearProfile()
            context.dataStore.deletes(listOf(TokenKey, UsernameKey))
        }
    }

    private suspend fun insertProfile(user: User) {
        database.insert(user.toProfileEntity())
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,

    val username: String = "",
    val isUsernameEmpty: Boolean = false,

    val password: String = "",
    val isPasswordEmpty: Boolean = false,

    val error: String = "",
    val isError: Boolean = false,

    val rememberLogin: Boolean = false,
)


sealed interface LoginUiEvent {
    data class UserNotFound(val msg: String) : LoginUiEvent
    data class WrongPassword(val msg: String) : LoginUiEvent

    data object Success : LoginUiEvent
    data class Failure(val msg: String) : LoginUiEvent
}


sealed interface LoginUiAction {
    data class OnUsernameChange(val username: String) : LoginUiAction
    data class OnPasswordChange(val password: String) : LoginUiAction
    data object Login : LoginUiAction
    data class OnRememberLogin(val isRemember: Boolean) : LoginUiAction

    data class UpdateError(val error: String) : LoginUiAction
}