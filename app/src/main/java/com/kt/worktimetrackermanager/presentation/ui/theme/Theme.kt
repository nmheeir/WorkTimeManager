package com.kt.worktimetrackermanager.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.kt.worktimetrackermanager.core.presentation.ui.AppTheme
import com.kt.worktimetrackermanager.presentation.ui.theme.colorScheme.BaseColorScheme
import com.kt.worktimetrackermanager.presentation.ui.theme.colorScheme.DefaultColorScheme
import com.kt.worktimetrackermanager.presentation.ui.theme.colorScheme.DynamicColorScheme

@Composable
fun WorkTimeTrackerManagerTheme(
    appTheme: AppTheme = AppTheme.DEFAULT,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = getThemeColorScheme(appTheme, darkTheme),
        content = content
    )
}

@Composable
@ReadOnlyComposable
private fun getThemeColorScheme(
    appTheme: AppTheme,
    darkTheme: Boolean,
): ColorScheme {
    val colorScheme = if (appTheme == AppTheme.MONET) {
        DynamicColorScheme(LocalContext.current)
    } else {
        colorSchemes.getOrDefault(appTheme, DefaultColorScheme)
    }
    return colorScheme.getColorScheme(darkTheme)
}

private val colorSchemes: Map<AppTheme, BaseColorScheme> = mapOf(
    AppTheme.DEFAULT to DefaultColorScheme,
)