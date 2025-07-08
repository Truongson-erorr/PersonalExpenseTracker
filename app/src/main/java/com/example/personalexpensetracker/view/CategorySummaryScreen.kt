package com.example.personalexpensetracker.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalexpensetracker.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySummaryScreen(
    userId: String,
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    val categoryData = remember { mutableStateMapOf<TransactionType, Map<String, Double>>() }
    val isLoading = remember { mutableStateOf(true) }
    val searchQuery = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getTransactionsByUser(userId)
        categoryData.clear()
        categoryData.putAll(viewModel.getTransactionsByCategoryGroupedByType())
        isLoading.value = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Thống kê theo danh mục",
                        fontSize = 20.sp,
                        color = Color(0xFF1976D2),
                        fontWeight = FontWeight.Bold
                    ) },
                colors = TopAppBarDefaults.mediumTopAppBarColors()
            )
        }

    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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

            if (isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.padding(top = 80.dp, start = 16.dp, end = 16.dp)) {
                    categoryData.forEach { (type, map) ->
                        item {
                            Text(
                                text = if (type == TransactionType.INCOME) "Danh mục Thu" else "Danh mục Chi",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }
                        map.forEach { (category, total) ->
                            item {
                                CategoryItem(
                                    category = category,
                                    total = total,
                                    isIncome = type == TransactionType.INCOME
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: String, total: Double, isIncome: Boolean) {
    val backgroundColor = if (isIncome) Color(0xFFD0F0C0) else Color(0xFFFFE0E0)
    val icon = if (isIncome) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward
    val iconColor = if (isIncome) Color(0xFF2E7D32) else Color(0xFFC62828)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = category,
                fontSize = 12.sp,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${"%,.0f".format(total)} đ",
                fontSize = 12.sp,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = iconColor
            )
        }
    }
}
