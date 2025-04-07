package com.kt.worktimetrackermanager.presentation.screens.memberManager

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.Helper
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.data.remote.dto.response.Team
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.exampleUser1
import com.kt.worktimetrackermanager.presentation.navigations.Screens
import com.kt.worktimetrackermanager.presentation.screens.memberManager.component.MemberListItem
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamInformationUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamInformationUiState
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamInformationViewModel
import kotlinx.coroutines.flow.Flow
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File


@Composable
fun TeamInformationScreen(
    viewModel: TeamInformationViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.channel) {
        when (it) {
            is TeamInformationUiEvent.Failure -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
            is TeamInformationUiEvent.Success -> {
            }
        }
    }

        TeamInformationLayout(
            state = uiState,
            onNavigateTo = { screen ->
                navController.navigate(screen.route)
            },
        )

}


@Composable
fun TeamInformationLayout(
    state: TeamInformationUiState,
    onNavigateTo: (Screens) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        //
        Header(state.team)
        Column (
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 40.dp)
        ) {
            TeamManagerInformation(exampleUser1, onNavigateTo)
            TeamMemberList(state.team, onNavigateTo)

            Text(
                text = stringResource(R.string.team_infor_location),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 20.dp, start = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )

            TeamMap(state.team.latitude, state.team.longitude)
        }
    }
}

@Composable
fun Header(team: Team) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val (title, description) = createRefs()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 0.dp, topStart = 0.dp, bottomEnd = 20.dp, bottomStart = 20.dp))
                .padding(20.dp)
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
        ) {
            Column {
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${stringResource(R.string.team_infor_created_at)}: " + Helper.convertToCustomDateFormat2(team.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.padding(vertical = 12.dp),
                )

                Text(
                    text = team.description,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun TeamManagerInformation(
    user: User,
    onNavigateTo: (Screens) -> Unit
) {
    val displayInfor: List<Pair<String, String>> = listOf(
        stringResource(R.string.email) to user.email,
        stringResource(R.string.designation) to user.designation,
    )

    Text(
        text = stringResource(R.string.team_infor_manager_infor),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 20.dp, start = 8.dp),
        color = MaterialTheme.colorScheme.primary
    )

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = 16.dp, horizontal = 12.dp)
            .clickable { onNavigateTo(Screens.MemberInfor(user.id)) }
    ) {
        // Manager information
        Row {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = "App Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .border(border = BorderStroke(4.dp, color = Color.White), RoundedCornerShape(50.dp))
                    .clip(RoundedCornerShape(50.dp))
            )
            Column (
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 12.dp)
            ) {
                Text(text = user.userFullName, style = MaterialTheme.typography.titleMedium)
                Text(text = "@" + user.userName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
        }

        // Content information
        InformationLayout {
            displayInfor.forEach { pair ->
                InformationItem(
                    fieldName = pair.first,
                    value = pair.second,
                )
            }
        }

        // Navigate to member inforation, for manager
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Person, // Biểu tượng yêu thích
                contentDescription = "Favorite Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = stringResource(R.string.profile))
        }
    }
}

@Composable
fun TeamMemberList(
    team: Team,
    onNavigateTo: (Screens) -> Unit
) {
    Text(
        text = stringResource(R.string.team_infor_team_members),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 20.dp, start = 8.dp),
        color = MaterialTheme.colorScheme.primary
    )

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.3f))
            .clip(RoundedCornerShape(16.dp))
            .padding(8.dp)
    ) {

        team.users?.forEach { user ->
            MemberListItem(user) { onNavigateTo(Screens.MemberInfor(user.id)) }
        }

        Button(
            onClick = { onNavigateTo(Screens.MemberList(teamId = team.id, null, null)) },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.List, // Biểu tượng yêu thích
                contentDescription = "Favorite Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = stringResource(R.string.team_infor_full_members))
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun TeamMap(
    latitude: Double,
    longitude: Double
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Cấu hình osmdroid
    LaunchedEffect(Unit) {
        val osmConfig = Configuration.getInstance()
        osmConfig.userAgentValue = context.packageName
        osmConfig.osmdroidBasePath = File(context.cacheDir, "osmdroid")
        osmConfig.osmdroidTileCache = File(osmConfig.osmdroidBasePath, "tile")
    }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val boxHeight = screenHeight * 0.3f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .height(boxHeight)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AndroidView(
            factory = { context ->
                val mapView = MapView(context).apply {
                    val mapController = this.controller
                    mapController.setZoom(18.0)
                    this.setMultiTouchControls(true)

                    // Set marker tại vị trí được truyền vào
                    val startPoint = GeoPoint(latitude, longitude)
                    mapController.setCenter(startPoint)

                    val marker = Marker(this)
                    marker.position = startPoint
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    marker.title = "Team Location"
                    this.overlays.add(marker)
                }

                // Quản lý vòng đời MapView
                mapView.apply {
                    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onResume(owner: LifecycleOwner) {
                            mapView.onResume()  // Handle map resume
                        }

                        override fun onPause(owner: LifecycleOwner) {
                            mapView.onPause()  // Handle map pause
                        }
                    })
                }
                mapView
            }
        )
    }
}



@Preview(showBackground = true)
@Composable
fun TeamInformationLayoutScreen() {
    WorkTimeTrackerManagerTheme {
        TeamInformationLayout(state = TeamInformationUiState(), {})
    }
}

