package com.example.kwalletay.data.model

data class Referral(
    val id: String,
    val name: String,
    val date: String,
    val status: String, // e.g., "Completed", "Pending"
    val reward: Double
)
