package com.kt.worktimetrackermanager.presentation.screens.memberManager.component

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.kt.worktimetrackermanager.R

@Composable
fun MyOutlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    text: String?,
    icon: ImageVector,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary, // Màu viền khi focus
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant, // Màu viền khi không focus
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            label = {
                Text(text ?: "", color = MaterialTheme.colorScheme.outline)
            },
            leadingIcon = {
                Icon(
                    imageVector = icon, // Biểu tượng yêu thích
                    contentDescription = "Favorite Icon",
                    tint = MaterialTheme.colorScheme.onBackground // Màu sắc cho icon
                )
            },
            shape = RoundedCornerShape(40.dp),
            singleLine = true,
            keyboardOptions = keyboardOptions,
            textStyle = TextStyle(
                color = Color.White,
            )
        )

}

@Composable
fun MyOutlineTextField(modifier: Modifier = Modifier ,value: String, onClick: () -> Unit, text: String, icon: ImageVector) {
        Text(
            text = text,
            modifier = modifier
                .padding(start = 8.dp, bottom = 12.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White, // Màu viền khi focus
                unfocusedBorderColor = Color.White.copy(alpha = 0.5f), // Màu viền khi không focus
                focusedContainerColor = Color.White.copy(alpha = 0.2f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.2f),
                disabledContainerColor = Color.White.copy(alpha = 0.2f),
            ),
            placeholder = {
                Text(value, color = Color.Gray)
            },
            leadingIcon = {
                Icon(
                    imageVector = icon, // Biểu tượng yêu thích
                    contentDescription = "Favorite Icon",
                    tint = MaterialTheme.colorScheme.onBackground // Màu sắc cho icon
                )
            },
            shape = RoundedCornerShape(20.dp),
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
            )
        )

}