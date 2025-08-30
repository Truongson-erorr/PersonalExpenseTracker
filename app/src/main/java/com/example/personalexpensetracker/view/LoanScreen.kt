package com.example.personalexpensetracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.Loan
import com.example.personalexpensetracker.viewmodel.LoanViewModel

@Composable
fun LoanScreen(
    navController: NavController,
    viewModel: LoanViewModel = viewModel(),
    userId: String,
) {
    var showDialog by remember { mutableStateOf(false) }
    val loans by viewModel.loans.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadLoans()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Quản Lý Khoản Vay / Nợ",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            val tabs = listOf("Khoản Cho Vay", "Khoản Nợ")
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.Bold) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            val filteredLoans = when (selectedTab) {
                0 -> loans.filter { !it.isDebt }
                1 -> loans.filter { it.isDebt }
                else -> loans
            }

            if (filteredLoans.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (selectedTab == 0) "Chưa có khoản cho vay nào"
                        else "Chưa có khoản nợ nào"
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filteredLoans) { loan ->
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
            userId = userId,
            onDismiss = { showDialog = false },
            onAdd = { loan ->
                viewModel.addLoan(loan)
                showDialog = false
            }
        )
    }
}

