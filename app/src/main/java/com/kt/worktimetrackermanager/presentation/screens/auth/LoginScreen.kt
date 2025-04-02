package com.kt.worktimetrackermanager.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.presentation.components.customButton.RoundedButton
import com.kt.worktimetrackermanager.presentation.navigations.Screens
import com.kt.worktimetrackermanager.presentation.ui.theme.WorkTimeTrackerManagerTheme
import com.kt.worktimetrackermanager.presentation.viewmodels.LoginUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.LoginUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.LoginUiState
import com.kt.worktimetrackermanager.presentation.viewmodels.LoginViewModel
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import kotlinx.coroutines.flow.Flow

// --------------------
// LoginScreen: Chịu trách nhiệm xử lý logic, quan sát sự kiện và định tuyến
// --------------------
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Tách riêng phần quan sát sự kiện login
    ObserveLoginEvents(
        eventChannel = viewModel.channel,
        onNavigateToHome = {
            navController.navigate(Screens.Home.route) {
                popUpTo("login") { inclusive = true }
                launchSingleTop = true
            }
        },
        onError = { errorMsg -> viewModel.onAction(LoginUiAction.UpdateError(errorMsg)) },
        onFailure = { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    )

    // Giao diện chính của Login
    LoginContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        onNavigateTo = { route -> navController.navigate(route) }
    )
}

// --------------------
// ObserveLoginEvents: Chỉ chịu trách nhiệm quan sát và phản ứng với các sự kiện UI
// --------------------
@Composable
private fun ObserveLoginEvents(
    eventChannel: Flow<LoginUiEvent>, // Sử dụng kiểu chính xác của channel nếu có
    onNavigateToHome: () -> Unit,
    onError: (String) -> Unit,
    onFailure: (String) -> Unit
) {
    val userNotFoundMsg = stringResource(id = R.string.error_user_not_found)
    val wrongPasswordMsg = stringResource(id = R.string.error_wrong_password)
    ObserveAsEvents(eventChannel) { event ->
        when (event) {
            LoginUiEvent.Success -> onNavigateToHome()
            is LoginUiEvent.UserNotFound -> onError(userNotFoundMsg)
            is LoginUiEvent.WrongPassword -> onError(wrongPasswordMsg)
            is LoginUiEvent.Failure -> onFailure(event.msg)
        }
    }
}

// --------------------
// LoginContent: Chịu trách nhiệm hiển thị bố cục tổng thể của màn hình Login
// --------------------
@Composable
fun LoginContent(
    uiState: LoginUiState,
    onAction: (LoginUiAction) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(40.dp))
        LoginField(
            uiState = uiState,
            onEvent = onAction,
            onNavigateTo = onNavigateTo
        )
    }
}

// --------------------
// HeaderSection: Chỉ hiển thị phần tiêu đề
// --------------------
@Composable
private fun HeaderSection() {
    Column(
        modifier = Modifier
            .padding(top = 40.dp, start = 40.dp, end = 40.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.login_welcome),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(R.string.login_welcome_jp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

// --------------------
// LoginField: Hiển thị các ô nhập liệu và các nút hành động liên quan đến đăng nhập
// --------------------
@Composable
fun LoginField(
    uiState: LoginUiState,
    onEvent: (LoginUiAction) -> Unit,
    onNavigateTo: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // Ô nhập username
            OutlinedTextField(
                label = { Text(stringResource(R.string.label_username), color = MaterialTheme.colorScheme.primary) },
                value = uiState.username,
                onValueChange = { onEvent(LoginUiAction.OnUsernameChange(it)) },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(40.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Ô nhập password
            OutlinedTextField(
                label = { Text(stringResource(R.string.label_password), color = MaterialTheme.colorScheme.primary) },
                value = uiState.password,
                onValueChange = { onEvent(LoginUiAction.OnPasswordChange(it)) },
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                ),
                shape = RoundedCornerShape(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            RememberAndForgotRow(uiState = uiState, onEvent = onEvent, onNavigateTo = onNavigateTo)
            if (uiState.isError) {
                Text(
                    text = uiState.error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            // Nút Login
            Button(
                onClick = { onEvent(LoginUiAction.Login) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = stringResource(R.string.login))
            }
            Spacer(modifier = Modifier.height(20.dp))
            OrDivider()
            Spacer(modifier = Modifier.height(20.dp))
            SocialButtonsRow()
            Spacer(modifier = Modifier.height(20.dp))
            SignupRow(onNavigateTo = onNavigateTo)
        }
    }
}

// --------------------
// RememberAndForgotRow: Hiển thị checkbox Remember và nút Forgot Password
// --------------------
@Composable
private fun RememberAndForgotRow(
    uiState: LoginUiState,
    onEvent: (LoginUiAction) -> Unit,
    onNavigateTo: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = uiState.rememberLogin,
                onCheckedChange = { onEvent(LoginUiAction.OnRememberLogin(it)) }
            )
            Text(
                text = stringResource(R.string.login_remember_login),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                )
            )
        }
        Text(
            text = stringResource(R.string.login_forgot_pw),
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
            ),
            modifier = Modifier.clickable { onNavigateTo("forgotPassword") }
        )
    }
}

// --------------------
// OrDivider: Hiển thị divider với chữ OR
// --------------------
@Composable
private fun OrDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(Color.Gray)
        )
        Text(
            text = "OR",
            modifier = Modifier.padding(horizontal = 20.dp),
            style = MaterialTheme.typography.titleSmall
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(Color.Gray)
        )
    }
}

// --------------------
// SocialButtonsRow: Hiển thị các nút đăng nhập qua mạng xã hội
// --------------------
@Composable
private fun SocialButtonsRow() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        RoundedButton(size = 64, onClick = {}) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email Icon",
                tint = Color.White,
            )
        }
        RoundedButton(size = 64, onClick = {}) {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Phone Icon",
                modifier = Modifier.weight(1f)
            )
        }
        RoundedButton(size = 64, onClick = {}) {
            Image(
                painter = painterResource(R.drawable.facebook),
                contentDescription = "Facebook Icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// --------------------
// SignupRow: Hiển thị lời nhắc đăng ký tài khoản mới
// --------------------
@Composable
private fun SignupRow(onNavigateTo: (String) -> Unit) {
    Row {
        Text(
            text = stringResource(R.string.login_singup_instruction),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.sign_up),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onNavigateTo("signup") }
        )
    }
}

// --------------------
// Preview
// --------------------
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val fakeState = LoginUiState(
        username = "",
        password = "",
        rememberLogin = false,
        isError = false,
        error = ""
    )
    WorkTimeTrackerManagerTheme {
        LoginContent(
            uiState = fakeState,
            onAction = {},
            onNavigateTo = {}
        )
    }
}
