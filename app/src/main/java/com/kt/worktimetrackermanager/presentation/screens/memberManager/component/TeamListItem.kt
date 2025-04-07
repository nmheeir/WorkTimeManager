package com.kt.worktimetrackermanager.presentation.screens.memberManager.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.Helper
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.components.image.UserAvatar
import com.kt.worktimetrackermanager.presentation.exampleTeam
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme

const val TEAM_MEMBER_AVATAR_SHOW = 3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamListItem(team: Team, onNavigate: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                BorderStroke(
                    4.dp, brush = Brush.linearGradient(
                        listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFF99BADD)
                        )
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = team.name,
                style = MaterialTheme.typography.titleLarge,
//                color = colorResource(R.color.theme)
            )
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ){
                Icon(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = Helper.convertToCustomDateFormat2(team.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            }

            TeamMemberAvatarList(team)
        }

        Text(
            text = "More Detail",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(12.dp)
                .clickable { onNavigate() }
        )
    }
}


@Composable
fun TeamMemberAvatarList(team: Team) {
    val extraUserCount = team.users!!.size - TEAM_MEMBER_AVATAR_SHOW
    Text(
        text = "Team Member",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(top = 20.dp, bottom = 12.dp),
    )
    Box(
        modifier = Modifier.size(60.dp) // Kích thước tổng thể Box
    ) {
        if (team.users != null) {
            if (team.users!!.size > TEAM_MEMBER_AVATAR_SHOW) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .offset(x = (3 * 2 / 2.5f) * 40.dp)
                        .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primaryContainer, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+$extraUserCount",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            team.users!!.take(3).forEachIndexed { index, user: User ->
                UserAvatar(
                    painter = painterResource(R.drawable.avatar),
                    modifier = Modifier.offset(x = ((2-index) * 2 / 2.5f) * 40.dp),
                    size = 40
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamListItemPreview() {
    WorkTimeTrackerManagerTheme {
        TeamListItem(exampleTeam) { }
    }
}