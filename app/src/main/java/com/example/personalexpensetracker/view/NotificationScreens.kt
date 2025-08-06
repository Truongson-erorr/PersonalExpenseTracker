package com.example.personalexpensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalexpensetracker.viewmodel.NotificationViewModel
import com.example.personalexpensetracker.model.Notification
import java.text.SimpleDateFormat
import java.util.*
import androidx.navigation.NavController

@Composable
fun NotificationScreen(
    navController: NavController,
    userId: String,
    viewModel: NotificationViewModel = viewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    var selectedNotification by remember { mutableStateOf<Notification?>(null) }

    LaunchedEffect(userId) {
        viewModel.getNotificationsForUser(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Hoạt động gần đây",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Không có thông báo nào.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(notifications) { notification ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!notification.isRead) Color(0xFFFFF8E1) else Color.White
                        ),
                        onClick = {
                            selectedNotification = notification
                            if (!notification.isRead) {
                                viewModel.markAsRead(notification.id)
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = notification.title,
                                    fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = notification.message,
                                    fontSize = 14.sp,
                                    color = Color.DarkGray,
                                    fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                        .format(Date(notification.timestamp)),
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Hiển thị dialog chi tiết
    selectedNotification?.let { notification ->
        AlertDialog(
            onDismissRequest = { selectedNotification = null },
            confirmButton = {
                TextButton(onClick = { selectedNotification = null }) {
                    Text("Đóng")
                }
            },
            title = {
                Text(
                    text = notification.title,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(notification.message)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            .format(Date(notification.timestamp)),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        )
    }
}

