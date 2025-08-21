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

fun String.normalize(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(temp)
        .replaceAll("")
        .lowercase(Locale.getDefault())
        .trim()
}