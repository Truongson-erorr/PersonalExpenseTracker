package com.example.personalexpensetracker.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.personalexpensetracker.admin.AdminScreen
import com.example.personalexpensetracker.admin.StatisticCard
import com.example.personalexpensetracker.admin.StatisticsScreen
import com.example.personalexpensetracker.admin.UserManagement
import com.example.personalexpensetracker.view.EditProfileScreen
import com.example.personalexpensetracker.view.HelpScreen
import com.example.personalexpensetracker.view.HomeScreen
import com.example.personalexpensetracker.view.LoginScreen
import com.example.personalexpensetracker.view.NotificationScreen
import com.example.personalexpensetracker.view.RegisterScreen
import com.example.personalexpensetracker.view.SavingDetailScreen
import com.example.personalexpensetracker.view.SavingsScreen
import com.example.personalexpensetracker.viewmodel.BudgetViewModel
import com.example.personalexpensetracker.viewmodel.NotificationViewModel
import com.example.personalexpensetracker.viewmodel.SavingViewModel
import com.example.personalexpensetracker.viewmodel.TransactionViewModel
import com.example.personalexpensetracker.viewmodel.UserViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost

const val SAVING_DETAIL_SCREEN = "saving_detail"

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()
    val budgetViewModel: BudgetViewModel = viewModel()
    val transactionViewModel: TransactionViewModel = viewModel()
    val savingViewModel: SavingViewModel = viewModel()
    val notificationViewmodel: NotificationViewModel = viewModel()
    AnimatedNavHost(
        navController = navController,
        startDestination = "LoginScreen",
        enterTransition = { slideInHorizontally { it } + fadeIn() },
        exitTransition = { slideOutHorizontally { -it } + fadeOut() },
        popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
        popExitTransition = { slideOutHorizontally { it } + fadeOut() }
    ) {
        composable("RegisterScreen") {
            RegisterScreen(navController = navController)
        }
        composable("LoginScreen") {
            LoginScreen(navController = navController)
        }
        composable("HomeScreen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            HomeScreen(navController = navController, userId = userId)
        }
        composable("AdminScreen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            AdminScreen(navController = navController, userId = userId)
        }
        composable("SavingsScreen") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            SavingsScreen(navController = navController, savingViewModel , userId = userId)
        }
        composable("NotificationScreen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            NotificationScreen(navController = navController, userId = userId)
        }
        composable("UserManagement") {
            UserManagement(navController = navController, userViewModel)
        }
        composable("StatisticsScreen") {
            StatisticsScreen(navController = navController, userViewModel, transactionViewModel , budgetViewModel )
        }
        composable("edit_profile/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val user = userViewModel.getUserById(userId ?: "")
            user?.let {
                EditProfileScreen(
                    navController = navController,
                    user = it,
                    userViewModel = userViewModel
                )
            }
        }
        composable("HelpScreen") {
            HelpScreen(navController = navController)
        }
    }
}
