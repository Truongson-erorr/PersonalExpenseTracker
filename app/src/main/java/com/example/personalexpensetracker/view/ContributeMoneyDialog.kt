package com.example.personalexpensetracker.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensetracker.model.Saving
import java.util.Date
import java.util.Locale
import kotlin.math.abs

val pastelColors = listOf(
    Color(0xFFFFCDD2),
    Color(0xFFF8BBD0),
    Color(0xFFE1BEE7),
    Color(0xFFD1C4E9),
    Color(0xFFC5CAE9),
    Color(0xFFBBDEFB),
    Color(0xFFB2EBF2),
    Color(0xFFC8E6C9),
    Color(0xFFFFE0B2),
    Color(0xFFFFCCBC)
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
        pastelColors[abs(saving.id.hashCode()) % pastelColors.size]
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Savings,
                    contentDescription = "Saving Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color.Black.copy(alpha = 0.2f), CircleShape)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = saving.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = formatTimestamp(saving.timestamp),
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }

                Text(
                    text = if (expanded) "Ẩn" else "Xem",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    val goal = saving.goalAmount.takeIf { it != 0.0 } ?: 1.0
                    val progress = (saving.amount / goal).coerceIn(0.0, 1.0)
                    val percent = (progress * 100).toInt()

                    LinearProgressIndicator(
                        progress = progress.toFloat(),
                        color = Color.DarkGray,
                        trackColor = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Đã tiết kiệm: ${"%,.0f".format(saving.amount)}đ / ${"%,.0f".format(goal)}đ ($percent%)",
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    if (saving.note.isNotBlank()) {
                        Text(
                            "Ghi chú: ${saving.note}",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!saving.completed && saving.amount >= goal) {
                            Button(
                                onClick = { onComplete(saving) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = "Hoàn thành", tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Hoàn thành", color = Color.White)
                            }
                        } else if (saving.completed) {
                            Text(
                                "Đã hoàn thành",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF388E3C)
                            )
                        }

                        if (!saving.completed) {
                            OutlinedButton(
                                onClick = { onAddMoney(saving) },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.Black)
                            ) {
                                Icon(Icons.Default.AddCircle, contentDescription = "Góp thêm", tint = Color.Black)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Góp thêm", color = Color.Black)
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

