package com.kt.worktimetrackermanager.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.utils.Gap
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.presentation.navigations.Screens
import com.kt.worktimetrackermanager.presentation.viewmodels.LoginUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.LoginUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    ObserveAsEvents(viewModel.channel) {
        when (it) {
            is LoginUiEvent.LoginFailure -> {
                Toast.makeText(context, it.msg, Toast.LENGTH_LONG).show()
            }

            LoginUiEvent.LoginSuccess -> {
                navController.navigate(Screens.Home.route) {
                    popUpTo("login") {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }

    }

    CompositionLocalProvider(
        LocalViewModel provides viewModel
    ) {
        LoginLayout()
    }
}

@Composable
private fun LoginLayout(
    modifier: Modifier = Modifier
) {
    val viewModel = LocalViewModel.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Gap(height = MaterialTheme.padding.medium)

        val (username, onUsernameChange) = remember { mutableStateOf("") }
        OutlinedTextField(
            value = username,
            onValueChange = {
                onUsernameChange(it)
            }
        )

        val (password, onPasswordChange) = remember { mutableStateOf("") }
        OutlinedTextField(
            value = password,
            onValueChange = {
                onPasswordChange(it)
            }
        )

        Button(
            onClick = {
                viewModel.onAction(LoginUiAction.Login(username, password))
            }
        ) {
            Text(
                text = "Login"
            )
        }

    }
}


private val LocalViewModel = compositionLocalOf<LoginViewModel> { error("No Viewmodel provided") }