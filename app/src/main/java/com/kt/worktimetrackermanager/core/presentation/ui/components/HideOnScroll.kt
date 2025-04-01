package com.kt.worktimetrackermanager.core.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kt.worktimetrackermanager.core.presentation.utils.isScrollingUp

@Composable
fun BoxScope.HideOnScrollComponent(
    visible: Boolean = true,
    lazyListState: LazyListState,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible && lazyListState.isScrollingUp(),
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
        modifier = Modifier
            .align(Alignment.BottomEnd)
    ) {
        content()
    }
}