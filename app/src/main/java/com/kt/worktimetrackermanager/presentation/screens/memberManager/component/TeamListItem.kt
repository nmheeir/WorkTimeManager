package com.kt.worktimetrackermanager.presentation.screens.memberManager.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.Helper
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.User

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
            Text(
                text = Helper.convertToCustomDateFormat2(team.createdAt),
                style = MaterialTheme.typography.labelSmall,
//                color = colorResource(R.color.blur_text_color)
            )

            Text(
                text = "Team Member",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 20.dp, bottom = 12.dp),
//                color = colorResource(R.color.blur_text_color)
            )
            Box(
                modifier = Modifier.size(60.dp) // Kích thước tổng thể Box
            ) {
                if (team.users != null) {
                    team.users!!.forEachIndexed { index, user: User ->
                        Image(
                            painter = painterResource(R.drawable.ic_person),
                            contentDescription = "Image 1",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .offset(x = (index * 2 / 3f) * 60.dp)
                                .size(60.dp)
                                .border(
                                    border = BorderStroke(4.dp, color = Color.White),
                                    RoundedCornerShape(50.dp)
                                )
                                .clip(RoundedCornerShape(50.dp))
                        )
                    }
                }
            }
        }

        Text(
            text = "More Detail",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                .padding(12.dp)
                .clickable { onNavigate() }
        )
    }
}