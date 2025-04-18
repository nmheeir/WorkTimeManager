@file:OptIn(
    ExperimentalSharedTransitionApi::class
)

package com.kt.worktimetrackermanager.presentation.screens

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.animation.ProfileSharedElementKey
import com.kt.worktimetrackermanager.core.presentation.animation.ProfileSharedElementType
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.activities.LocalSharedTransitionScope
import com.kt.worktimetrackermanager.presentation.components.dialog.AlertDialog
import com.kt.worktimetrackermanager.presentation.components.image.CoilImage
import com.kt.worktimetrackermanager.presentation.components.preference.PreferenceEntry
import com.kt.worktimetrackermanager.presentation.components.preference.PreferenceGroupTitle
import com.kt.worktimetrackermanager.presentation.navigations.Screens
import com.kt.worktimetrackermanager.presentation.viewmodels.ProfileUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.ProfileViewModel
import kotlin.math.max
import kotlin.math.min

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isLogout by viewModel.isLogout.collectAsStateWithLifecycle()

    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current

    val roundedCornerAnim by animatedVisibilityScope.transition
        .animateDp(label = "rounded corner") { enterExit: EnterExitState ->
            when (enterExit) {
                EnterExitState.PreEnter -> 20.dp
                EnterExitState.Visible -> 0.dp
                EnterExitState.PostExit -> 20.dp
            }
        }


    ObserveAsEvents(viewModel.channel) {
        when (it) {
            ProfileUiEvent.LogoutSuccess -> {
                navController.navigate(Screens.Login.route) {
                    popUpTo("profile") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        user.takeIf { it != null }?.let { user ->
            with(sharedTransitionScope) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(roundedCornerAnim))
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(
                                    key = ProfileSharedElementKey(
                                        id = user.id,
                                        type = ProfileSharedElementType.Bounds
                                    )
                                ),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        val scrollState = rememberScrollState(0)
                        Header(user.id)
                        Avatar(
                            userId = user.id,
                            avatarUrl = user.avatarUrl ?: "",
                            scrollProvider = { scrollState.value }
                        )
                        Body(
                            user = user,
                            onNavigate = {
                                navController.navigate(it.route)
                            },
                            onLogout = {
                                viewModel.logout()
                            },
                            scroll = scrollState
                        )
                    }
                    if (isLogout) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.3f))
                        )

                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    userId: Int,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current

    with(sharedTransitionScope) {
        val brushColors = listOf<Color>(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.tertiaryContainer
        )

        val infiniteTransition = rememberInfiniteTransition(label = "background")
        val targetOffset = with(LocalDensity.current) {
            1000.dp.toPx()
        }

        val offset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = targetOffset,
            animationSpec = infiniteRepeatable(
                tween(50000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "offset"
        )

        Spacer(
            modifier = Modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = ProfileSharedElementKey(
                            id = userId,
                            type = ProfileSharedElementType.Background
                        )
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                )
                .height(280.dp)
                .fillMaxWidth()
                .blur(40.dp)
                .drawWithCache {
                    val brushSize = 400f
                    val brush = Brush.linearGradient(
                        colors = brushColors,
                        start = Offset(offset, offset),
                        end = Offset(offset + brushSize, offset + brushSize),
                        tileMode = TileMode.Mirror
                    )
                    onDrawBehind {
                        drawRect(brush)
                    }
                }
        )
    }
}

@Composable
private fun Body(
    user: User,
    onNavigate: (Screens) -> Unit,
    onLogout: () -> Unit,
    scroll: ScrollState,
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current

    var showLogoutDialog by remember { mutableStateOf(false) }

    with(sharedTransitionScope) {
        Column(
            modifier = Modifier.skipToLookaheadSize()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(MinTitleOffset)
            )

            Column(
                modifier = Modifier.verticalScroll(scroll)
            ) {
                Spacer(Modifier.height(GradientScroll))
                Spacer(Modifier.height(ImageOverlap))
                PreferenceGroupTitle(
                    title = "Contact"
                )
                PreferenceEntry(
                    title = { Text(user.email) },
                    icon = { Icon(painterResource(R.drawable.ic_mail), null) }
                )
                PreferenceEntry(
                    title = { Text(user.address) },
                    icon = { Icon(painterResource(R.drawable.ic_distance), null) }
                )
                PreferenceGroupTitle(
                    title = "Account"
                )
                PreferenceEntry(
                    title = { Text("Personal Data") },
                    icon = { Icon(painterResource(R.drawable.ic_person), null) }
                )
                PreferenceEntry(
                    title = { Text("Office Assets") },
                    icon = { Icon(painterResource(R.drawable.ic_folder_fill), null) },
                    trailingContent = {
                        Icon(
                            painterResource(R.drawable.ic_keyboard_arrow_right),
                            null
                        )
                    },
                    onClick = { onNavigate(Screens.OfficeAssets) }
                )
                PreferenceGroupTitle(title = "Settings")
                PreferenceEntry(
                    title = { Text("Change password") },
                    icon = { Icon(painterResource(R.drawable.ic_settings_fill), null) },
                    trailingContent = {
                        Icon(
                            painterResource(R.drawable.ic_keyboard_arrow_right),
                            null
                        )
                    }
                )
                PreferenceEntry(
                    title = { Text("Version") },
                    icon = { Icon(painterResource(R.drawable.ic_code_blocks), null) },
                    trailingContent = {
                        Icon(
                            painterResource(R.drawable.ic_keyboard_arrow_right),
                            null
                        )
                    }
                )
                PreferenceEntry(
                    title = { Text("Logout") },
                    icon = {
                        Icon(
                            painterResource(R.drawable.ic_logout),
                            null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    onClick = {
                        showLogoutDialog = true
                    }
                )
                if (showLogoutDialog) {
                    AlertDialog(
                        title = "Log out ?",
                        text = "Do you want to log out ?",
                        onDismiss = { showLogoutDialog = false },
                        onConfirm = {
                            onLogout()
                            showLogoutDialog = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Avatar(
    userId: Int,
    avatarUrl: String,
    scrollProvider: () -> Int,
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }

    CollapsingImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = Modifier.hozPadding()
    ) {
        val sharedTransitionScope = LocalSharedTransitionScope.current
        val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current

        with(sharedTransitionScope) {
            CoilImage(
                imageUrl = avatarUrl,
                modifier = Modifier
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            key = ProfileSharedElementKey(
                                id = userId,
                                type = ProfileSharedElementType.Image
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        exit = fadeOut(),
                        enter = fadeIn(),
                    )
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun CollapsingImageLayout(
    modifier: Modifier = Modifier,
    collapseFractionProvider: () -> Float,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction)
            .roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 256.dp
private val CollapsedImageSize = 64.dp
