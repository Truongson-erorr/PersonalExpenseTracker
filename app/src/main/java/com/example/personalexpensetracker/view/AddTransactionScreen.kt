package com.example.personalexpensetracker.view

import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.Transaction
import com.example.personalexpensetracker.model.TransactionType
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import java.util.*

@Composable
fun AddTransactionScreen(
    navController: NavController,
    userId: String,
    viewModel: TransactionViewModel = viewModel()
) {
    val context = LocalContext.current
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }

    val categoryVoiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
        spokenText?.let { category = it }
    }

    val noteVoiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
        spokenText?.let { note = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Quay lại"
                )
            }

            Text(
                text = "Thêm giao dịch",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Số tiền", fontSize = 14.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN")
                }
                categoryVoiceLauncher.launch(intent)
            }) {
                Icon(Icons.Default.Mic, contentDescription = "Giọng nói")
            }
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Danh mục", fontSize = 14.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN")
                }
                categoryVoiceLauncher.launch(intent)

            }) {
                Icon(Icons.Default.Mic, contentDescription = "Giọng nói")
            }
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Ghi chú", fontSize = 14.sp) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Loại giao dịch:", fontSize = 16.sp)
            DropdownMenuBox(selectedType = type, onTypeSelected = { type = it })
        }

        Button(
            onClick = {
                val amountDouble = amount.toDoubleOrNull()
                if (amountDouble == null || category.isBlank()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val transaction = Transaction(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    type = type,
                    amount = amountDouble,
                    category = category,
                    note = note.takeIf { it.isNotBlank() },
                    date = System.currentTimeMillis()
                )

                viewModel.addTransaction(
                    transaction,
                    onSuccess = {
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    onFailure = {
                        Toast.makeText(context, "Lỗi khi thêm: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Lưu giao dịch", color = Color.White)
        }
    }
}

@Composable
fun DropdownMenuBox(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEEEEE))) {
            Text(
                when (selectedType) {
                    TransactionType.EXPENSE -> "Chi tiêu"
                    TransactionType.INCOME -> "Thu nhập"
                },
                color = Color.Black
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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
