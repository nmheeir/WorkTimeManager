package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.core.presentation.utils.set
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.kt.worktimetrackermanager.presentation.navigations.Screens
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
    private val userUseCase: UserUseCase
) : ViewModel() {

    val showSplash = mutableStateOf(false)

    val startDestination = mutableStateOf("login")

    private val user = MutableStateFlow<User?>(null)

    init {
        viewModelScope.launch {
            checkToken()
            showSplash.value = false
        }
    }

    private fun checkToken() {
        viewModelScope.launch {
            val token = context.dataStore[TokenKey]

            if (token.isNullOrEmpty()) {
                return@launch
            }

            startDestination.value = "home"
            user.value = userUseCase.

            Timber.d(startDestination.value)
            Timber.d(token)
            return@launch
        }
    }

}