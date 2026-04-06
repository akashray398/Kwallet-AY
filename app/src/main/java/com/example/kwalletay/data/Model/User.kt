package com.example.kwalletay.data.model

data class User(
    val uid: String = "",
    val email: String? = "",
    val displayName: String? = "",
    val balance: Double = 0.0,
    val phoneNumber: String? = "",
    val referralCode: String? = ""
)
