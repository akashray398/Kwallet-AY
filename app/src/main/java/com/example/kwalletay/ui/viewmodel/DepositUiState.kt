package com.example.kwalletay.ui.viewmodel

import com.example.kwalletay.data.local.TransactionEntity

sealed class DepositUiState {
    object Idle : DepositUiState()
    object Loading : DepositUiState()
    data class Success(val transaction: TransactionEntity) : DepositUiState()
    data class Error(val message: String) : DepositUiState()
}
