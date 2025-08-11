package com.example.personalexpensetracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarms
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun HelpGuideScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Hướng dẫn sử dụng",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        val steps = listOf(
            Triple("Đăng nhập / Đăng ký", "Sử dụng email và mật khẩu để đăng nhập.\nNếu chưa có tài khoản, chọn 'Đăng ký' để tạo mới.", Icons.Default.Person),
            Triple("Thêm khoản thu/chi", "Chọn nút '+' ở màn hình chính.\nNhập số tiền, loại giao dịch, và ghi chú nếu cần.", Icons.Default.AddCircle),
            Triple("Quản lý ngân sách", "Vào mục 'Ngân sách' để đặt giới hạn chi tiêu.\nỨng dụng sẽ cảnh báo khi sắp vượt mức.", Icons.Default.AccountBalanceWallet),
            Triple("Xem báo cáo", "Biểu đồ và thống kê giúp bạn theo dõi thu chi theo ngày, tháng, năm.\nCó thể lọc theo loại giao dịch để phân tích chi tiết.", Icons.Default.BarChart),
            Triple("Quản lý tiết kiệm", "Tạo các mục tiêu tiết kiệm và theo dõi tiến độ.\nỨng dụng sẽ nhắc nhở bạn khi đến hạn.", Icons.Default.AccountBalanceWallet),
            Triple("Vay / Cho vay", "Theo dõi các khoản vay và cho vay, kèm lịch nhắc thanh toán.\nGiúp tránh quên nợ hoặc bị trễ hạn.", Icons.Default.AccountBalanceWallet),
            Triple("Đầu tư", "Ghi lại các khoản đầu tư và lãi/lỗ.\nCó thể phân loại theo cổ phiếu, crypto hoặc quỹ.", Icons.Default.BarChart),
            Triple("Nhắc nhở hóa đơn", "Thiết lập lịch nhắc thanh toán điện, nước, internet...\nTránh bị trễ hạn và mất phí phạt.", Icons.Default.AccessAlarms),
            Triple("Xuất dữ liệu", "Xuất dữ liệu thu/chi ra file Excel hoặc PDF.\nPhục vụ việc lưu trữ hoặc chia sẻ.", Icons.Default.BarChart),
            Triple("Đồng bộ đám mây", "Đồng bộ dữ liệu qua tài khoản Google hoặc server riêng.\nGiúp bảo vệ dữ liệu và dùng trên nhiều thiết bị.", Icons.Default.Cloud),
            Triple("Tùy chỉnh giao diện", "Chọn màu theme, chế độ tối/sáng và font chữ.\nGiúp cá nhân hóa trải nghiệm người dùng.", Icons.Default.Brightness6),
            Triple("Bảo mật", "Đặt mã PIN hoặc vân tay để mở ứng dụng.\nTăng tính an toàn cho dữ liệu tài chính của bạn.", Icons.Default.Security)
        )

        val cardColors = listOf(
            Color.White,
        )

        steps.forEachIndexed { index, step ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardColors[index % cardColors.size]),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = step.third,
                        contentDescription = step.first,
                        tint = Color.LightGray,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${index + 1}. ${step.first}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = step.second,
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}
