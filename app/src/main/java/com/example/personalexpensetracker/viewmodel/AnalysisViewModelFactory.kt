package com.example.personalexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.personalexpensetracker.viewmodel.AnalysisViewModel
import com.example.personalexpensetracker.viewmodel.TransactionViewModel

class AnalysisViewModelFactory(
    private val transactionViewModel: TransactionViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnalysisViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnalysisViewModel(transactionViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}