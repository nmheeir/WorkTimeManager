package com.kt.worktimetrackermanager.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.ui.EmptyBox
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme

@Composable
fun EmptyCardState(
    modifier: Modifier = Modifier,
    @StringRes message: Int,
    @StringRes desc: Int,
    @DrawableRes icon: Int,
    onClick: (() -> Unit)? = null,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        onClick = {
            onClick?.invoke()
        },
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            EmptyBox(
                stringRes = message,
                descRes = desc,
                iconRes = icon,
            )
        }
    }
}