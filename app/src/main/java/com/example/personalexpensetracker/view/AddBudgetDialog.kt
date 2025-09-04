package com.example.personalexpensetracker.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Thêm ngân sách",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Danh mục") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Hạn mức (VND)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextField(
                        value = selectedMonth.toString(),
                        onValueChange = { selectedMonth = it.toIntOrNull() ?: calendar.get(Calendar.MONTH) + 1 },
                        label = { Text("Tháng") },
                        placeholder = { Text("MM") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    TextField(
                        value = selectedYear.toString(),
                        onValueChange = { selectedYear = it.toIntOrNull() ?: calendar.get(Calendar.YEAR) },
                        label = { Text("Năm") },
                        placeholder = { Text("YYYY") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: return@TextButton
                    if (category.isBlank()) return@TextButton

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
                                userId,
                                "Ngân sách mới",
                                "Bạn vừa thêm ngân sách ${String.format("%,.0f", amountDouble)} VND cho $category trong $selectedMonth/$selectedYear."
                            )
                            onDismiss()
                        },
                        onFailure = { onDismiss() }
                    )
                }
            ) {
                Text("Lưu", fontWeight = FontWeight.Bold, color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
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