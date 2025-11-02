package com.example.personalexpensetracker.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensetracker.model.Loan
import java.text.SimpleDateFormat
import java.util.*

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
    var debt by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color(0xFFFDFDFD),
        title = {
            Text(
                "💼 Thêm khoản vay / nợ",
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
                LoanInputField(
                    value = borrower,
                    onValueChange = { borrower = it },
                    label = "Người liên quan",
                )

                LoanInputField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Số tiền (VND)",
                    keyboardType = KeyboardType.Number,
                )

                LoanInputField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = "Lý do",
                )

                LoanInputField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = "Hạn trả (dd/MM/yyyy)",
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(12.dp))
                    RadioButton(selected = !debt, onClick = { debt = false })
                    Text("Khoản cho vay", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = debt, onClick = { debt = true })
                    Text("Khoản nợ", fontSize = 13.sp)
                }

                AnimatedVisibility(visible = errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (borrower.isBlank() || amountValue == null || amountValue <= 0) {
                        errorMessage = "Vui lòng nhập đầy đủ và hợp lệ."
                        return@Button
                    }

                    val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val timestamp = try {
                        df.parse(dueDate)?.time ?: System.currentTimeMillis()
                    } catch (e: Exception) {
                        System.currentTimeMillis()
                    }

                    onAdd(
                        Loan(
                            borrower = borrower,
                            amount = amountValue,
                            reason = reason,
                            dueDate = timestamp,
                            userId = userId,
                            debt = debt,
                            paid = false
                        )
                    )
                    onDismiss()
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
fun LoanInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
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

