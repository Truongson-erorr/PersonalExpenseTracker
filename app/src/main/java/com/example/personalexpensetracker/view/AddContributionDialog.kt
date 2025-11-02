package com.example.personalexpensetracker.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensetracker.model.Saving
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AddContributionDialog(
    saving: Saving,
    onDismiss: () -> Unit,
    onAdd: (Double) -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val value = amount.toDoubleOrNull()
                    when {
                        value == null -> errorMessage = "Vui lòng nhập số hợp lệ."
                        value <= 0 -> errorMessage = "Số tiền phải lớn hơn 0."
                        else -> {
                            onAdd(value)
                            onDismiss()
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00897B),
                    contentColor = Color.White
                )
            ) {
                Text("💰 Góp tiền", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color.Gray)
            }
        },
        title = {
            Text(
                text = "Góp tiền vào hũ tiết kiệm",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "Nhập số tiền bạn muốn góp thêm vào '${saving.title}':",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        errorMessage = null
                    },
                    label = { Text("Số tiền (VND)") },
                    shape = RoundedCornerShape(10.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Money,
                            contentDescription = null,
                            tint = Color(0xFF00897B)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00897B),
                        focusedLabelColor = Color(0xFF00897B)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

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
        shape = RoundedCornerShape(16.dp)
    )
}
