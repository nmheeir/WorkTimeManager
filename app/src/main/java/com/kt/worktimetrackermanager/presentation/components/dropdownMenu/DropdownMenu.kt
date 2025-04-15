package com.kt.worktimetrackermanager.presentation.components.dropdownMenu

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlin.reflect.KProperty1

@Composable
fun <T : Enum<T>> DropdownMenuForEnum(
    enumValues: Array<T>,
    onItemSelected: (T?) -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    initialValue: T? = null,
    isNullable: Boolean = false,
    content: @Composable (displayText: String, onClick: () -> Unit) -> Unit = { defaultDisplayText, onClick ->
        // Default content with clickable behavior
        DefaultDropDown(defaultDisplayText, onClick)
    }
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember {
        mutableStateOf(
            initialValue ?: if (isNullable) null else enumValues.firstOrNull()
        )
    }

    val displayText = selectedItem?.name
        ?.lowercase()
        ?.replace('_', ' ')
        ?.replaceFirstChar { it.uppercase() } ?: text

    Box(modifier = modifier) {
        content(displayText, {expanded = true})
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isNullable) {
                DropdownMenuItem(
                    text = { Text("-----") },
                    onClick = {
                        selectedItem = null
                        onItemSelected(null)
                        expanded = false
                    }
                )
            }

            enumValues.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            item.name.lowercase().replace('_', ' ')
                                .replaceFirstChar { it.uppercase() }
                        )
                    },
                    onClick = {
                        selectedItem = item
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun <T> DropdownMenuForList(
    list: List<T>,
    onItemSelected: (T?) -> Unit,
    text: String,
    propertyName: String,
    modifier: Modifier = Modifier,
    initialValue: T? = null,
    content: @Composable (displayText: String, onClick: () -> Unit) -> Unit = { defaultDisplayText, onClick ->
        // Default content with clickable behavior
        DefaultDropDown(defaultDisplayText, onClick)
    }
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(initialValue) }

    val displayText = selectedItem?.let {
        it::class.members
            .filterIsInstance<KProperty1<T, *>>()
            .firstOrNull { member -> member.name == propertyName }
            ?.get(it)?.toString()
    } ?: text

    Box(modifier = modifier) {
        content(displayText, {expanded = true})

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            DropdownMenuItem(
                text = { Text("-----") },
                onClick = {
                    selectedItem = null
                    onItemSelected(null)
                    expanded = false
                }
            )

            list.forEach { item ->
                val itemText = item!!::class.members
                    .filterIsInstance<KProperty1<T, *>>()
                    .firstOrNull { it.name == propertyName }
                    ?.get(item)
                    ?.toString() ?: ""

                DropdownMenuItem(
                    text = { Text(itemText) },
                    onClick = {
                        selectedItem = item
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun DefaultDropDown(defaultDisplayText: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = defaultDisplayText,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Dropdown Arrow"
        )
    }
}