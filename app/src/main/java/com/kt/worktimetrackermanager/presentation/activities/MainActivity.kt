package com.kt.worktimetrackermanager.presentation.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kt.worktimetrackermanager.core.presentation.NavigationBarHeight
import com.kt.worktimetrackermanager.core.presentation.ui.AppTheme
import com.kt.worktimetrackermanager.core.presentation.utils.AppThemeKey
import com.kt.worktimetrackermanager.core.presentation.utils.rememberEnumPreference
import com.kt.worktimetrackermanager.presentation.navigations.Screens
import com.kt.worktimetrackermanager.presentation.navigations.navigationBuilder
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import com.kt.worktimetrackermanager.presentation.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.startDestination.value == null
            }
        }

        setContent {
            val appTheme by rememberEnumPreference(AppThemeKey, AppTheme.MONET)

            WorkTimeTrackerManagerTheme(
                appTheme = appTheme
            ) {
                Box {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()

                    val navigationItems = remember { Screens.MainScreens }
                    val topLevelScreens = listOf(
                        Screens.Home.route,
                        Screens.Dashboard.route,
                        Screens.Attendant.route,
                        Screens.Group.route
                    )

                    val shouldShowNavigationBar = remember(backStackEntry) {
                        navigationItems.fastAny { it.route == backStackEntry?.destination?.route }
                    }

                    val windowInsets = WindowInsets.systemBars
                    val density = LocalDensity.current
                    val bottomInset = with(density) { windowInsets.getBottom(density).toDp() }

                    val navigationBarHeight by animateDpAsState(
                        targetValue = if (shouldShowNavigationBar) NavigationBarHeight else 0.dp,
                        label = "",
                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                    )

                    CompositionLocalProvider(
                        LocalMainViewModel provides viewModel
                    ) {
                        Scaffold(
                            bottomBar = {
                                NavigationBar(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .offset {
                                            if (navigationBarHeight == 0.dp) {
                                                IntOffset(
                                                    x = 0,
                                                    y = (bottomInset + NavigationBarHeight).roundToPx()
                                                )
                                            } else {
                                                val hideOffset =
                                                    (bottomInset + NavigationBarHeight) * (1 - navigationBarHeight / NavigationBarHeight)
                                                IntOffset(x = 0, y = hideOffset.roundToPx())
                                            }
                                        }
                                ) {
                                    navigationItems.fastForEach { screens ->
                                        key(screens.route) {
                                            NavigationBarItem(
                                                selected = backStackEntry?.destination?.hierarchy?.any { it.route == screens.route } == true,
                                                icon = {
                                                    Icon(
                                                        painter = painterResource(screens.iconId),
                                                        contentDescription = null
                                                    )
                                                },
                                                label = {
                                                    Text(
                                                        text = stringResource(screens.titleId),
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                },
                                                onClick = {
                                                    if (backStackEntry?.destination?.hierarchy?.any { it.route == screens.route } == true) {
                                                        backStackEntry?.savedStateHandle?.set(
                                                            "scrollToTop",
                                                            true
                                                        )
                                                    } else {
                                                        navController.navigate(screens.route) {
                                                            popUpTo(navController.graph.startDestinationId) {
                                                                saveState = true
                                                            }
                                                            launchSingleTop = true
                                                            restoreState = true
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        ) { paddingValues ->
                            NavHost(
                                navController = navController,
                                startDestination = viewModel.startDestination.value!!,
                                enterTransition = {
                                    if (initialState.destination.route in topLevelScreens
                                        && targetState.destination.route in topLevelScreens
                                    ) {
                                        fadeIn(tween(250))
                                    } else {
                                        fadeIn(tween(250)) + slideInHorizontally { it / 2 }
                                    }
                                },
                                exitTransition = {
                                    if (initialState.destination.route in topLevelScreens
                                        && targetState.destination.route in topLevelScreens
                                    ) {
                                        fadeOut(tween(200))
                                    } else {
                                        fadeOut(tween(200)) + slideOutHorizontally { -it / 2 }
                                    }
                                },
                                popEnterTransition = {
                                    if (initialState.destination.route in topLevelScreens
                                        && targetState.destination.route in topLevelScreens
                                    ) {
                                        fadeIn(tween(250))
                                    } else {
                                        fadeIn(tween(250)) + slideInHorizontally { -it / 2 }
                                    }
                                },
                                popExitTransition = {
                                    if (initialState.destination.route in topLevelScreens
                                        && targetState.destination.route in topLevelScreens
                                    ) {
                                        fadeOut(tween(200))
                                    } else {
                                        fadeOut(tween(200)) + slideOutHorizontally { it / 2 }
                                    }
                                },
                                modifier = Modifier
                                    .padding(
                                        if (shouldShowNavigationBar) paddingValues
                                        else PaddingValues(0.dp)
                                    )
                            ) {
                                navigationBuilder(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

val LocalMainViewModel = compositionLocalOf<MainViewModel> { error("No MainViewModel provided") }