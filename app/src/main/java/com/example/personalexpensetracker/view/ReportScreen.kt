package com.example.personalexpensetracker.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.TransactionType
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    userId: String,
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(7) }

    val monthlySummary = viewModel.getMonthlySummary()
    val expenseByCategory = viewModel.getExpenseByCategory(selectedMonth)

    val selectedMonthSummary = viewModel.filterByMonthYear(
        selectedMonth - 1,
        Calendar.getInstance().get(Calendar.YEAR)
    )
    val income = selectedMonthSummary.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
    val expense = selectedMonthSummary.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    val balance = income - expense
    var expandedDetail by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                "Báo cáo tài chính",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Tổng quan chi tiêu của bạn",
                fontSize = 14.sp,
                color = Color.Gray.copy(alpha = 0.8f)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tổng kết tháng", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Số dư",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                "${"%,.0f".format(balance)} VND",
                                color = Color.Black,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        TextButton(onClick = { expandedDetail = !expandedDetail }) {
                            Text(if (expandedDetail) "Ẩn" else "Chi tiết", color = Color.Black)
                            Icon(
                                imageVector = if (expandedDetail) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    }

                    AnimatedVisibility(visible = expandedDetail) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Thu nhập", fontSize = 14.sp, color = Color.Gray)
                                    Text(
                                        "+${"%,.0f".format(income)} VND",
                                        color = Color(0xFF4CAF50),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Column {
                                    Text("Chi tiêu", fontSize = 14.sp, color = Color.Gray)
                                    Text(
                                        "-${"%,.0f".format(expense)} VND",
                                        color = Color(0xFFF44336),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Thống kê theo tháng: $selectedMonth",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = "Chọn tháng",
                            tint = Color.Black
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        (1..12).forEach { month ->
                            DropdownMenuItem(
                                text = { Text("Tháng $month") },
                                onClick = {
                                    selectedMonth = month
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CategoryPieChart(expenseByCategory)
                    Spacer(modifier = Modifier.height(16.dp))
                    CategoryLegend(expenseByCategory)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                "Biểu đồ thu chi theo tháng",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MonthlyBarChart(monthlySummary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        LegendItem(Color(0xFF4CAF50), "Thu nhập")
                        LegendItem(Color(0xFFF44336), "Chi tiêu")
                    }
                }
            }
        }
    }
}
