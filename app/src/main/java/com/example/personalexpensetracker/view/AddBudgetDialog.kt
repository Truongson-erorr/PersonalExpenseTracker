package com.example.personalexpensetracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalexpensetracker.model.Budget
import com.example.personalexpensetracker.viewmodel.BudgetViewModel
import com.example.personalexpensetracker.viewmodel.NotificationViewModel
import java.text.Normalizer
import java.util.*
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
        shape = RoundedCornerShape(20.dp),
        containerColor = Color(0xFFFDFDFD),
        title = {
            Text(
                text = "📊 Thêm Ngân Sách",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BudgetInputField(value = category, onValueChange = { category = it }, label = "Danh mục")
                BudgetInputField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Hạn mức (VND)",
                    keyboardType = KeyboardType.Number
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    BudgetInputField(
                        value = selectedMonth.toString(),
                        onValueChange = { selectedMonth = it.toIntOrNull() ?: calendar.get(Calendar.MONTH) + 1 },
                        label = "Tháng",
                        placeholder = "MM",
                        modifier = Modifier.weight(1f)
                    )
                    BudgetInputField(
                        value = selectedYear.toString(),
                        onValueChange = { selectedYear = it.toIntOrNull() ?: calendar.get(Calendar.YEAR) },
                        label = "Năm",
                        placeholder = "YYYY",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
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
                                userId,
                                "Ngân sách mới",
                                "Bạn vừa thêm ngân sách ${String.format("%,.0f", amountDouble)} VND cho $category trong $selectedMonth/$selectedYear."
                            )
                            onDismiss()
                        },
                        onFailure = { onDismiss() }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(46.dp)
            ) {
                Text("Lưu", color = Color.White, fontWeight = FontWeight.SemiBold)
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
fun BudgetInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = if (placeholder.isNotEmpty()) ({ Text(placeholder, color = Color.Gray) }) else null,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color(0xFFF7F7F7),
            unfocusedContainerColor = Color(0xFFF7F7F7),
            cursorColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Gray
        ),
        shape = RoundedCornerShape(14.dp)
    )
}

fun String.normalize(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(temp).replaceAll("")
        .lowercase(Locale.getDefault())
        .trim()
}
