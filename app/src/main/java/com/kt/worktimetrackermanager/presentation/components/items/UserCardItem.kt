package com.kt.worktimetrackermanager.presentation.components.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.remote.dto.enums.Role
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.components.image.CoilImage

@Composable
fun UserCardItem(
    modifier: Modifier = Modifier,
    user: User,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.extraSmall),
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.padding.mediumSmall)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CoilImage(
                    imageUrl = user.avatarUrl!!,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(36.dp)
                )
                Text(
                    text = user.userName,
                    modifier = Modifier
                        .weight(1f)
                )
                RoleBadge(role = user.role)
            }
            Text(
                text = user.department
            )
            Text(
                text = user.designation
            )
        }
    }
}


@Composable
private fun RoleBadge(
    modifier: Modifier = Modifier,
    role: Role,
) {
    val textColor = when (role) {
        Role.Master -> MaterialTheme.colorScheme.onErrorContainer
        Role.Manager -> MaterialTheme.colorScheme.onPrimaryContainer
        Role.Staff -> MaterialTheme.colorScheme.onTertiaryContainer
    }
    val bgColor = when (role) {
        Role.Master -> MaterialTheme.colorScheme.errorContainer
        Role.Manager -> MaterialTheme.colorScheme.primaryContainer
        Role.Staff -> MaterialTheme.colorScheme.tertiaryContainer
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(bgColor)

    ) {
        Text(
            text = role.name,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            modifier = Modifier
                .padding(MaterialTheme.padding.small)
        )
    }
}