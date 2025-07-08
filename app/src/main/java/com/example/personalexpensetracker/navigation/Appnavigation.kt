package com.example.personalexpensetracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.personalexpensetracker.view.AddTransactionScreen
import com.example.personalexpensetracker.view.HomeScreen
import com.example.personalexpensetracker.view.LoginScreen
import com.example.personalexpensetracker.view.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "LoginScreen"
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
        composable("AddTransactionScreen/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            AddTransactionScreen(navController = navController, userId = userId)
        }

    }
}