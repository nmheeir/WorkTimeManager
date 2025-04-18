package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.TokenKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.delete
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import com.skydoves.sandwich.message
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
class HomeViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val token = context.dataStore[TokenKey]!!

    val user = MutableStateFlow<User?>(null)

    fun onAction(action: HomeUiAction) {
        when (action) {
            HomeUiAction.Stuff -> {}
        }
    }

    init {
        getUserProfile()
    }

    fun getUserProfile() {
        viewModelScope.launch {
            userUseCase.getUserProfile(token)
                .suspendOnSuccess {
                    user.value = this.data.data!!
                    Timber.d("Success: ${this.data}")
                }
                .suspendOnFailure {
                    Timber.d("Failure:%s", this.message())
                    context.dataStore.delete(TokenKey)
                    return@suspendOnFailure
                }
                .suspendOnException {
                    Timber.d("Exception:%s", this.message())
                    context.dataStore.delete(TokenKey)
                    return@suspendOnException
                }
        }
    }


}

sealed interface HomeUiAction {
    data object Stuff : HomeUiAction
}