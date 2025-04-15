package com.kt.worktimetrackermanager.presentation.components.image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.kt.worktimetrackermanager.data.remote.dto.response.UserProfileDto
import com.kt.worktimetrackermanager.presentation.fakeUserProfiles
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme

@Composable
fun AvatarGroup(
    modifier: Modifier = Modifier,
    assignees: List<UserProfileDto>,
) {
    // Khoảng cách offset âm giữa các avatar (có thể điều chỉnh nếu cần)
    val overlapOffset = 8.dp

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Hiển thị tối đa 3 avatar
        assignees.take(3).fastForEachIndexed { index, profile ->
            CoilImage(
                imageUrl = profile.avatarUrl,
                contentDescription = profile.userFullName,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.surface, CircleShape)
            )
        }
        if (assignees.size > 3) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    .border(1.dp, MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Text(
                    text = "+${assignees.size - 3}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Test() {
    WorkTimeTrackerManagerTheme {
        AvatarGroup(
            assignees = fakeUserProfiles
        )
    }
}