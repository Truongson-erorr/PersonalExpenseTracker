package com.example.personalexpensetracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensetracker.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun createUser(
        ten: String,
        email: String,
        role: String = "user", // mặc định là user
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onError("Không thể lấy UID của người dùng.")
            return
        }

        val user = Users(
            userId = userId,
            ten = ten,
            email = email,
            role = role
        )

        viewModelScope.launch {
            firestore.collection("Users")
                .document(userId)
                .set(user)
                .addOnSuccessListener {
                    Log.d("UserViewModel", "Tạo tài khoản thành công")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.e("UserViewModel", "Lỗi: ${e.message}")
                    onError("Lỗi khi lưu thông tin người dùng: ${e.message}")
                }
        }
    }
}
