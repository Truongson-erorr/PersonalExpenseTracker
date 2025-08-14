package com.example.personalexpensetracker.admin

import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Xin chào, Admin 👋",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                AdminOptionCard(
                    icon = Icons.Default.People,
                    title = "Người dùng",
                    backgroundColor = Color(0xFFE3F2FD),
                    iconTint = Color(0xFF1E88E5)
                ) {
                    navController.navigate("UserManagement")
                }
            }
            item {
                AdminOptionCard(
                    icon = Icons.Default.BarChart,
                    title = "Thống kê",
                    backgroundColor = Color(0xFFFFF3E0),
                    iconTint = Color(0xFFFB8C00)
                ) {
                    navController.navigate("StatisticsScreen")
                }
            }
            item {
                AdminOptionCard(
                    icon = Icons.Default.Feedback,
                    title = "Phản hồi",
                    backgroundColor = Color(0xFFE8F5E9),
                    iconTint = Color(0xFF43A047)
                ) {
                    navController.navigate("Feedback")
                }
            }
            item {
                AdminOptionCard(
                    icon = Icons.Default.AttachMoney,
                    title = "Ngân sách",
                    backgroundColor = Color(0xFFE0F7FA),
                    iconTint = Color(0xFF00ACC1)
                ) { navController.navigate("BudgetScreen") }
            }
            item {
                AdminOptionCard(
                    icon = Icons.Default.ShoppingCart,
                    title = "Giao dịch",
                    backgroundColor = Color(0xFFFFEBEE),
                    iconTint = Color(0xFFD32F2F)
                ) { navController.navigate("TransactionManagement") }
            }
            item {
                AdminOptionCard(
                    icon = Icons.Default.Notifications,
                    title = "Thông báo",
                    backgroundColor = Color(0xFFFFF8E1),
                    iconTint = Color(0xFFFBC02D)
                ) { navController.navigate("NotificationsScreen") }
            }
            item {
                AdminOptionCard(
                    icon = Icons.Default.Assessment,
                    title = "Báo cáo",
                    backgroundColor = Color(0xFFEDE7F6),
                    iconTint = Color(0xFF5E35B1)
                ) { navController.navigate("ReportsScreen") }
            }
            item {
                AdminOptionCard(
                    icon = Icons.Default.Help,
                    title = "Hỗ trợ",
                    backgroundColor = Color(0xFFE1F5FE),
                    iconTint = Color(0xFF0288D1)
                ) { navController.navigate("SupportScreen") }
            }
        }
    }
}

@Composable
fun AdminOptionCard(
    icon: ImageVector,
    title: String,
    backgroundColor: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

