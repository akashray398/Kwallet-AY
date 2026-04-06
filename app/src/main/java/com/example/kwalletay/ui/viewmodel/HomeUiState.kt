package com.example.kwalletay.ui.viewmodel

import com.example.kwalletay.data.local.TransactionEntity

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val balance: String = "₹0.00",
    val transactions: List<TransactionEntity> = emptyList(),
    val errorMessage: String? = null
)
