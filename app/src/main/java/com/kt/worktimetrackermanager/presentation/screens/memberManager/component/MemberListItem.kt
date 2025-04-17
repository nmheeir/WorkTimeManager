package com.kt.worktimetrackermanager.presentation.screens.memberManager.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.Helper
import com.kt.worktimetrackermanager.data.remote.dto.enums.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.components.customButton.RoundedButton
import com.kt.worktimetrackermanager.presentation.sampleUser
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme


@Composable
fun MemberListItem(user: User, onNavigate:() -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.clickable { onNavigate() }
        ) {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = "App Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .border(
                        border = BorderStroke(
                            4.dp,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ), RoundedCornerShape(50.dp)
                    )
                    .clip(RoundedCornerShape(50.dp))
            )
            Column (
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = user.userFullName,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = "(" + user.companyTeam?.name + ")",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                }
                Text(
                    text = "@" + user.userName,
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            RoundedButton(
                size = 40,
                onClick = {},
                backgroundColor = Color.Transparent
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert, // Biểu tượng yêu thích
                    contentDescription = "Favorite Icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Text(
            text = Helper.convertToCustomDateFormat2(user.createdAt),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.End)
        )

        Box(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color = MaterialTheme.colorScheme.primary
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = EmployeeType.fromIntToName(user.employeeType.ordinal).toString(),
                fontWeight = FontWeight(600),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .padding(end = 10.dp)
        )
    }
}

@Composable
fun MemberListItem2(user: User, onNavigate:() -> Unit, isClickable: Boolean = true) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.then(
                if (isClickable) Modifier.clickable { onNavigate() }
                else Modifier
            )
        ) {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = "App Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .border(
                        border = BorderStroke(
                            4.dp,
                            color = MaterialTheme.colorScheme.outline
                        ), RoundedCornerShape(50.dp)
                    )
                    .clip(RoundedCornerShape(50.dp))
            )
            Column (
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = user.userFullName,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = "(" + user.companyTeam?.name + ")",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 4.dp),
                    )
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                color = MaterialTheme.colorScheme.primary
                            )
                            .padding(horizontal = 2.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = EmployeeType.fromIntToName(user.employeeType.ordinal).toString(),
                            fontWeight = FontWeight(600),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Text(
                    text = "@" + user.userName,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemberListItemPreview() {
    WorkTimeTrackerManagerTheme {
        MemberListItem2(sampleUser, {})
    }
}