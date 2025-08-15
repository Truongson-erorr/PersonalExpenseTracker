package com.example.personalexpensetracker.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.personalexpensetracker.model.Message

class ChatViewModel : ViewModel() {
    val messages = mutableStateListOf<Message>()

    fun sendMessage(userText: String) {
        messages.add(Message(text = userText, isUser = true))

        val reply = getBotResponse(userText)
        messages.add(Message(text = reply, isUser = false))
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
