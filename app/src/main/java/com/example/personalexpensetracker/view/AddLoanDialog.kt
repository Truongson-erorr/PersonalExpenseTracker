package com.example.personalexpensetracker.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.personalexpensetracker.model.Loan
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
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
        title = {
            Text("Thêm khoản vay")
                },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = borrower,
                    onValueChange = { borrower = it },
                    label = { Text("Người vay") },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                TextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Số tiền") },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                TextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Lý do") },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                TextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Hạn trả (dd/MM/yyyy)") },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Huỷ", color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(2.dp))

                Button(
                    onClick = {
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
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(26.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Thêm")
                }
            }
        }
    )
}
