package com.kt.worktimetrackermanager.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.presentation.components.image.CoilImage
import com.kt.worktimetrackermanager.presentation.viewmodels.HomeViewModel
import timber.log.Timber

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

    }
}

@Composable
private fun HomeTopBar(
    modifier: Modifier = Modifier
) {

    Row {

    }
}