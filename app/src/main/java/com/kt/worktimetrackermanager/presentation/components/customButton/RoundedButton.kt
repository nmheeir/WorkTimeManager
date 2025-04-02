package com.kt.worktimetrackermanager.presentation.components.customButton

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp


@Composable
fun RoundedButton(
    size: Int,
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable RowScope.() -> Unit,
) {
    val corner : Int = size / 2;
    Button(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape(corner.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row {
            content()
        }
    }
}