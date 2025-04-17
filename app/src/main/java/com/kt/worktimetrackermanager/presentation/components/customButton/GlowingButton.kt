package com.kt.worktimetrackermanager.presentation.components.customButton

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun GlowingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF2196F3),
                        Color(0xFF00BCD4)
                    )
                )
            )
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.5f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(1.dp)
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(23.dp)
            )
            .padding(1.dp)
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(22.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

// Usage example:
@Composable
@Preview
fun PreviewGlowingButton() {
    GlowingButton(
        modifier = Modifier.width(200.dp)
    ) {
        Text(
            text = "Glow Button",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}