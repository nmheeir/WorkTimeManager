package com.kt.worktimetrackermanager.presentation.screens.memberManager

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.PopupProperties
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.components.customButton.GlowingButton
import com.kt.worktimetrackermanager.presentation.screens.memberManager.component.MemberListItem
import com.kt.worktimetrackermanager.presentation.screens.memberManager.component.MemberListItem2
import com.kt.worktimetrackermanager.presentation.screens.memberManager.component.MyOutlineTextField
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamCreateUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamCreateUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamCreateUiState
import com.kt.worktimetrackermanager.presentation.viewmodels.memberManager.TeamCreateViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.File


@Composable
fun TeamCreateScreen(
    viewModel: TeamCreateViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ObserveAsEvents(viewModel.channel) {
        when (it) {
            is TeamCreateUiEvent.Failure -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }

            is TeamCreateUiEvent.Success -> {
            }
        }
    }

    TeamCreateLayout(
        state = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
fun TeamCreateLayout(
    state: TeamCreateUiState,
    onAction: (TeamCreateUiAction) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Text(
                text = stringResource(R.string.team_create),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight(550)
            )

            Text(
                text = stringResource(R.string.team_create_des),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            MyOutlineTextField(
                value = state.teamName,
                onValueChange = {
                    onAction(TeamCreateUiAction.OnFieldChange("teamname", it))
                },
                text = stringResource(R.string.team_name),
                icon = Icons.Default.Person,
            )
            TeamCreateLocationField(
                stateLatitude = state.teamLatitude,
                stateLongitude = state.teamLongitude,
                onAction = onAction
            )

            TeamCreateMemberPicker(state.memList, state.userName, state.teamManager, onAction)

            GlowingButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                onClick = {
                    onAction(TeamCreateUiAction.OnCreateTeam)
                }
            ) {
                Text(
                    text = stringResource(R.string.team_create),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }

}
@Composable
fun TeamCreateMemberPicker(memberList: List<User>, searchValue: String, chosenMember: User?, onAction: (TeamCreateUiAction) -> Unit) {
    var expanded by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        MyOutlineTextField(
            value = searchValue,
            onValueChange = {
                expanded = true
                onAction(TeamCreateUiAction.OnFieldChange("username", it))
                onAction(TeamCreateUiAction.OnGetUsers)
            },  // Set expanded to true when typing
            text = stringResource(R.string.choose_manager),
            icon = Icons.Default.Person,
        )
        // Gợi ý nằm đè lên
        DropdownMenu(
            expanded = expanded && searchValue.isNotEmpty(),
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.primary).fillMaxWidth(fraction = 0.9f),
            properties = PopupProperties(focusable = false)
        ) {
            memberList.forEach { suggestion ->
                DropdownMenuItem(
                    text = {
                        MemberListItem2(suggestion, {}, isClickable = false)
                    },
                    onClick = {
                        expanded = false
                        onAction(TeamCreateUiAction.OnFieldChange("username", ""))
                        onAction(TeamCreateUiAction.OnChooseManager(suggestion))
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    Text(text = stringResource(R.string.team_manager), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
    if (chosenMember != null) {
        MemberListItem(
            chosenMember,
            onNavigate = {}
        )
    }
}


@Composable
fun TeamCreateLocationField(
    stateLongitude: Double?,
    stateLatitude: Double?,
    onAction: (TeamCreateUiAction) -> Unit
)
{
    var displayLatitude by remember { mutableStateOf<Double?>(null) }
    var displayLongitude by remember { mutableStateOf<Double?>(null) }

    Row  {
        Spacer(modifier = Modifier.height(10.dp))
        MyOutlineTextField(
            modifier = Modifier
                .weight(1f)
                .padding(end = 10.dp),
            value = stateLatitude?.toString() ?: "",
            onValueChange = {
                onAction(TeamCreateUiAction.OnFieldChange("latitude", it.toDouble()))
            },
            text = stringResource(R.string.latitude),
            icon = Icons.Default.LocationOn,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        MyOutlineTextField(
            modifier = Modifier.weight(1f),
            value = stateLongitude?.toString() ?:"",
            onValueChange = {
                onAction(TeamCreateUiAction.OnFieldChange("longitude", it.toDouble()))
            },
            text = stringResource(R.string.longitude),
            icon = Icons.Default.LocationOn,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }

    GlowingButton (
        onClick = {
            displayLatitude = stateLatitude
            displayLongitude = stateLongitude
        },
        modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.update_location),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.height(20.dp))
    SelectableMap(
        setLongitude = displayLongitude,
        setLatitude = displayLatitude,
        onLocationSelected = { lat, long ->
            onAction(TeamCreateUiAction.OnFieldChange("longitude", long))
            onAction(TeamCreateUiAction.OnFieldChange("latitude", lat))
        }
    )
}




@SuppressLint("SuspiciousIndentation", "ClickableViewAccessibility")
@Composable
fun SelectableMap(
    onLocationSelected: (latitude: Double, longitude: Double) -> Unit,
    setLongitude: Double? = null,
    setLatitude: Double? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var mapViewState by remember { mutableStateOf<MapView?>(null) }
    var mapInitialized by remember { mutableStateOf(false) }  // Flag to check if map is initialized

    // Cấu hình osmdroid
    LaunchedEffect(Unit) {
        val osmConfig = Configuration.getInstance()
        osmConfig.userAgentValue = context.packageName
        osmConfig.osmdroidBasePath = File(context.cacheDir, "osmdroid")
        osmConfig.osmdroidTileCache = File(osmConfig.osmdroidBasePath, "tile")
    }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val boxHeight = screenHeight * 0.3f

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Yêu cầu cấp quyền truy cập vị trí
            ActivityCompat.requestPermissions(
                context as ComponentActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return@LaunchedEffect
        }

        // Lấy vị trí hiện tại của thiết bị
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            currentLocation = location
        }
    }

    // Thực hiện update map khi setLatitude hoặc setLongitude thay đổi
    LaunchedEffect(setLatitude, setLongitude) {
        mapViewState?.let { mapView ->
            val mapController = mapView.controller
            val geoPoint = if (setLatitude != null && setLongitude != null) {
                GeoPoint(setLatitude, setLongitude)
            } else {
                currentLocation?.let { GeoPoint(it.latitude, it.longitude) }
            }

            // Nếu tọa độ hợp lệ thì cập nhật marker và setCenter
            geoPoint?.let {
                val marker = Marker(mapView).apply {
                    position = geoPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Selected Location"
                }

                mapView.overlays.clear()
                mapView.overlays.add(marker)
                mapController.setCenter(geoPoint)
                mapView.invalidate() // Vẽ lại bản đồ
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .height(boxHeight)
            .clip(RoundedCornerShape(8.dp))
    ) {
        currentLocation?.let {
            AndroidView(
                factory = { context ->
                    val mapView = MapView(context).apply {
                        val mapController = this.controller
                        mapController.setZoom(18.0)
                        this.setMultiTouchControls(true)

                        // Set vị trí trung tâm ban đầu
                        val startPoint = currentLocation?.let { GeoPoint(it.latitude, it.longitude) }
                        mapController.setCenter(startPoint)

                        mapInitialized = true
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

                        // Thêm sự kiện click để chọn vị trí
                        mapView.setOnTouchListener { v, event ->
                            if (event.action == MotionEvent.ACTION_UP) {
                                val proj = mapView.projection
                                val geoPoint =
                                    proj.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint

                                // Thêm marker tại vị trí nhấp chuột
                                val marker = Marker(mapView).apply {
                                    position = geoPoint
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    title = "Selected Location"
                                }

                                // Xóa các marker trước và thêm marker mới
                                mapView.overlays.clear()
                                mapView.overlays.add(marker)
                                mapView.invalidate() // Vẽ lại bản đồ

                                // Gọi callback với vị trí đã chọn
                                onLocationSelected(geoPoint.latitude, geoPoint.longitude)
                            }
                            false
                        }
                    }
                    mapViewState = mapView
                    mapView
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamCreateLayoutPreview() {
    WorkTimeTrackerManagerTheme {
        TeamCreateLayout(TeamCreateUiState(), {})
    }
}