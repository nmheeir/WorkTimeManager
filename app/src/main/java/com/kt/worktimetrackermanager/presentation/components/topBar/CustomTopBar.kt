package com.kt.worktimetrackermanager.presentation.components.topBar

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.kt.worktimetrackermanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(modifier: Modifier = Modifier, title: String, onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Normal
                )
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_left),
                contentDescription = "Icon Arrow Back",
                modifier = Modifier
                    .clickable {
                        onBack()
                    }
            )
        }
    )
}