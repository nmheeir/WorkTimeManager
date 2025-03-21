package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.delete
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.apiMessage
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userUseCase: UserUseCase,
) : ViewModel() {

    val showSplash = mutableStateOf(false)

    val startDestination = mutableStateOf<String?>(null)

    val user = MutableStateFlow<User?>(null)
    private val token = context.dataStore[TokenKey]

    init {
        showSplash.value = true
        viewModelScope.launch {
            checkToken()
        }
    }

    private suspend fun checkToken() {

        if (token.isNullOrEmpty()) {
            startDestination.value = "login"
            return
        }

//        userUseCase.getUserProfile(token)
//            .suspendOnSuccess {
//                Timber.d(this.data.data.toString())
//                startDestination.value = "home"
//                user.value = this.data.data!!
//            }
//            .suspendOnFailure {
//                startDestination.value = "login"
//                context.dataStore.delete(TokenKey)
//                Timber.d(this.apiMessage)
//            }
//            .suspendOnException {
//                startDestination.value = "login"
//                context.dataStore.delete(TokenKey)
//                Timber.d(this.apiMessage)
//            }

        startDestination.value = "home"

        showSplash.value = false
    }

    fun logout() {
        viewModelScope.launch {
            context.dataStore.delete(TokenKey)
            user.value = null
        }
    }
}