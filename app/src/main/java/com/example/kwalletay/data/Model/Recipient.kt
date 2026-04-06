package com.example.kwalletay.data.model

data class Recipient(
    val id: String,
    val name: String,
    val identifier: String, // Phone or UPI ID
    val avatarUrl: String? = null
)
