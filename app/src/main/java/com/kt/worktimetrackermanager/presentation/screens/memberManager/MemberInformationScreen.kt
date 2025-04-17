package com.kt.worktimetrackermanager.presentation.screens.memberManager

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.navigations.Screens
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.MemberInforUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.MemberInforUiState
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.MemberInforViewModel
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.Helper
import com.kt.worktimetrackermanager.data.remote.dto.enums.EmployeeType
import com.kt.worktimetrackermanager.data.remote.dto.enums.Role
import com.kt.worktimetrackermanager.presentation.components.customButton.RoundedButton
import com.kt.worktimetrackermanager.presentation.components.customButton.GlowingButton
import com.kt.worktimetrackermanager.presentation.components.image.UserAvatar
import com.kt.worktimetrackermanager.presentation.components.scaffold.MyScaffold
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme


@Composable
fun MemberInforScreen(
    viewModel: MemberInforViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.channel) {
        when (it) {
            is MemberInforUiEvent.Failure -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }

            is MemberInforUiEvent.Success -> {
            }
        }
    }
    MyScaffold(
        onBack = { navController.popBackStack() },
        title = stringResource(R.string.member_infor),
    ) { paddingValues ->
        MemberInforLayout(
            state = uiState,
            onNavigateTo = { screens ->
                navController.navigate(screens.route)
            },
            paddingValues = paddingValues
        )
    }
}

@Composable
fun MemberInforLayout (
    state: MemberInforUiState,
    onNavigateTo: (Screens) -> Unit,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        // Header
        HeaderInfor(
            user = state.user,
            onNavigateTo = onNavigateTo
        )

        // Infor
        Information(
            user = state.user
        )

        // Button nav
        MemberActivityOption()
    }
}

@Composable
fun HeaderInfor(
    user: User,
    onNavigateTo: (Screens) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (title, description) = createRefs()
        Box(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 50.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    user.userFullName,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(vertical = 2.dp),
                )
                Text(
                    text = "@ ${user.userName}",
                )
                Row(
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    EmployeeType.fromIntToName(user.employeeType.ordinal)?.let {
                        Text(it, color = Color.Gray)
                    }
                    user.companyTeam?.let {
                        Text(text = it.name, modifier = Modifier.padding(10.dp, 0.dp), color = Color.Gray)
                    }
                    Text(text = user.designation, color = Color.Gray)
                }
                GlowingButton(
                    onClick = { onNavigateTo(Screens.UpdateMember(userId = user.id)) },
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "Favorite Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = stringResource(R.string.edit_profile), style = MaterialTheme.typography.titleMedium, color = Color.White)
                    }
                }
            }
        }

        // Image
        Box(
            modifier = Modifier
                .constrainAs(description) {
                    top.linkTo(title.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            UserAvatar(
                size = 100,
                painter = painterResource(R.drawable.avatar),
            )
        }
    }
}

@Composable
fun Information(user: User) {

    val personalInformation: List<Pair<String, String>> = listOf(
        stringResource(R.string.full_name) to user.userFullName,
        stringResource(R.string.email) to user.email,
        stringResource(R.string.phone_number) to user.phoneNumber,
        stringResource(R.string.address) to user.address
    )

    val employmentDetail: List<Pair<String, String>> = listOf(
        stringResource(R.string.designation) to user.designation,
        stringResource(R.string.team) to (user.companyTeam?.name ?: "None"),
        stringResource(R.string.start_date) to Helper.convertToCustomDateFormat2(user.createdAt),
        stringResource(R.string.department) to user.department,
        stringResource(R.string.EmpType) to (EmployeeType.fromIntToName(user.employeeType.ordinal)?:"None"),
        stringResource(R.string.role) to (Role.fromIntToName(user.role.ordinal)?:"None"),
    )


    InformationLayout {
        Text(
            text = stringResource(R.string.personal_infor),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        personalInformation.forEach { pair ->
            InformationItem(
                fieldName = pair.first,
                value = pair.second,
            )
        }
    }

    InformationLayout {
        Text(
            text = stringResource(R.string.work_infor),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        employmentDetail.forEach { pair ->
            InformationItem(
                fieldName = pair.first,
                value = pair.second,
            )
        }
    }



}

@Composable
fun InformationLayout(
    content: @Composable () -> Unit,
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(vertical = 10.dp, horizontal = 12.dp)
    ) {
        content()
    }
}


@Composable
fun InformationItem(
    value: String,
    fieldName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "$fieldName: ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.alignByBaseline() // Căn theo baseline
        )

        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.alignByBaseline() // Căn theo baseline
        )
    }
}


@Composable
fun MemberActivityOption() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary), RoundedCornerShape(16.dp))
            .padding(vertical = 10.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedButton(
            size = 60,
            onClick = {},
            backgroundColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle, // Icon cần hiển thị
                contentDescription = "Icon with background",
                tint = Color.Blue, // Màu của icon
            )
        }
        Text("Activity",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 10.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Icon with background",
            tint = Color.Gray,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMemberInformation() {
    WorkTimeTrackerManagerTheme {
        MemberInforLayout(
            state = MemberInforUiState(),
            onNavigateTo = {}
        )
    }
}