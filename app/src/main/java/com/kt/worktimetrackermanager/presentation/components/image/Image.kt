package com.kt.worktimetrackermanager.presentation.components.image

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleImage(
    imageUrl: String,
    size: Dp = 24.dp,
) {
    CoilImage(
        imageUrl = imageUrl,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    )
}