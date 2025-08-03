package com.example.personalexpensetracker.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensetracker.model.Saving
import java.util.Date
import java.util.Locale
import kotlin.math.abs

val vibrantColors = listOf(
    Color(0xFFF44336),
    Color(0xFFE91E63),
    Color(0xFF9C27B0),
    Color(0xFF673AB7),
    Color(0xFF3F51B5),
    Color(0xFF2196F3),
    Color(0xFF03A9F4),
    Color(0xFF009688),
    Color(0xFFFF9800),
    Color(0xFFFF5722)
)

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val format = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}

@Composable
fun SavingItem(
    saving: Saving,
    onComplete: (Saving) -> Unit = {},
    onAddMoney: (Saving) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    val backgroundColor = remember(saving.id) {
        vibrantColors[abs(saving.id.hashCode()) % vibrantColors.size]
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = saving.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${formatTimestamp(saving.timestamp)}",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = if (expanded) "Ẩn" else "Xem",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))

                    val goal = saving.goalAmount.takeIf { it != 0.0 } ?: 1.0
                    val progress = (saving.amount / goal).coerceIn(0.0, 1.0)
                    val percent = (progress * 100).toInt()

                    LinearProgressIndicator(
                        progress = progress.toFloat(),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Đã tiết kiệm: ${"%,.0f".format(saving.amount)}đ / ${"%,.0f".format(goal)}đ ($percent%)",
                        color = Color.White
                    )

                    if (saving.note.isNotBlank()) {
                        Text("Ghi chú: ${saving.note}", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Row chứa "Đánh dấu hoàn thành" và nút "Góp thêm"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (!saving.completed && saving.amount >= goal) {
                            Button(
                                onClick = { onComplete(saving) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            ) {
                                Text("Đánh dấu hoàn thành", color = backgroundColor)
                            }
                        } else if (saving.completed) {
                            Text(
                                "Đã hoàn thành",
                                fontWeight = FontWeight.Bold,
                                color = Color.Yellow,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }

                        if (!saving.completed) {
                            TextButton(
                                onClick = { onAddMoney(saving) },
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "Góp thêm",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Góp thêm", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContributeMoneyDialog(
    saving: Saving,
    onDismiss: () -> Unit,
    onContribute: (Double) -> Unit
) {
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val value = amount.toDoubleOrNull()
                if (value != null && value > 0) {
                    onContribute(value)
                    onDismiss()
                }
            }) {
                Text("Xác nhận")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        },
        title = { Text("Góp thêm vào hũ") },
        text = {
            Column {
                Text("Nhập số tiền muốn góp vào hũ \"${saving.title}\"")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    shape = RoundedCornerShape(16.dp),
                    label = { Text("Số tiền") }
                )
            }
        }
    )
}

