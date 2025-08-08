package com.example.personalexpensetracker.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.Transaction
import com.example.personalexpensetracker.model.TransactionType
import com.example.personalexpensetracker.viewmodel.BudgetViewModel
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    userId: String,
    navController: NavController,
    viewModel: TransactionViewModel = viewModel(),
    budgetViewModel: BudgetViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<TransactionType?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.getTransactionsByUser(userId)

        val now = Calendar.getInstance()
        budgetViewModel.loadBudgets(userId, now.get(Calendar.MONTH), now.get(Calendar.YEAR))
    }

    val expenseByCategory = viewModel.transactions
        .filter { it.type == TransactionType.EXPENSE }
        .groupBy { it.category.normalize() }
        .mapValues { it.value.sumOf { tx -> tx.amount } }

    val budgetWarnings = budgetViewModel.budgets.filter { budget ->
        val spent = expenseByCategory.entries.firstOrNull {
            it.key.normalize() == budget.category.normalize()
        }?.value ?: 0.0

        spent > budget.amount
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Xin chào!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Thông báo",
                        modifier = Modifier
                            .size(45.dp)
                            .padding(end = 12.dp)
                            .clickable { navController.navigate("NotificationScreen/$userId") },
                        tint = Color.Black
                    )
                }
            }

            Text(
                text = "Biến động số dư",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem("Tổng thu", viewModel.totalIncome, Color(0xFF2E7D32))
                SummaryItem("Chi tiêu", viewModel.totalExpense, Color(0xFFD32F2F))
                SummaryItem("Số dư", viewModel.balance, Color.Black)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hũ tích lũy",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { navController.navigate("SavingsScreen") }) {
                    Icon(
                        imageVector = Icons.Default.Savings,
                        contentDescription = "Tiết kiệm",
                        tint = Color.Black,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            if (budgetWarnings.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Cảnh báo ngân sách!", fontWeight = FontWeight.Bold, color = Color.Red)
                        Spacer(modifier = Modifier.height(6.dp))

                        for (budget in budgetWarnings) {
                            val spent = expenseByCategory.entries.firstOrNull {
                                it.key.normalize() == budget.category.normalize()
                            }?.value ?: 0.0

                            Text(
                                text = "• ${budget.category}: đã chi quá ngân sách của tháng này",
                                color = Color.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Tìm kiếm...") },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.White,
                    containerColor = Color(0xFFF0F0F0)
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lịch sử giao dịch",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                var expanded by remember { mutableStateOf(false) }

                Box {
                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = "Lọc",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                    ) {
                        DropdownMenuItem(
                            text = { Text("Tất cả") },
                            onClick = {
                                selectedType = null
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Thu nhập") },
                            onClick = {
                                selectedType = TransactionType.INCOME
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Chi tiêu") },
                            onClick = {
                                selectedType = TransactionType.EXPENSE
                                expanded = false
                            }
                        )
                    }
                }
            }

            val filteredTransactions = viewModel.transactions.filter { transaction ->
                (selectedType == null || transaction.type == selectedType) &&
                        (transaction.category.contains(searchQuery, ignoreCase = true) ||
                                transaction.note?.contains(searchQuery, ignoreCase = true) == true)
            }

            if (filteredTransactions.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Biến động", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (searchQuery.isEmpty()) "Chưa có dữ liệu" else "Không tìm thấy giao dịch",
                            color = Color.Gray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredTransactions) { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            viewModel = viewModel,
                            userId = userId
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
            Text("+", color = Color.White, fontSize = 20.sp)
        }
    }

    if (showDialog) {
        AddTransactionDialog(
            userId = userId,
            onDismiss = { showDialog = false },
            viewModel = viewModel,
            navController = navController
        )
    }
}

fun formatDate(dateMillis: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")
    }
    return dateFormat.format(Date(dateMillis))
}

@Composable
fun TransactionCard(
    transaction: Transaction,
    userId: String,
    viewModel: TransactionViewModel = viewModel()
) {
    val context = LocalContext.current
    val dateString = formatDate(transaction.date)
    val formattedAmount = String.format("%,.0f", transaction.amount)
    var showDialog by remember { mutableStateOf(false) }

    val bgColor = if (transaction.type == TransactionType.INCOME)
        Color(0xFFE8F5E9)
    else
        Color(0xFFFFEBEE)

    val amountColor = if (transaction.type == TransactionType.INCOME)
        Color(0xFF2E7D32)
    else
        Color(0xFFD32F2F)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { showDialog = true })
            }
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = bgColor),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = transaction.category,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Text(
                        text = if (transaction.type == TransactionType.INCOME)
                            "+ $formattedAmount VND"
                        else
                            "- $formattedAmount VND",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = amountColor
                    )
                    transaction.note?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Ghi chú: $it",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(dateString, fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = if (transaction.type == TransactionType.INCOME) "Thu nhập" else "Chi tiêu",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = amountColor
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Tuỳ chọn giao dịch", fontWeight = FontWeight.Bold) },
                text = { Text("Bạn muốn làm gì với giao dịch này?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text("Sửa")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        viewModel.deleteTransaction(
                            transaction.id,
                            onSuccess = {
                                Toast.makeText(context, "Đã xoá", Toast.LENGTH_SHORT).show()
                            },
                            onFailure = {
                                Toast.makeText(context, "Lỗi khi xoá", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }) {
                        Text("Xoá", color = Color.Red)
                    }
                }
            )
        }
    }
}
