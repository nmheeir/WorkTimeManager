package com.kt.worktimetrackermanager.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.presentation.components.scaffold.CustomTopBar
import com.kt.worktimetrackermanager.presentation.viewmodels.CreateNewPasswordUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.ForgotPasswordUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.ForgotPasswordUiState
import com.kt.worktimetrackermanager.presentation.viewmodels.ForgotPasswordViewModel


@Composable
fun CreateNewPasswordScreen(
    navController: NavHostController,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    token: String
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.onAction(ForgotPasswordUiAction.UpdateToken(token))

    ObserveAsEvents(viewModel.channel2) {
        when (it) {
            is CreateNewPasswordUiEvent.ResetPasswordFailure -> {
                Toast.makeText(context, it.msg, Toast.LENGTH_SHORT).show()
            }

            CreateNewPasswordUiEvent.ResetPasswordSuccess -> {
                navController.navigate("resetPasswordSuccess")
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.label_back),
                onBack = {
                    navController.popBackStack()
                }
            )
        },
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(paddingValues)
        ) {
            CreateNewPasswordDetail(
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            PasswordInputSection(
                modifier = Modifier.padding(horizontal = 24.dp),
                state = uiState,
                action = viewModel::onAction
            )
            if (uiState.isError) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = uiState.error,
                    style = MaterialTheme.typography.bodyMedium, color = Color.Red
                )
            }
            Button(
                onClick = {
                    if (uiState.newPassword == uiState.confirmPassword) {
                        viewModel.onAction(ForgotPasswordUiAction.ResetNewPassword)
                    } else {
                        viewModel.onAction(ForgotPasswordUiAction.PasswordNotMatch)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(1.dp),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.reset_pw))
            }
        }
    }
}

@Composable
private fun CreateNewPasswordDetail(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.create_new_pw),
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = stringResource(R.string.new_pw_instruction),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PasswordInputSection(
    modifier: Modifier = Modifier,
    state: ForgotPasswordUiState,
    action: (ForgotPasswordUiAction) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        PasswordInputField(
            title = stringResource(R.string.auth_new_pw),
            value = {
                state.newPassword
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onValueChange = {
                action(ForgotPasswordUiAction.OnNewPasswordChange(it))
            }
        )

        PasswordInputField(
            title = stringResource(R.string.auth_new_pw),
            value = {
                state.confirmPassword
            },
            onValueChange = {
                action(ForgotPasswordUiAction.OnConfirmPasswordChange(it))
            }
        )
    }
}

@Composable
private fun PasswordInputField(
    modifier: Modifier = Modifier,
    title: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    value: () -> String,
    onValueChange: (String) -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge
        )
        OutlinedTextField(
            value = value(),
            onValueChange = {
                onValueChange(it)
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val iconResource =
                    if (showPassword) R.drawable.ic_hide_pw else R.drawable.ic_show_pw
                Icon(
                    painter = painterResource(iconResource),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            showPassword = !showPassword
                        }
                )
            },
            keyboardOptions = keyboardOptions,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}