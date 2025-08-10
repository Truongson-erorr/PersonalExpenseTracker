package com.example.personalexpensetracker.view

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.personalexpensetracker.model.Transaction
import com.example.personalexpensetracker.model.TransactionType
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
