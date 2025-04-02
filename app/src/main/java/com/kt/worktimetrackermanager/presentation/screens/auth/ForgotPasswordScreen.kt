package com.kt.worktimetrackermanager.presentation.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.presentation.components.topBar.CustomTopBar
import com.kt.worktimetrackermanager.presentation.viewmodels.ForgotPasswordUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.ForgotPasswordUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.ForgotPasswordUiState
import com.kt.worktimetrackermanager.presentation.viewmodels.ForgotPasswordViewModel
import kotlinx.coroutines.flow.Flow


@Composable
fun ForgotPasswordScreen(
    navController: NavHostController,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val userNotFoundMsg = stringResource(id = R.string.error_user_not_found)
    val unknownError = stringResource(id = R.string.error_unknown)
    val wrongPasswordMsg = stringResource(id = R.string.error_wrong_password)

    ObserveAsEvents(viewModel.channel) {
        when (it) {
            ForgotPasswordUiEvent.NotFoundUser -> {
                Toast.makeText(context, userNotFoundMsg, Toast.LENGTH_SHORT).show()
            }

            ForgotPasswordUiEvent.SendRequestSuccess -> {
//                Toast.makeText(context, unknownError, Toast.LENGTH_SHORT).show()
//                Hiện ra màn hình thành công và có nút chuyển tới ứng dụng gmail
            }


            ForgotPasswordUiEvent.UnknownError -> {
                Toast.makeText(context, unknownError, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = stringResource(R.string.forgot_pw),
                onBack = {navController.popBackStack()}
            )
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(paddingValues)
        ) {
            ForgotPasswordDetail(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            )
            ForgotPasswordEmailForm(
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                state = uiState,
                action = viewModel::onAction
            )
            Button(
                onClick = {
                    viewModel.onAction(ForgotPasswordUiAction.SendRequest)
                },
                shape = RoundedCornerShape(4.dp),
                enabled = uiState.isButtonEnabled,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Text(
                    text = stringResource(R.string.send_request),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordDetail(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.forgot_pw),
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = stringResource(R.string.forgot_pw_instruction),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ForgotPasswordEmailForm(
    modifier: Modifier = Modifier,
    action: (ForgotPasswordUiAction) -> Unit,
    state: ForgotPasswordUiState
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {

        Text(
            text = stringResource(R.string.label_email_address),
            style = MaterialTheme.typography.labelLarge
        )

        OutlinedTextField(
            value = state.email,
            onValueChange = {
                action(ForgotPasswordUiAction.OnEmailChange(it))
            },
            isError = state.isError,
            supportingText = {
                if (state.isError) {
                    Text(text = state.error)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}