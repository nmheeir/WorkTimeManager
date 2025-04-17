package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kt.worktimetrackermanager.core.presentation.utils.UsernameKey
import com.kt.worktimetrackermanager.core.presentation.utils.dataStore
import com.kt.worktimetrackermanager.core.presentation.utils.get
import com.kt.worktimetrackermanager.data.local.AppDatabase
import com.kt.worktimetrackermanager.data.local.entities.NotificationEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val database: AppDatabase,
) : ViewModel() {

    val notifications = database.notifications(context.dataStore[UsernameKey] ?: "")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    val isLoadingDeleteMultiple = MutableStateFlow(false)
    val isLoadingMarkMultipleAsRead = MutableStateFlow(false)


    fun onAction(action: NotificationUiAction) {
        when (action) {
            is NotificationUiAction.Delete -> {
                viewModelScope.launch {
                    database.delete(action.notification)
                }
            }

            NotificationUiAction.DeleteAll -> {
                isLoadingDeleteMultiple.value = true
                viewModelScope.launch {
                    database.delete(notifications.value)
                    isLoadingDeleteMultiple.value = false
                }
            }

            is NotificationUiAction.MarkAsRead -> {
                viewModelScope.launch {
                    database.update(action.notification.copy(isRead = true))
                }
            }

            NotificationUiAction.MarkAllAsRead -> {
                isLoadingMarkMultipleAsRead.value = true
                viewModelScope.launch {
                    database.update(
                        notifications.value.filter { !it.isRead }.map { it.copy(isRead = true) }
                    )
                    isLoadingMarkMultipleAsRead.value = false
                }
            }

            is NotificationUiAction.DeleteMultipleNotifications -> {
                isLoadingDeleteMultiple.value = true
                viewModelScope.launch {
                    database.deleteByIds(action.selectedNotifications)
                    isLoadingDeleteMultiple.value = false
                }
            }

            is NotificationUiAction.MarkMultipleAsRead -> {
                isLoadingMarkMultipleAsRead.value = true
                viewModelScope.launch {
                    database.updateByIds(action.selectedNotifications, true)
                    isLoadingMarkMultipleAsRead.value = false
                }
            }
        }
    }

}

sealed interface NotificationUiAction {
    data class MarkAsRead(val notification: NotificationEntity) : NotificationUiAction
    data object MarkAllAsRead : NotificationUiAction
    data class MarkMultipleAsRead(val selectedNotifications: List<Int>) : NotificationUiAction
    data object DeleteAll : NotificationUiAction
    data class Delete(val notification: NotificationEntity) : NotificationUiAction
    data class DeleteMultipleNotifications(val selectedNotifications: List<Int>) :
        NotificationUiAction
}