package com.example.personalexpensetracker.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.personalexpensetracker.model.Users
import com.example.personalexpensetracker.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var user by remember { mutableStateOf<Users?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        if (userId != null) {
            FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        user = document.toObject(Users::class.java)
                    } else {
                        error = "Không tìm thấy người dùng."
                    }
                }
                .addOnFailureListener { e ->
                    error = "Lỗi: ${e.message}"
                }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF5F5F5)) {
        if (error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(error ?: "", color = Color.Red)
            }
        } else if (user == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(50.dp))

                    // Avatar
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .background(Color.Gray, shape = CircleShape)
                                .padding(16.dp),
                            tint = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change Avatar",
                            modifier = Modifier
                                .offset(y = (-8).dp, x = (-8).dp)
                                .size(28.dp)
                                .background(Color.White, CircleShape)
                                .padding(4.dp),
                            tint = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Name and Email
                    Text(
                        user!!.ten,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        user!!.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Edit Profile Button
                    Button(
                        onClick = { navController.navigate("Chỉnh sua") },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2DBE60))
                    ) {
                        Text("Chỉnh sửa hồ sơ")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                val menuItems = listOf(
                    Pair("Ngôn ngữ", Icons.Default.Language),
                    Pair("Vị trí", Icons.Default.LocationOn),
                    Pair("Tùy chỉnh giao diện", Icons.Default.Bedtime),
                )

                items(menuItems.size) { index ->
                    MenuItem(title = menuItems[index].first, icon = menuItems[index].second)
                }

                item {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }

                val systemItems = listOf(
                    Pair("Xóa bộ nhớ đệm", Icons.Default.Delete),
                    Pair("Xóa lịch sử", Icons.Default.History)
                )

                items(systemItems.size) { index ->
                    MenuItem(title = systemItems[index].first, icon = systemItems[index].second)
                }

                item {
                    MenuItem(
                        title = "Log Out",
                        icon = Icons.Default.ExitToApp,
                        iconTint = Color.Red,
                        textColor = Color.Red
                    ) {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo("profile") { inclusive = true }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    title: String,
    icon: ImageVector,
    iconTint: Color = Color.Black,
    textColor: Color = Color.Black,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, color = textColor, fontSize = 16.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}
