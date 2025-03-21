package com.kt.worktimetrackermanager.presentation.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.presentation.activities.LocalMainViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
) {
    val mainViewModel = LocalMainViewModel.current

    TextButton(
        onClick = {
            mainViewModel.logout()
            navController.navigate("login") {
                popUpTo("home") {
                    inclusive = true
                }
            }
        }
    ) {
        Text(
            text = "Log out"
        )
    }

}