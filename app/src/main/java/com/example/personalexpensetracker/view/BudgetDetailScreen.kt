package com.example.personalexpensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.Budget
import com.example.personalexpensetracker.model.TransactionType
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetDetailScreen(
    budget: Budget,
    transactionViewModel: TransactionViewModel,
    navController: NavController
) {
    val transactions = transactionViewModel.transactions.filter {
        it.category.normalize() == budget.category.normalize() &&
                it.type == TransactionType.EXPENSE
    }

    val spent = transactions.filter {
        val cal = Calendar.getInstance().apply { timeInMillis = it.date }
        cal.get(Calendar.MONTH) + 1 == budget.month && cal.get(Calendar.YEAR) == budget.year
    }.sumOf { it.amount }

    val progress = (spent / budget.amount).coerceIn(0.0, 1.0)
    val percent = (progress * 100).toInt()
    val remaining = budget.amount - spent

    val progressColor = when {
        progress >= 1.0 -> Color.Red
        progress > 0.8 -> Color(0xFFFFC107)
        progress > 0.5 -> Color(0xFF03A9F4)
        else -> Color(0xFF4CAF50)
    }

    val reminderText = when {
        progress >= 1.0 -> "Đã đạt hoặc vượt quá ngân sách! Cần điều chỉnh ngay"
        progress > 0.8 -> "Sắp đạt ngân sách. Hãy cân nhắc chi tiêu!"
        progress > 0.5 -> "Bạn đã dùng hơn nửa ngân sách"
        else -> "Chi tiêu đang trong tầm kiểm soát"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết ngân sách") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Danh mục: ${budget.category}")
            Text("Hạn mức: ${String.format("%,.0f", budget.amount)} VND")
            Text("Đã chi: ${String.format("%,.0f", spent)} VND")
            Text(
                "Còn lại: ${String.format("%,.0f", remaining)} VND",
                color = if (remaining >= 0) Color.Black else Color.Red
            )

            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = progressColor,
                trackColor = Color.LightGray
            )

            Spacer(Modifier.height(8.dp))
            Text("Đã sử dụng: $percent%", color = progressColor)

            Spacer(Modifier.height(8.dp))
            Text(reminderText, color = progressColor, fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(16.dp))
            if (transactions.isNotEmpty()) {
                Text("Giao dịch trong tháng:", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    transactions
                        .filter {
                            val cal = Calendar.getInstance().apply { timeInMillis = it.date }
                            cal.get(Calendar.MONTH) + 1 == budget.month &&
                                    cal.get(Calendar.YEAR) == budget.year
                        }
                        .sortedByDescending { it.date }
                        .forEach { tx ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    tx.note?.let { Text(it.ifBlank { tx.category }, fontWeight = FontWeight.SemiBold) }
                                    Text(
                                        "${String.format("%,.0f", tx.amount)} VND",
                                        color = Color.Red,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                }
            } else {
                Text("Không có giao dịch nào trong danh mục này tháng này.", color = Color.Gray)
            }
        }
    }
}
