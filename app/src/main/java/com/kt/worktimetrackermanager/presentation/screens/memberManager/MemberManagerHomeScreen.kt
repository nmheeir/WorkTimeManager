package com.kt.worktimetrackermanager.presentation.screens.memberManager

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.data.remote.dto.enum.Role
import com.kt.worktimetrackermanager.data.remote.dto.response.UsersStatistic
import com.kt.worktimetrackermanager.presentation.navigations.Screens
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.MemberManagerHomeUIEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.MemberManagerHomeViewModel
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.MemberMangerUiState


@Composable
fun MemberManagerHomeScreen(
    navController: NavHostController,
    viewModel: MemberManagerHomeViewModel
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.channel) {
        when (it) {
            is MemberManagerHomeUIEvent.Failure -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }

            MemberManagerHomeUIEvent.Success -> {
            }
        }
    }

    MemberManagerHomePageLayout(
        state = uiState,
        onNavigateTo = { screen -> navController.navigate(screen.route) }
    )
}


@Composable
fun MemberManagerHomePageLayout(
    state: MemberMangerUiState,
    onNavigateTo: (Screens) -> Unit
) {


    //
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.member_manager_home_header),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Summary Cards
        MembersStatistic(state.memberTotal)

        Spacer(modifier = Modifier.height(24.dp))

        // Total Teams Card
        TeamStatistic(state.teamCount)

        // Navigation Buttons
        Text(
            text = stringResource(R.string.label_q_action),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        NavigationButton(
            text = stringResource(R.string.member_manager_home_action_add_member),
            icon = Icons.Default.Add,
            onClick = {onNavigateTo(Screens.AddMember)}
        )
        NavigationButton(
            text = stringResource(R.string.member_manager_home_action_create_team),
            icon = Icons.Default.Add,
            onClick =  {onNavigateTo(Screens.TeamCreate)}
        )
        NavigationButton(
            text = stringResource(R.string.member_manager_home_action_member_list),
            icon = Icons.Default.List,
            onClick = {onNavigateTo(Screens.MemberList())}
        )
        NavigationButton(
            text = stringResource(R.string.member_manager_home_action_team_list),
            icon = Icons.Default.List,
            onClick =  {onNavigateTo(Screens.TeamList)}
        )
    }
}

@Composable
fun MembersStatistic(
    membersStatistic: UsersStatistic
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SummaryCard(title = stringResource(R.string.member_manager_home_label_total_mem), count = membersStatistic.totalMember, Color(0xFF4CAF50))
        SummaryCard(title = Role.MANAGER.toString(), count = membersStatistic.managerCount, Color(0xFFFFA000))
        SummaryCard(title = Role.STAFF.toString(), count = membersStatistic.staffCount, Color(0xFF2196F3))
    }
}

@Composable
fun TeamStatistic(
    teamCount: Int
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.member_manager_home_label_total_team),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = teamCount.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun SummaryCard(title: String, count: Int, color: Color) {
    Box(
        modifier = Modifier
            .width(100.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(8.dp)) // Giả sử bạn muốn làm bo góc như `Card`
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Thêm viền giống `Card`
            .background(color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun NavigationButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(4.dp, brush = Brush.linearGradient(
            listOf(
                Color(0xFF1E90FF),
                Color(0xFF99BADD)
            )
        ))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(icon, contentDescription = null)
            Text(
                text = text,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMemberManagerHome() {
    val state = MemberMangerUiState(
        memberTotal = UsersStatistic(10, 10, 10),
        teamCount = 10
    )
    WorkTimeTrackerManagerTheme {
        MemberManagerHomePageLayout(
            state,
            onNavigateTo = {},
        )
    }

}