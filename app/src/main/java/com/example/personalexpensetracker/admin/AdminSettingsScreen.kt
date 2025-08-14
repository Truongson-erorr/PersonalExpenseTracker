package com.example.personalexpensetracker.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.personalexpensetracker.R

data class AdminSettingItem(
    val title: String,
    val description: String = "",
    val onClick: () -> Unit
)

@Composable
fun AdminSettingsScreen() {
    val settings = listOf(
        AdminSettingItem("Thông tin cá nhân") {  },
        AdminSettingItem("Quản lý người dùng") {  },
        AdminSettingItem("Quản lý giao dịch") {  },
        AdminSettingItem("Quản lý ngân sách") {  },
        AdminSettingItem("Đăng xuất") {  }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar Admin",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(100.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Cài đặt Admin",
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(settings.size) { index ->
                val item = settings[index]
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { item.onClick() },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = item.title, fontSize = 18.sp, color = Color.Black)
                        if (item.description.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = item.description, fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}
