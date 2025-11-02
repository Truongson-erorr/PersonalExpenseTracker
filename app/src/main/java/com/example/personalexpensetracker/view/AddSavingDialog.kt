package com.example.personalexpensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.personalexpensetracker.model.Saving

@Composable
fun AddSavingDialog(
    onDismiss: () -> Unit,
    onAdd: (Saving) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var goalText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color(0xFFFDFDFD),
        title = {
            Text(
                text = "💰 Thêm Hũ Tiết Kiệm",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomInputField(value = title, onValueChange = { title = it }, label = "Tên hũ")
                CustomInputField(value = amountText, onValueChange = { amountText = it }, label = "Số tiền hiện tại")
                CustomInputField(value = goalText, onValueChange = { goalText = it }, label = "Mục tiêu (tùy chọn)")
                CustomInputField(value = note, onValueChange = { note = it }, label = "Ghi chú", singleLine = false)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    val goal = goalText.toDoubleOrNull()
                    onAdd(
                        Saving(
                            title = title,
                            amount = amount,
                            goalAmount = goal,
                            note = note
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(46.dp)
            ) {
                Text("Thêm", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Huỷ", color = Color.Gray)
            }
        }
    )
}

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
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
