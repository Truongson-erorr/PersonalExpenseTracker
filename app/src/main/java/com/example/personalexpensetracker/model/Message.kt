package com.example.personalexpensetracker.model

data class Message(
    val id: String = System.currentTimeMillis().toString(),
    val text: String,
    val isUser: Boolean
)