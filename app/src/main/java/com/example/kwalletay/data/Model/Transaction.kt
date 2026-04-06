package com.example.kwalletay.data.model

data class Transaction(
    val id: String = "",
    val userId: String = "",
    val amount: Double = 0.0,
    val type: String = "DEBIT", // "CREDIT" or "DEBIT"
    val title: String = "",
    val category: String = "General",
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUCCESS"
)
