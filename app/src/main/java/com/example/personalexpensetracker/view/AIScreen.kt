package com.example.personalexpensetracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.example.personalexpensetracker.viewmodel.AnalysisViewModel
import com.example.personalexpensetracker.viewmodel.AnalysisViewModelFactory
import com.example.personalexpensetracker.viewmodel.TransactionViewModel

@Composable
fun AnalysisScreen(
    userId: String,
    navController: NavController,
    transactionViewModel: TransactionViewModel = viewModel()
) {
    val analysisViewModel: AnalysisViewModel = viewModel(
        factory = AnalysisViewModelFactory(transactionViewModel)
    )

    var isLoading by remember { mutableStateOf(false) }
    val result by analysisViewModel.analysisResult.collectAsState()

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            transactionViewModel.getTransactionsByUser(userId)
        }
    }

    val transactions = transactionViewModel.transactions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Dự đoán chi tiêu",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "👋 Xin chào! Đây là trợ lý AI giúp bạn phân tích thói quen chi tiêu. " +
                    "Bạn chỉ cần nhấn nút bên dưới, AI sẽ xem lại dữ liệu giao dịch và dự đoán chi tiêu tháng tới. " +
                    "Hãy kéo xuống để xem kết quả chi tiết nhé!",
            fontSize = 15.sp,
            color = Color.DarkGray,
            lineHeight = 20.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    isLoading = true
                    analysisViewModel.analyzeTransactions(transactions)
                },
                enabled = transactions.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Phân tích ngay")
            }
        }

        if (isLoading && result.isEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }

        if (result.isNotEmpty()) {
            Text(
                text = result,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (!isLoading && transactions.isEmpty()) {
            Text(
                text = "Dữ liệu giao dịch đang được tải hoặc chưa có.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
