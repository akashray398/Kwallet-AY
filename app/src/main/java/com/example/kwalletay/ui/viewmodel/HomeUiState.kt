package com.example.kwalletay.ui.viewmodel

import com.example.kwalletay.data.model.Transaction

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val balance: String = "₹0.00",
    val transactions: List<Transaction> = emptyList(),
    val errorMessage: String? = null
)
