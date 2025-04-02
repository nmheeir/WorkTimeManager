package com.kt.worktimetrackermanager.presentation.screens.auth

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import kotlinx.coroutines.delay

@Composable
fun ResetPasswordSuccessScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AnimatedCheckIcon()

        Text(
            text = stringResource(R.string.reset_pw_success),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))



        Button(
            onClick = { navController.navigate("login") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(R.string.reset_pw_back_to_login),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


@Composable
fun AnimatedCheckIcon() {
    var isAnimating by remember { mutableStateOf(false) }

    // Điều khiển tỷ lệ phóng to
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1.1f else 1f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "Scale Animation"
    )

    // Điều khiển góc quay
    val rotationY by animateFloatAsState(
        targetValue = if (isAnimating) 720f else 0f, // Xoay 2 vòng
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "Rotation Y Animation"
    )

    LaunchedEffect(Unit) {
        while (true) {
            isAnimating = !isAnimating
            delay(5000) // Chờ một lúc trước khi lặp lại animation
        }
    }

    Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Animated 3D Rotate Y Icon",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .size(200.dp) // Kích thước ban đầu
            .scale(scale) // Phóng to
            .graphicsLayer(
                rotationY = rotationY, // Xoay theo chiều ngang (trục Y)
                cameraDistance = 12f * 8 // Điều chỉnh khoảng cách camera để hiệu ứng 3D rõ hơn
            )
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewResetPasswordSuccessScreen() {
    WorkTimeTrackerManagerTheme {
        ResetPasswordSuccessScreen(navController = rememberNavController())
    }
}
