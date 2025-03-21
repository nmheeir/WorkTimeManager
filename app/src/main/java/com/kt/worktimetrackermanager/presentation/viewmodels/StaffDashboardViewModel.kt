package com.kt.worktimetrackermanager.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class StaffDashboardViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val staffId = savedStateHandle.get<Int>("staffId")!!
}