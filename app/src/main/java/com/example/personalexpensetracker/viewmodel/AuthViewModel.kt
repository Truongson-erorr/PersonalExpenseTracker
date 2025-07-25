package com.example.personalexpensetracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensetracker.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _userList = MutableStateFlow<List<Users>>(emptyList())
    val userList: StateFlow<List<Users>> = _userList

    init {
        fetchUsers()
    }

    fun createUser(
        ten: String,
        email: String,
        role: String = "user",
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

    fun fetchUsers() {
        firestore.collection("Users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("UserViewModel", "Lỗi khi đọc danh sách: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val users = snapshot.documents.mapNotNull { it.toObject(Users::class.java) }
                    _userList.value = users
                }
            }
    }

    fun deleteUser(userId: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        firestore.collection("Users")
            .document(userId)
            .delete()
            .addOnSuccessListener {
                Log.d("UserViewModel", "Xoá người dùng thành công")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("UserViewModel", "Lỗi khi xoá: ${e.message}")
                onError("Xoá thất bại: ${e.message}")
            }
    }

    fun updateUser(user: Users, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        firestore.collection("Users")
            .document(user.userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("UserViewModel", "Cập nhật người dùng thành công")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("UserViewModel", "Lỗi khi cập nhật: ${e.message}")
                onError("Cập nhật thất bại: ${e.message}")
            }
    }
}
