package com.kt.worktimetrackermanager.presentation.components.image

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp


@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    painter: Painter,
    size: Int
) {
    Image(
        painter = painter,
        contentDescription = "Image 1",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size.dp)
            .border(
                border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primaryContainer),
                RoundedCornerShape(50.dp)
            )
            .clip(RoundedCornerShape(50.dp))
    )
}