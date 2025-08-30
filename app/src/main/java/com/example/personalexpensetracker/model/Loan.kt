package com.example.personalexpensetracker.model

data class Loan(
    val id: String = "",
    val userId: String = "",
    val borrower: String = "",
    val amount: Double = 0.0,
    val reason: String = "",
    val dueDate: Long = 0L,
    val paid: Boolean = false,
    val isDebt: Boolean
)