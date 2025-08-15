package com.example.personalexpensetracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensetracker.model.Message
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    val messages = mutableStateListOf<Message>()

    fun sendMessage(userText: String) {
        messages.add(Message(text = userText, isUser = true))

        val typingMessage = Message(text = "...", isUser = false)
        messages.add(typingMessage)

        viewModelScope.launch {
            delay(2500)
            messages.remove(typingMessage)
            messages.add(Message(text = getBotResponse(userText), isUser = false))
        }
    }

    private fun getBotResponse(input: String): String {
        return when {
            input.contains("xin chào", ignoreCase = true) -> "Xin chào! Tôi có thể giúp gì cho bạn?"
            input.contains("thêm giao dịch", ignoreCase = true) -> "Bạn có thể nhấn nút ➕ trên màn hình chính để thêm giao dịch mới."
            input.contains("ngân sách", ignoreCase = true) -> "Bạn có thể tạo ngân sách theo danh mục và nhận cảnh báo khi vượt ngưỡng."
            input.contains("báo cáo", ignoreCase = true) -> "Báo cáo chi tiêu hiển thị thống kê theo ngày, tháng và danh mục."
            else -> "Xin lỗi, tôi chưa hiểu câu hỏi. Bạn có thể thử lại bằng cách khác."
        }
    }
}
