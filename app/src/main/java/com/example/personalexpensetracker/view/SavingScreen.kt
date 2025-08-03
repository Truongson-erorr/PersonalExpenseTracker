package com.example.personalexpensetracker.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterList
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
import com.example.personalexpensetracker.model.Saving
import com.example.personalexpensetracker.model.SavingFilter
import com.example.personalexpensetracker.viewmodel.SavingViewModel
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@Composable
fun SavingsScreen(
    navController: NavController,
    viewModel: SavingViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    userId: String
) {
    var selectedSaving by remember { mutableStateOf<Saving?>(null) }
    var showContributeDialog by remember { mutableStateOf(false) }

    val savings = viewModel.savings.collectAsState().value
    var filter by remember { mutableStateOf(SavingFilter.ALL) }
    var expandedFilter by remember { mutableStateOf(false) }

    val filteredSavings = when (filter) {
        SavingFilter.ALL -> savings
        SavingFilter.COMPLETED -> savings.filter { it.completed }
        SavingFilter.INCOMPLETE -> savings.filter { !it.completed }
    }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadSavings()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Hũ Tiết Kiệm",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }

            Box {
                IconButton(onClick = { expandedFilter = true }) {
                    Icon(
                        imageVector = Icons.Default.FilterAlt,
                        contentDescription = "Lọc"
                    )
                }

                DropdownMenu(
                    expanded = expandedFilter,
                    onDismissRequest = { expandedFilter = false },
                    modifier = Modifier
                        .background(Color.White)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    DropdownMenuItem(
                        text = { Text("Tất cả", color = Color.Black) },
                        onClick = {
                            filter = SavingFilter.ALL
                            expandedFilter = false
                        },
                        colors = MenuDefaults.itemColors()
                    )
                    DropdownMenuItem(
                        text = { Text("Đã hoàn thành", color = Color.Black) },
                        onClick = {
                            filter = SavingFilter.COMPLETED
                            expandedFilter = false
                        },
                        colors = MenuDefaults.itemColors()
                    )
                    DropdownMenuItem(
                        text = { Text("Chưa hoàn thành", color = Color.Black) },
                        onClick = {
                            filter = SavingFilter.INCOMPLETE
                            expandedFilter = false
                        },
                        colors = MenuDefaults.itemColors()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val total = savings.sumOf { it.amount }
                Text(
                    "Tổng số tiền đã tiết kiệm",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${"%,.0f".format(total)}đ",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredSavings) { saving ->
                SavingItem(
                    saving = saving,
                    onComplete = {
                        viewModel.updateSaving(it.copy(completed = true))
                    },
                    onAddMoney = {
                        selectedSaving = it
                        showContributeDialog = true
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = Color.Black,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add,
                contentDescription = "Thêm",
                tint = Color.White
            )
        }
    }

    if (showDialog) {
        AddSavingDialog(
            onDismiss = { showDialog = false },
            onAdd = { saving ->
                viewModel.addSaving(saving)
                showDialog = false
            }
        )
    }

    if (showContributeDialog && selectedSaving != null) {
        ContributeMoneyDialog(
            saving = selectedSaving!!,
            onDismiss = {
                showContributeDialog = false
                selectedSaving = null
            },
            onContribute = { amount ->
                val updated = selectedSaving!!.copy(amount = selectedSaving!!.amount + amount)
                viewModel.updateSaving(updated)
            }
        )
    }
}
