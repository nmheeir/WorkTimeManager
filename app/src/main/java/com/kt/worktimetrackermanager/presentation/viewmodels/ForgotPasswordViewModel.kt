package com.kt.worktimetrackermanager.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.ext.isValidEmail
import com.kt.worktimetrackermanager.domain.use_case.AuthUseCase
import com.skydoves.sandwich.StatusCode
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.retrofit.statusCode
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    val uiState = MutableStateFlow(ForgotPasswordUiState())

    private val _channel = Channel<ForgotPasswordUiEvent>()
    val channel = _channel.receiveAsFlow()

    private val _channel2 = Channel<CreateNewPasswordUiEvent>()
    val channel2 = _channel2.receiveAsFlow()

    fun onAction(event: ForgotPasswordUiAction) {
        when (event) {
            is ForgotPasswordUiAction.OnEmailChange -> {
                uiState.update {
                    it.copy(
                        email = event.email,
                        isButtonEnabled = event.email.isValidEmail()
                    )
                }
            }

            ForgotPasswordUiAction.SendRequest -> {
                if (!uiState.value.email.isValidEmail()) {
                    uiState.update {
                        it.copy(
                            error = "Email is not valid",
                            isError = true
                        )
                    }
                } else {
                    sendRequest(uiState.value.email)
                }
            }

            is ForgotPasswordUiAction.OnConfirmPasswordChange -> {
                uiState.update {
                    it.copy(
                        confirmPassword = event.confirmPassword
                    )
                }
            }

            is ForgotPasswordUiAction.OnNewPasswordChange -> {
                uiState.update {
                    it.copy(
                        newPassword = event.newPassword
                    )
                }
            }

            ForgotPasswordUiAction.ResetNewPassword -> {
                resetNewPassword()
            }

            ForgotPasswordUiAction.PasswordNotMatch -> {
                uiState.update {
                    it.copy(
                        error = "Password not match",
                        isError = true
                    )
                }
            }

            ForgotPasswordUiAction.CheckPassword -> {
                Timber.d("onAction: ${uiState.value.newPassword} ${uiState.value.confirmPassword}")
                if (uiState.value.newPassword == uiState.value.confirmPassword) {
                    uiState.update {
                        it.copy(
                            isButtonEnabled = true
                        )
                    }
                } else {
                    uiState.update {
                        it.copy(
                            error = "Password do not match",
                            isError = false,
                            isButtonEnabled = false
                        )
                    }
                }
            }

            is ForgotPasswordUiAction.UpdateToken -> {
                uiState.update {
                    it.copy(
                        token = event.token
                    )
                }
            }
        }
    }

    private fun resetNewPassword() {
        viewModelScope.launch {
            val token = "Bearer " + uiState.value.token
            authUseCase.resetPassword(token, uiState.value.newPassword)
                .suspendOnSuccess {
                    _channel2.send(CreateNewPasswordUiEvent.ResetPasswordSuccess)
                }
                .suspendOnError {
                    _channel2.send(CreateNewPasswordUiEvent.ResetPasswordFailure(this.errorBody.toString()))
                }
                .suspendOnException {
                    Timber.d(
                        "resetNewPassword exception: " + this.message
                    )
                }
        }
    }

    private fun sendRequest(email: String) {
        viewModelScope.launch {
            authUseCase.requestPasswordReset(email)
                .suspendOnSuccess {
                    _channel.send(ForgotPasswordUiEvent.SendRequestSuccess)
                }
                .suspendOnError {
                    when (this.statusCode) {
                        StatusCode.NotFound -> {
                            _channel.send(ForgotPasswordUiEvent.NotFoundUser)
                        }

                        else -> {
                            Timber.d("sendRequest: ${this.statusCode} + ${this.errorBody}")
                            _channel.send(ForgotPasswordUiEvent.UnknownError)
                        }
                    }
                }
                .suspendOnException {
                    Timber.d(
                        "sendRequest exception: " + this.message
                    )
                }
        }
    }

}

data class ForgotPasswordUiState(
    val token: String = "",

    val email: String = "",
    val isLoading: Boolean = false,

    val newPassword: String = "",
    val confirmPassword: String = "",

    val isError: Boolean = false,
    val error: String = "",

    val isButtonEnabled: Boolean = false
)

sealed class ForgotPasswordUiEvent {
    data object SendRequestSuccess : ForgotPasswordUiEvent()
    data object NotFoundUser : ForgotPasswordUiEvent()
    data object UnknownError : ForgotPasswordUiEvent()

}

sealed class ForgotPasswordUiAction {
    data class OnEmailChange(val email: String) : ForgotPasswordUiAction()
    data object SendRequest : ForgotPasswordUiAction()
    data class UpdateToken(val token: String) : ForgotPasswordUiAction()

    data class OnNewPasswordChange(val newPassword: String) : ForgotPasswordUiAction()
    data class OnConfirmPasswordChange(val confirmPassword: String) : ForgotPasswordUiAction()
    data object CheckPassword : ForgotPasswordUiAction()
    data object PasswordNotMatch : ForgotPasswordUiAction()

    data object ResetNewPassword : ForgotPasswordUiAction()
}

sealed class CreateNewPasswordUiEvent {
    data object ResetPasswordSuccess : CreateNewPasswordUiEvent()
    data class ResetPasswordFailure(val msg: String) : CreateNewPasswordUiEvent()
}