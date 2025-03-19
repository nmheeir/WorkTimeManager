package com.kt.worktimetrackermanager.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kt.worktimetrackermanager.domain.use_case.summary.SummaryUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DashBoardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val summaryUseCase: SummaryUseCase,
) : ViewModel() {


    init {

    }

}