package com.example.personalexpensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.Transaction
import com.example.personalexpensetracker.model.TransactionType
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    userId: String,
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf<TransactionType?>(null) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.getTransactionsByUser(userId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    text = "Xin chào, Sơn!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )

                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar người dùng",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    tint = Color.DarkGray
                )
            }

            Text(
                text = "Biến động số dư",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem("Tổng thu", viewModel.totalIncome, Color(0xFF2E7D32))
                SummaryItem("Chi tiêu", viewModel.totalExpense, Color(0xFFD32F2F))
                SummaryItem("Số dư", viewModel.balance, Color(0xFF1976D2))
            }

            Text(
                text = "Hoạt động hôm nay",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

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

                Box {
                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Lọc",
                            tint = Color(0xFF1976D2),
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
                        TransactionCard(transaction)
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate("AddTransactionScreen/$userId")
            },
            containerColor = Color(0xFF1976D2),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+", color = Color.White, fontSize = 20.sp)
        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction) {
    val bgColor = if (transaction.type == TransactionType.INCOME) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateString = dateFormat.format(Date(transaction.date))
    val formattedAmount = String.format("%,.0f", transaction.amount)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = transaction.category, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                    text = if (transaction.type == TransactionType.INCOME) "+ $formattedAmount VND"
                    else "- $formattedAmount VND",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                transaction.note?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Ghi chú: $it", fontSize = 11.sp, color = Color.Gray)
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = dateString, fontSize = 11.sp, color = Color.Gray)
                Text(
                    text = if (transaction.type == TransactionType.INCOME) "Thu nhập" else "Chi tiêu",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (transaction.type == TransactionType.INCOME) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                )
            }
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
