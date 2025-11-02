package com.example.personalexpensetracker.view

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Note
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.Transaction
import com.example.personalexpensetracker.model.TransactionType
import com.example.personalexpensetracker.viewmodel.NotificationViewModel
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import java.util.*

@Composable
fun AddTransactionDialog(
    userId: String,
    onDismiss: () -> Unit,
    navController: NavController,
    viewModel: TransactionViewModel,
    notificationViewModel: NotificationViewModel = viewModel()
) {
    val context = LocalContext.current

    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }

    var isSpeakingCategory by remember { mutableStateOf(false) }

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val resultText =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
            if (resultText != null) {
                if (isSpeakingCategory) {
                    category = resultText
                } else {
                    note = resultText
                }
            }
        }
    }

    fun startSpeech(isCategory: Boolean) {
        isSpeakingCategory = isCategory
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Nói gì đó…")
        }
        speechRecognizerLauncher.launch(intent)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color(0xFFFDFDFD),
        title = {
            Text(
                "💸 Thêm Giao Dịch",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                TransactionInputField(
                    value = category,
                    onValueChange = { category = it },
                    label = "Danh mục",
                    onMicClick = { startSpeech(true) }
                )

                TransactionInputField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Số tiền",
                    keyboardType = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                )

                TransactionInputField(
                    value = note,
                    onValueChange = { note = it },
                    label = "Ghi chú",
                    onMicClick = { startSpeech(false) }
                )


                DropdownMenuBox(
                    selectedType = type,
                    onTypeSelected = { type = it }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull()
                    if (amountDouble == null || category.isBlank()) return@Button

                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"))
                    val currentDate = calendar.timeInMillis

                    val transaction = Transaction(
                        id = UUID.randomUUID().toString(),
                        userId = userId,
                        type = type,
                        amount = amountDouble,
                        category = category,
                        note = note.takeIf { it.isNotBlank() },
                        date = currentDate
                    )

                    viewModel.addTransaction(
                        transaction,
                        onSuccess = {
                            viewModel.getTransactionsByUser(userId)
                            notificationViewModel.addNotification(
                                userId = userId,
                                title = "Giao dịch mới",
                                message = "Bạn vừa thêm ${if (type == TransactionType.EXPENSE) "khoản chi" else "khoản thu"}: ${String.format("%,.0f", amountDouble)} VND cho $category."
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
fun TransactionInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardOptions = KeyboardOptions.Default,
    onMicClick: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        trailingIcon = {
            if (onMicClick != null) {
                IconButton(onClick = onMicClick) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = Color.Black)
                }
            }
        },
        keyboardOptions = keyboardType,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
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

@Composable
fun DropdownMenuBox(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEEEEE)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                when (selectedType) {
                    TransactionType.EXPENSE -> "Chi tiêu"
                    TransactionType.INCOME -> "Thu nhập"
                },
                color = Color.Black
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("Chi tiêu") },
                onClick = {
                    onTypeSelected(TransactionType.EXPENSE)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Thu nhập") },
                onClick = {
                    onTypeSelected(TransactionType.INCOME)
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun SummaryItem(title: String, amount: Double, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${String.format("%,.0f", amount)} VND",
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}