package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.set
import com.kt.worktimetrackermanager.domain.use_case.AuthUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authUseCase: AuthUseCase
) : ViewModel() {

    val username = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _channel = Channel<LoginUiEvent>()
    val channel = _channel.receiveAsFlow()

    fun onAction(action: LoginUiAction) {
        when (action) {
            is LoginUiAction.Login -> login(action.username, action.password)
            is LoginUiAction.OnPasswordChange -> {
                username.update {
                    action.newPassword
                }
            }

            is LoginUiAction.OnUsernameChange -> {
                password.update {
                    action.newUsername
                }
            }
        }
    }

    init {
        Timber.d("LoginViewModel initialized")
    }

    private fun login(username: String, password: String) {
        viewModelScope.launch {
            authUseCase.login(username, password, "test")
                .suspendOnSuccess {
                    Timber.d("Success: ${this.data}")
                    context.dataStore.set(TokenKey, this.data.data!!.token)
                    _channel.send(LoginUiEvent.LoginSuccess)
                }
                .suspendOnFailure {
                    Timber.d("Failure: ${this.message()}")
                }
                .suspendOnException {
                    Timber.d("Exception: ${this.message}")
                }
        }
    }


}

sealed interface LoginUiEvent {
    data object LoginSuccess : LoginUiEvent
    data class LoginFailure(val msg: String) : LoginUiEvent
}

sealed interface LoginUiAction {
    data class Login(val username: String, val password: String) : LoginUiAction

    data class OnUsernameChange(val newUsername: String) : LoginUiAction
    data class OnPasswordChange(val newPassword: String) : LoginUiAction
}