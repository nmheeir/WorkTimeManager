package com.kt.worktimetrackermanager.core.presentation.utils

import android.widget.Space
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Stable
@Composable
fun ColumnScope.Gap(modifier: Modifier = Modifier, height: Dp) {
    Spacer(modifier = modifier.height(height))
}

@Stable
@Composable
fun RowScope.Gap(modifier: Modifier = Modifier, width: Dp) {
    Spacer(modifier = modifier.width(width))
}