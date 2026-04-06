package com.example.kwalletay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kwalletay.data.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val auth = FirebaseAuth.getInstance()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        val userId = auth.currentUser?.uid ?: return
        
        viewModelScope.launch {
            combine(
                repository.getUserBalance(userId),
                repository.getTransactions(userId)
            ) { balance, transactions ->
                HomeUiState(
                    isLoading = false,
                    isRefreshing = false,
                    balance = "₹${String.format("%.2f", balance)}",
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
