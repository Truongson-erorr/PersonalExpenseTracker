package com.example.personalexpensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensetracker.model.Loan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun LoanItem(
    loan: Loan,
    onPaid: (Loan) -> Unit,
    onDelete: (Loan) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val bgStatusColor = if (loan.paid) Color(0xFFE3FCE3) else Color(0xFFFFEBEE)

    val currentTime = System.currentTimeMillis()
    val daysLeft = ((loan.dueDate - currentTime) / (1000 * 60 * 60 * 24)).toInt()
    val dueText = when {
        daysLeft < 0 -> "Quá hạn ${-daysLeft} ngày"
        daysLeft == 0 -> "Hết hạn trong hôm nay"
        else -> "Còn $daysLeft ngày"
    }
    val dueColor = when {
        daysLeft < 0 -> Color.Red
        daysLeft <= 3 -> Color(0xFFFFA000)
        else -> Color.Gray
    }
    var showDeleteDialog = remember { mutableStateOf(false) }
    var showPaidDialog = remember { mutableStateOf(false) }

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = { Text("Xác nhận xoá") },
            text = { Text("Bạn có chắc chắn muốn xoá khoản vay này không?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(loan)
                    showDeleteDialog.value = false
                }) {
                    Text("Có")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog.value = false }) {
                    Text("Không")
                }
            }
        )
    }

    if (showPaidDialog.value) {
        AlertDialog(
            onDismissRequest = { showPaidDialog.value = false },
            title = { Text("Xác nhận thanh toán") },
            text = { Text("Bạn có muốn đánh dấu khoản vay này đã được trả không?") },
            confirmButton = {
                TextButton(onClick = {
                    onPaid(loan)
                    showPaidDialog.value = false
                }) {
                    Text("Có")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPaidDialog.value = false }) {
                    Text("Không")
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 2.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = loan.borrower,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = "%,.0f đ".format(loan.amount),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Event, contentDescription = "Hạn trả", tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Hạn trả: ${dateFormat.format(Date(loan.dueDate))}", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                dueText,
                fontSize = 14.sp,
                color = dueColor
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = "Lý do", tint = Color.Gray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Lý do: ${loan.reason}", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(
                            color = bgStatusColor,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (loan.paid) "Đã trả" else "Chưa trả",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (loan.paid) Color(0xFF2E7D32) else Color(0xFFD32F2F)
                    )
                }

                Row {
                    if (!loan.paid) {
                        IconButton(
                            onClick = { showPaidDialog.value = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.DoneAll,
                                contentDescription = "Đánh dấu đã thanh toán",
                                tint = Color.Black
                            )
                        }
                    }
                    IconButton(
                        onClick = { showDeleteDialog.value = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Xoá",
                            tint = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}
