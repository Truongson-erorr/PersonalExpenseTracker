package com.example.personalexpensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.HelpItem

val helpItems = listOf(
    HelpItem(
        icon = Icons.Default.MenuBook,
        title = "Hướng dẫn sử dụng",
        description = "Xem cách nhập thu/chi, quản lý ngân sách và theo dõi báo cáo."
    ),
    HelpItem(
        icon = Icons.Default.TipsAndUpdates,
        title = "Mẹo quản lý tài chính",
        description = "Áp dụng quy tắc tiết kiệm và kiểm soát chi tiêu hiệu quả."
    ),
    HelpItem(
        icon = Icons.Default.SupportAgent,
        title = "Liên hệ hỗ trợ",
        description = "Gửi phản hồi hoặc chat trực tiếp với đội ngũ hỗ trợ."
    ),
    HelpItem(
        icon = Icons.Default.Info,
        title = "Thông tin ứng dụng",
        description = "Xem phiên bản, điều khoản và chính sách bảo mật."
    )
)

@Composable
fun HelpScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Trung tâm trợ giúp",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        helpItems.forEach { item ->
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        when (item.title) {
                            "Hướng dẫn sử dụng" -> navController.navigate("HelpGuideScreen")
                            "Mẹo quản lý tài chính" -> navController.navigate("FinanceTipsScreen")
                            "Liên hệ hỗ trợ" -> navController.navigate("ChatScreen")
                            "Thông tin ứng dụng" -> navController.navigate("AppInfoScreen")
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = Color.DarkGray,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = item.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Text(
                            text = item.description,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
