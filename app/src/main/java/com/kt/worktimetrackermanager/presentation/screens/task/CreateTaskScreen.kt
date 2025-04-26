package com.kt.worktimetrackermanager.presentation.screens.task

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.R
import com.kt.worktimetrackermanager.core.presentation.hozPadding
import com.kt.worktimetrackermanager.core.presentation.padding
import com.kt.worktimetrackermanager.core.presentation.utils.Gap
import com.kt.worktimetrackermanager.core.presentation.utils.ObserveAsEvents
import com.kt.worktimetrackermanager.data.remote.dto.enums.Priority
import com.kt.worktimetrackermanager.data.remote.dto.response.User
import com.kt.worktimetrackermanager.presentation.components.dialog.DefaultDialog
import com.kt.worktimetrackermanager.presentation.components.dialog.ListDialog
import com.kt.worktimetrackermanager.presentation.components.image.CircleImage
import com.kt.worktimetrackermanager.presentation.viewmodels.CreateTaskUiAction
import com.kt.worktimetrackermanager.presentation.viewmodels.CreateTaskUiEvent
import com.kt.worktimetrackermanager.presentation.viewmodels.CreateTaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    navController: NavHostController,
    viewModel: CreateTaskViewModel = hiltViewModel(),
) {
    // Define colors
    val purpleColor = Color(0xFFAA80FF)
    val lightPurpleColor = Color(0xFFF0EBFF)
    val backgroundColor = Color(0xFFF5F5F5)

    // State for form fields
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val users by viewModel.users.collectAsStateWithLifecycle()
    val assignUsers by viewModel.assignUsers.collectAsStateWithLifecycle()
    val priority by viewModel.priority.collectAsStateWithLifecycle()

    var showSuccessDialog by remember { mutableStateOf(false) }

    ObserveAsEvents(viewModel.channel) {
        when (it) {
            CreateTaskUiEvent.Success -> {
                showSuccessDialog = true
            }
        }
    }

    if (showSuccessDialog) {
        DefaultDialog(
            onDismiss = { showSuccessDialog = false },
            icon = {
                Icon(painterResource(R.drawable.ic_check), null)
            }
        ) {
            Text(text = "Success")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header - Outside LazyColumn for fixed positioning
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                IconButton(onClick = navController::navigateUp) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = purpleColor
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Create New Task",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Main Content Card with LazyColumn
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                // Using LazyColumn for efficient scrolling
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Task Title
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Task Title",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )

                            OutlinedTextField(
                                value = taskTitle,
                                onValueChange = { taskTitle = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Enter Task Title") },
                                leadingIcon = {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(purpleColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "📝",
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = purpleColor,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )
                        }
                    }

                    // Task Description
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Task Description",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )

                            OutlinedTextField(
                                value = taskDescription,
                                onValueChange = { taskDescription = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                placeholder = { Text("Enter Task Description") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = purpleColor,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    // Assign To
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Assign To",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                            var showListUserDialog by remember { mutableStateOf(false) }
                            SelectField(
                                text = "Select Member",
                                iconContent = {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(purpleColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "👤",
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    }
                                },
                                purpleColor = purpleColor,
                                onClick = { showListUserDialog = true }
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small)
                            ) {
                                assignUsers.forEach { assignUser ->
                                    AssistChip(
                                        onClick = {
                                            viewModel.onAction(
                                                CreateTaskUiAction.AssignUser(
                                                    assignUser
                                                )
                                            )
                                        },
                                        label = {
                                            Text(text = assignUser.userFullName)
                                        },
                                        trailingIcon = {
                                            Icon(painterResource(R.drawable.ic_close), null)
                                        }
                                    )
                                }
                            }
                            if (showListUserDialog) {
                                ListUserDialog(
                                    users = users,
                                    onDismiss = { showListUserDialog = false },
                                    action = { viewModel.onAction(it) }
                                )
                            }
                        }
                    }

                    // Priority
                    item {
                        var showPriorityDialog by remember { mutableStateOf(false) }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Priority",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )

                            SelectField(
                                text = priority?.name ?: "Select Priority",
                                iconContent = {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(purpleColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Stacked lines icon for priority
                                        Column(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .fillMaxSize(),
                                            verticalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            repeat(3) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(2.dp)
                                                        .background(Color.White)
                                                )
                                            }
                                        }
                                    }
                                },
                                purpleColor = purpleColor,
                                onClick = { showPriorityDialog = true }
                            )
                            if (showPriorityDialog) {
                                PriorityDialog(
                                    priorities = Priority.entries,
                                    onDismiss = { showPriorityDialog = false },
                                    action = { viewModel.onAction(it) }
                                )
                            }

                        }
                    }

                    // Add some space at the bottom for better scrolling experience
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                enabled = taskTitle.isNotEmpty() && taskDescription.isNotEmpty() && assignUsers.isNotEmpty() && priority != null,
                onClick = {
                    viewModel.onAction(CreateTaskUiAction.CreateTask(taskTitle, taskDescription))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleColor
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Create Task",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun AttachmentBox(
    modifier: Modifier = Modifier,
    purpleColor: Color,
    lightPurpleColor: Color,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(lightPurpleColor)
            .border(
                border = BorderStroke(1.dp, purpleColor.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { /* Handle attachment selection */ },
        contentAlignment = Alignment.Center
    ) {
        // Upload icon
        Icon(
            painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_upload),
            contentDescription = "Upload",
            tint = purpleColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SelectField(
    text: String,
    iconContent: @Composable () -> Unit,
    purpleColor: Color,
    onClick: () -> Unit,
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Custom icon based on field type
            iconContent()

            Spacer(modifier = Modifier.width(12.dp))

            // Field text
            Text(
                text = text,
                color = Color.DarkGray,
                modifier = Modifier.weight(1f)
            )

            // Dropdown arrow
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
                tint = Color.Gray
            )
        }
    }
}

@Composable
private fun ListUserDialog(
    modifier: Modifier = Modifier,
    users: List<User>,
    onDismiss: () -> Unit,
    action: (CreateTaskUiAction) -> Unit,
) {
    ListDialog(
        onDismiss = onDismiss,
    ) {
        if (users.isEmpty()) {
            item {
                Text(
                    text = "No users available",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        items(
            items = users
        ) { user ->
            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(MaterialTheme.padding.mediumSmall),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        action(CreateTaskUiAction.AssignUser(user))
                        onDismiss()
                    }
                    .hozPadding()
            ) {
                CircleImage(
                    imageUrl = user.avatarUrl ?: "",
                    size = 36.dp
                )
                Text(
                    text = user.userFullName,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Gap(MaterialTheme.padding.small)
        }
    }
}

@Composable
private fun PriorityDialog(
    modifier: Modifier = Modifier,
    priorities: List<Priority>,
    onDismiss: () -> Unit,
    action: (CreateTaskUiAction) -> Unit,
) {
    ListDialog(
        onDismiss = onDismiss,
        modifier = modifier.fillMaxWidth()
    ) {
        items(
            items = priorities
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .hozPadding()
                    .fillMaxWidth()
                    .clickable {
                        action(CreateTaskUiAction.SetPriority(it))
                        onDismiss()
                    }
            ) {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Gap(MaterialTheme.padding.small)
        }
    }
}