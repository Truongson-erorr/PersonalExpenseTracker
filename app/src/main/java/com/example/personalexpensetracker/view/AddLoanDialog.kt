package com.example.personalexpensetracker.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.personalexpensetracker.model.Loan
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddLoanDialog(
    userId: String,
    onDismiss: () -> Unit,
    onAdd: (Loan) -> Unit
) {
    var borrower by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm khoản vay") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = borrower,
                    onValueChange = { borrower = it },
                    label = { Text("Người vay") }
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Số tiền") }
                )
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Lý do") }
                )
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Hạn trả (dd/MM/yyyy)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val timestamp = df.parse(dueDate)?.time ?: System.currentTimeMillis()
                onAdd(
                    Loan(
                        borrower = borrower,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        reason = reason,
                        dueDate = timestamp,
                        userId = userId
                    )
                )
            }) {
                Text("Thêm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Huỷ")
            }
        }
    )
}
