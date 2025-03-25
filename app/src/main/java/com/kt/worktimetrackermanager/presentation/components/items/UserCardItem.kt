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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.components.image.CoilImage
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme

@Composable
fun UserCardItem(
    modifier: Modifier = Modifier,
    user: User,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.padding.small)
            ) {
                CoilImage(
                    imageUrl = user.avatarUrl!!,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(36.dp)
                )
                Text(
                    text = user.userName
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
        Role.MASTER -> MaterialTheme.colorScheme.onErrorContainer
        Role.MANAGER -> MaterialTheme.colorScheme.onPrimaryContainer
        Role.STAFF -> MaterialTheme.colorScheme.onTertiaryContainer
    }
    val bgColor = when (role) {
        Role.MASTER -> MaterialTheme.colorScheme.errorContainer
        Role.MANAGER -> MaterialTheme.colorScheme.primaryContainer
        Role.STAFF -> MaterialTheme.colorScheme.tertiaryContainer
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