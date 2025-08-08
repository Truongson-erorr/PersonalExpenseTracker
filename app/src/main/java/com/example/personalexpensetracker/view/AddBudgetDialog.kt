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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalexpensetracker.model.Budget
import com.example.personalexpensetracker.model.TransactionType
import com.example.personalexpensetracker.viewmodel.BudgetViewModel
import com.example.personalexpensetracker.viewmodel.NotificationViewModel
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import java.text.Normalizer
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import java.util.regex.Pattern

@Composable
fun AddBudgetDialog(
    userId: String,
    onDismiss: () -> Unit,
    budgetViewModel: BudgetViewModel,
    notificationViewModel: NotificationViewModel = viewModel()
) {
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm ngân sách", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Danh mục") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Hạn mức (VND)") },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = selectedMonth.toString(),
                        shape = RoundedCornerShape(12.dp),
                        onValueChange = { selectedMonth = it.toIntOrNull() ?: calendar.get(Calendar.MONTH) + 1 },
                        label = { Text("Tháng") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = selectedYear.toString(),
                        shape = RoundedCornerShape(12.dp),
                        onValueChange = { selectedYear = it.toIntOrNull() ?: calendar.get(Calendar.YEAR) },
                        label = { Text("Năm") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val amountDouble = amount.toDoubleOrNull() ?: return@Button
                if (category.isBlank()) return@Button

                val budget = Budget(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    category = category,
                    amount = amountDouble,
                    month = selectedMonth,
                    year = selectedYear
                )

                budgetViewModel.addBudget(
                    budget,
                    onSuccess = {
                        notificationViewModel.addNotification(
                            userId = userId,
                            title = "Ngân sách mới",
                            message = "Bạn vừa thêm ngân sách ${String.format("%,.0f", amountDouble)} VND cho danh mục $category trong tháng $selectedMonth/$selectedYear."
                        )
                        onDismiss()
                    },
                    onFailure = { onDismiss() }
                )
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color.Gray)
            }
        }
    )
}

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

fun String.normalize(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(temp)
        .replaceAll("")
        .lowercase(Locale.getDefault())
        .trim()
}