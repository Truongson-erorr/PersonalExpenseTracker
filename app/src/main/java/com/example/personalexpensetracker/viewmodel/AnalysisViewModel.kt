package com.example.personalexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensetracker.model.Transaction
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnalysisViewModel(
    private val transactionViewModel: TransactionViewModel
) : ViewModel() {

    private val _analysisResult = MutableStateFlow("")
    val analysisResult: StateFlow<String> = _analysisResult

    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "AIzaSyBlQlyzrqhS_QZVLqPAwU8ppZyPUMKeTIA"
    )

    fun analyzeTransactions(transactions: List<Transaction>) {
        viewModelScope.launch {
            if (transactions.isEmpty()) {
                _analysisResult.value = "Chưa có dữ liệu giao dịch để phân tích."
                return@launch
            }

            val input = buildString {
                append("Dưới đây là dữ liệu chi tiêu của tôi:\n")
                transactions.forEach {
                    append("Ngày: ${it.date}, Loại: ${it.type}, Danh mục: ${it.category}, Số tiền: ${it.amount}\n")
                }
                append("\nTổng thu nhập: ${transactionViewModel.totalIncome}")
                append("\nTổng chi tiêu: ${transactionViewModel.totalExpense}")
                append("\nSố dư: ${transactionViewModel.balance}")
                append("\nHãy phân tích thói quen chi tiêu và dự đoán chi tiêu tháng tới theo từng danh mục.")
            }

            try {
                val response = model.generateContent(input)
                _analysisResult.value = response.text ?: "Không có dữ liệu phân tích từ AI."
            } catch (e: Exception) {
                _analysisResult.value = "Lỗi khi gọi API: ${e.message}"
            }
        }
    }

}
