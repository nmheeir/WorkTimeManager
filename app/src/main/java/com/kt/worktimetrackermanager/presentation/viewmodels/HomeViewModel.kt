package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kt.worktimetrackermanager.domain.use_case.user.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {


    val user = MutableStateFlow("")

    fun onAction(action: HomeUiAction) {
        when (action) {
            HomeUiAction.Stuff -> {}
        }
    }


}

sealed interface HomeUiAction {
    data object Stuff : HomeUiAction
}