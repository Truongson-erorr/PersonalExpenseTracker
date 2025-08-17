package com.example.personalexpensetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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
        userId?.let {
            FirebaseFirestore.getInstance().collection("Users").document(it)
                .get()
                .addOnSuccessListener { document ->
                    user = document.toObject(Users::class.java)
                }
                .addOnFailureListener { e -> error = e.localizedMessage }
        }
    }

    when {
        error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Lỗi: $error", color = Color.Red)
        }

        user == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        else -> LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { ProfileHeader(user!!, navController) }
            item { ProfileMenu(navController) }
        }
    }
}

@Composable
fun ProfileHeader(user: Users, navController: NavController) {
    Spacer(modifier = Modifier.height(50.dp))
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray, CircleShape)
                        .padding(16.dp),
                    tint = Color.White
                )
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .background(Color.White, CircleShape)
                        .padding(2.dp),
                    tint = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(user.ten, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(user.email, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        navController.navigate("edit_profile/${user.userId}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Chỉnh sửa", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun ProfileMenu(navController: NavController) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("Tiếng Việt") }

    val items = listOf(
        Triple("Ngôn ngữ", Icons.Default.Language) {
            showLanguageDialog = true
        },
        Triple("Vị trí", Icons.Default.LocationOn) {  },
        Triple("Tùy chỉnh giao diện", Icons.Default.Bedtime) { },
        Triple("Đăng xuất", Icons.Default.ExitToApp) {
            showLogoutDialog = true
        }
    )

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text("Chọn ngôn ngữ") },
            text = {
                Column {
                    LanguageOption("Tiếng Việt", selectedLanguage) {
                        selectedLanguage = "Tiếng Việt"
                        showLanguageDialog = false
                    }
                    Spacer(Modifier.height(8.dp))
                    LanguageOption("English", selectedLanguage) {
                        selectedLanguage = "English"
                        showLanguageDialog = false
                    }
                }
            },
            confirmButton = {},
            dismissButton = {},
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Xác nhận đăng xuất") },
            text = { Text("Bạn có chắc chắn muốn đăng xuất không?") },
            confirmButton = {
                TextButton(onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("LoginScreen") {
                        popUpTo("HomeScreen/{userId}") { inclusive = true }
                    }
                    showLogoutDialog = false
                }) {
                    Text("Đăng xuất")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                }) {
                    Text("Huỷ")
                }
            }
        )
    }
    Spacer(Modifier.height(20.dp))

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column {
                items.forEach { (title, icon, action) ->
                    ListItem(
                        headlineContent = { Text(title) },
                        leadingContent = {
                            Icon(
                                icon,
                                contentDescription = null,
                                tint = Color(0xFFFFB300)
                            )
                        },
                        trailingContent = { Icon(Icons.Default.KeyboardArrowRight, null) },
                        modifier = Modifier.clickable { action() }
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageOption(language: String, selectedLanguage: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = language == selectedLanguage,
            onClick = onClick
        )
        Text(language, modifier = Modifier.padding(start = 8.dp))
    }
}