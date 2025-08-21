package com.example.personalexpensetracker.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensetracker.model.Budget
import com.example.personalexpensetracker.model.TransactionType
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import java.util.Calendar

@Composable
fun BudgetDetailDialog(
    budget: Budget,
    transactionViewModel: TransactionViewModel,
    onDismiss: () -> Unit
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

    val (reminderMsg, reminderColor) = when {
        progress >= 1.0 -> "Đã đạt hoặc vượt quá ngân sách! Cần điều chỉnh ngay" to Color.Red
        progress > 0.8 -> "Sắp đạt ngân sách. Hãy cân nhắc chi tiêu!" to Color(0xFFFFC107)
        progress > 0.5 -> "Bạn đã dùng hơn nửa ngân sách" to Color(0xFF03A9F4)
        else -> "Chi tiêu đang trong tầm kiểm soát" to Color(0xFF4CAF50)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng", fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
                Text(
                    "Chi tiết ngân sách",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "${budget.category} • Tháng ${budget.month}/${budget.year}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text("Hạn mức", fontSize = 14.sp, color = Color.Gray)
                        Text(
                            "${String.format("%,.0f", budget.amount)} VND",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column {
                        Text("Đã chi", fontSize = 14.sp, color = Color.Gray)
                        Text(
                            "${String.format("%,.0f", spent)} VND",
                            fontWeight = FontWeight.SemiBold,
                            color = if (progress > 1) Color.Red else Color.Black
                        )
                    }
                }

                Column {
                    LinearProgressIndicator(
                        progress = progress.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = if (progress > 1) Color.Red else Color.DarkGray,
                        backgroundColor = Color.LightGray.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "$percent% đã sử dụng",
                        fontSize = 14.sp,
                        color = if (progress > 1) Color.Red else Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = reminderColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = reminderMsg,
                        color = reminderColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    )
}
