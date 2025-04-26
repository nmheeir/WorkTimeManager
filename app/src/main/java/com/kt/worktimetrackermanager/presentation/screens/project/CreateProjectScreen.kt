package com.kt.worktimetrackermanager.presentation.screens.project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kt.worktimetrackermanager.presentation.viewmodels.CreateProjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    navController: NavHostController,
    viewModel: CreateProjectViewModel = hiltViewModel(),
) {
    val purpleColor = Color(0xFFAA80FF)
    val lightPurpleColor = Color(0xFFF0EBFF)
    val backgroundColor = Color(0xFFF5F5F5)

    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

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
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                IconButton(onClick = navController::navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = purpleColor
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Create New Project",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Main Content Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Attachment Section
                    Text(
                        text = "Attachment",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "Format should be in .pdf .jpeg .png less than 5MB",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    // Attachment Boxes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(3) {
                            AttachmentBox(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f),
                                purpleColor = purpleColor,
                                lightPurpleColor = lightPurpleColor
                            )
                        }
                    }

                    // Task Title
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
                            Icon(
                                painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_edit),
                                contentDescription = "Task",
                                tint = purpleColor,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(4.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = purpleColor,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    // Task Description
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

                    // Assign To
                    Text(
                        text = "Assign To",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )

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
                        purpleColor = purpleColor
                    )

                    // Priority
                    Text(
                        text = "Priority",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )

                    SelectField(
                        text = "Select Priority",
                        iconContent = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(2.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    repeat(3) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(4.dp)
                                                .background(purpleColor)
                                        )
                                    }
                                }
                            }
                        },
                        purpleColor = purpleColor
                    )

                    // Difficulty
                    Text(
                        text = "Difficulty",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )

                    SelectField(
                        text = "Select Difficulty",
                        iconContent = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(purpleColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "⚙️",
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        purpleColor = purpleColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Create Task Button
            Button(
                onClick = { /* Handle task creation */ },
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
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { /* Handle selection */ },
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
            iconContent()

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                color = Color.DarkGray,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
                tint = Color.Gray
            )
        }
    }
}