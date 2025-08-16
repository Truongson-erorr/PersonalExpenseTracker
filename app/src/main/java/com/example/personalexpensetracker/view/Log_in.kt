package com.example.personalexpensetracker.view

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.personalexpensetracker.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var isLoading by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var captchaInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val captchaCode = remember { mutableStateOf(generateCaptcha()) }

    fun resetCaptcha() {
        captchaCode.value = generateCaptcha()
        captchaInput = ""
    }

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            auth.signInWithCredential(credential)
                .addOnCompleteListener { signInTask ->
                    if (signInTask.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        Toast.makeText(context, "Đăng nhập Google thành công!", Toast.LENGTH_SHORT).show()
                        navController.navigate("HomeScreen/$userId") {
                            popUpTo("LoginScreen") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Savings,
            contentDescription = "Piggy Bank",
            tint = Color(0xFFFFB300),
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 12.dp)
        )

        Text(
            "Đăng nhập",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFB300)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Quản lí chi tiêu thông minh",
            fontSize = 13.sp,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email", fontSize =12.sp) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFBBDEFB), shape = RoundedCornerShape(16.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFEEEEEE),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Mật khẩu", fontSize =12.sp) },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFEEEEEE),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Mã xác nhận: ", fontSize = 12.sp)
            Text(
                text = captchaCode.value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFB300),
                modifier = Modifier
                    .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Tạo lại mã",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { resetCaptcha() },
                tint = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        TextField(
            value = captchaInput,
            onValueChange = { captchaInput = it },
            placeholder = { Text("Nhập mã xác nhận", fontSize = 12.sp) },
            leadingIcon = { Icon(imageVector = Icons.Default.Security, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp)),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFEEEEEE),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                errorMessage = ""
                if (email.isBlank() || password.isBlank() || captchaInput.isBlank()) {
                    errorMessage = "Vui lòng điền đầy đủ thông tin"
                    return@Button
                }

                if (!captchaInput.equals(captchaCode.value, ignoreCase = true)) {
                    errorMessage = "Mã xác nhận không đúng"
                    resetCaptcha()
                    return@Button
                }

                isLoading = true
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            val userId = FirebaseAuth.getInstance().currentUser?.uid
                            if (userId != null) {
                                val db = FirebaseFirestore.getInstance()
                                db.collection("Users").document(userId).get()
                                    .addOnSuccessListener { document ->
                                        val role = document.getString("role") ?: "user"
                                        Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                                        if (role == "admin") {
                                            navController.navigate("AdminScreen/$userId") {
                                                popUpTo("LoginScreen") { inclusive = true }
                                            }
                                        } else {
                                            navController.navigate("HomeScreen/$userId") {
                                                popUpTo("LoginScreen") { inclusive = true }
                                            }
                                        }
                                    }
                                    .addOnFailureListener {
                                        errorMessage = "Lỗi khi lấy role: ${it.message}"
                                    }
                            } else {
                                errorMessage = "Không lấy được thông tin người dùng"
                            }

                        } else {
                            errorMessage = "Đăng nhập thất bại: ${task.exception?.message}"
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(1.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFB300),
                contentColor = Color.White
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "Đăng nhập",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    navController.navigate("ForgotPasswordScreen")
                }
            ) {
                Text(
                    text = "Quên mật khẩu?",
                    fontSize = 12.sp,
                    color = Color(0xFFFFB300)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Đã chưa có tài khoản? ",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Đăng ký",
                color = Color(0xFFFFB300),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    navController.navigate("RegisterScreen")
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                }
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(vertical = 10.dp, horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Đăng nhập với Google",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

    }
}

fun generateCaptcha(): String {
    val chars = ('A'..'Z') + ('0'..'9')
    return (1..5).map { chars.random() }.joinToString("")
}
