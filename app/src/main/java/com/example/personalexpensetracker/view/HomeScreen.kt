package com.example.personalexpensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.NavItem
import com.example.personalexpensetracker.viewmodel.TransactionViewModel

@Composable
fun HomeScreen(
    userId: String = "",
    navController: NavController,
) {
    var selectedTab by remember { mutableStateOf(0) }

    val navItems = listOf(
        NavItem("Giao dịch", Icons.Default.AttachMoney),
        NavItem("Ngân sách", Icons.Default.Wallet),
        NavItem("Báo cáo", Icons.Default.Report),
        NavItem("Cá nhân", Icons.Default.Person)
    )

    val transactionViewModel: TransactionViewModel = viewModel()
    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(30.dp))
                    .clip(RoundedCornerShape(30.dp))
            ) {
                BottomNavigation(
                    backgroundColor = Color.Black,
                    contentColor = Color.White,
                    modifier = Modifier.height(60.dp),
                    elevation = 0.dp
                ) {
                    navItems.forEachIndexed { index, item ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(text = item.label)
                            },
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            selectedContentColor = Color.White,
                            unselectedContentColor = Color.White.copy(alpha = 0.7f),
                            alwaysShowLabel = false
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            when (selectedTab) {
                0 -> HomeContent(userId, navController)
                1 -> BudgetScreen(userId, navController)
                2 -> ReportScreen(userId, navController, transactionViewModel)
                3 -> ProfileScreen(navController)
            }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = if (selected) Color(0xFFFFB300) else Color.White

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        if (selected) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = Color(0xFFFFB300),
            )
        }
    }
}
