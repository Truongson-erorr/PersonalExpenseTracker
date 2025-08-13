import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.personalexpensetracker.viewmodel.BudgetViewModel
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import com.example.personalexpensetracker.viewmodel.UserViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    transactionViewModel: TransactionViewModel = viewModel(),
    budgetViewModel: BudgetViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        transactionViewModel.getTotalTransactionCount()
        budgetViewModel.loadAllBudgets()
    }

    val users by userViewModel.userList.collectAsState()
    val totalUsers = users.size

    val totalTransactions by transactionViewModel.totalTransactionCount.collectAsState()
    val budgets = budgetViewModel.budgets
    val totalBudgets = budgets.size

    val items = listOf(
        Triple("Người dùng", "$totalUsers", Color(0xFFE1F5FE)),
        Triple("Giao dịch", "$totalTransactions", Color(0xFFFFF3E0)),
        Triple("Ngân sách", "$totalBudgets", Color(0xFFE8F5E9)),
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(40.dp))


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                StatisticCard(
                    title = item.first,
                    value = item.second,
                    backgroundColor = item.third
                )
            }
        }
    }
}

@Composable
fun StatisticCard(title: String, value: String, backgroundColor: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .size(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
