package com.example.personalexpensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.Loan
import com.example.personalexpensetracker.viewmodel.LoanViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LoanScreen(
    navController: NavController,
    userId: String,
    viewModel: LoanViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val loans by viewModel.loans.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLoans(userId)
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text("Quản lý khoản vay", fontSize = 22.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            if (loans.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Chưa có khoản vay nào")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(loans) { loan ->
                        LoanItem(
                            loan = loan,
                            onPaid = { viewModel.updateLoan(it.copy(paid = true)) },
                            onDelete = { viewModel.deleteLoan(it) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = Color.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm", tint = Color.White)
        }
    }

    if (showDialog) {
        AddLoanDialog(
            onDismiss = { showDialog = false },
            onAdd = { loan ->
                viewModel.addLoan(loan.copy(userId = userId))
                showDialog = false
            }
        )
    }
}

@Composable
fun LoanItem(
    loan: Loan,
    onPaid: (Loan) -> Unit,
    onDelete: (Loan) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Người vay: ${loan.borrower}", fontSize = 16.sp, color = Color.Black)
                Text("Số tiền: %, .0fđ".format(loan.amount), fontSize = 16.sp)
                Text("Hạn trả: ${dateFormat.format(Date(loan.dueDate))}", fontSize = 14.sp)
                Text("Lý do: ${loan.reason}", fontSize = 14.sp)
                Text(
                    "Trạng thái: ${if (loan.paid) "Đã trả" else "Chưa trả"}",
                    fontSize = 14.sp,
                    color = if (loan.paid) Color.Green else Color.Red
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (!loan.paid) {
                    IconButton(onClick = { onPaid(loan) }) {
                        Icon(Icons.Default.Done, contentDescription = "Đã trả", tint = Color.Green)
                    }
                }
                IconButton(onClick = { onDelete(loan) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Xoá", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun AddLoanDialog(
    onDismiss: () -> Unit,
    onAdd: (Loan) -> Unit
) {
    var borrower by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") } // dd/MM/yyyy

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
                onAdd(Loan(borrower = borrower, amount = amount.toDoubleOrNull() ?: 0.0, reason = reason, dueDate = timestamp))
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
