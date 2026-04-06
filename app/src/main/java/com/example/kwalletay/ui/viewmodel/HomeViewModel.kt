package com.example.kwalletay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kwalletay.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            combine(
                repository.getBalance(),
                repository.getAllTransactions()
            ) { balance, transactions ->
                HomeUiState(
                    isLoading = false,
                    isRefreshing = false,
                    balance = "₹${String.format("%.2f", balance ?: 0.0)}",
                    transactions = transactions,
                    errorMessage = null
                )
            }
            .onStart { 
                if (!_uiState.value.isRefreshing) {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
            }
            .catch { e ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = e.message ?: "An unknown error occurred"
                    ) 
                }
            }
            .collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
        loadHomeData()
    }
}
