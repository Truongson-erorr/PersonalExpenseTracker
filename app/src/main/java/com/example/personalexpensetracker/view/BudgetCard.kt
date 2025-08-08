package com.example.personalexpensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personalexpensetracker.model.Budget


@Composable
fun BudgetCard(budget: Budget, onClick: () -> Unit) {
    val colors = listOf(
        Color(0xFFFFF9C4),
        Color(0xFFBBDEFB),
        Color(0xFFC8E6C9),
        Color(0xFFFFCDD2),
        Color(0xFFD1C4E9),
        Color(0xFFFFE0B2)
    )

    val backgroundColor = remember { colors.random() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = budget.category,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Hạn mức: ${String.format("%,.0f", budget.amount)} VND",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Text(
                    text = "Tháng ${budget.month}/${budget.year}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                tint = Color(0xFF6200EA),
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    color: Color = Color.Black
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            shape = RoundedCornerShape(12.dp),
            label = { Text(label) },
            readOnly = true,
            enabled = false,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .width(120.dp)
                .height(55.dp)
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(12.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = { Text(option) }
                )
            }
        }
    }
}
