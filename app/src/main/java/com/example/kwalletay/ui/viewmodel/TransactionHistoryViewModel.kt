package com.example.kwalletay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kwalletay.data.model.Transaction
import com.example.kwalletay.data.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TransactionHistoryUiState(
    val transactions: List<Transaction> = emptyList(),
    val searchQuery: String = "",
    val selectedType: String? = null, // "CREDIT", "DEBIT", or null
    val isLoading: Boolean = false
)

class TransactionHistoryViewModel(private val repository: FirestoreRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionHistoryUiState())
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState.asStateFlow()
    
    private val auth = FirebaseAuth.getInstance()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        val userId = auth.currentUser?.uid ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            combine(
                repository.getTransactions(userId),
                _uiState.map { it.searchQuery }.distinctUntilChanged(),
                _uiState.map { it.selectedType }.distinctUntilChanged()
            ) { allTransactions, query, type ->
                allTransactions.filter { transaction ->
                    val matchesQuery = transaction.title.contains(query, ignoreCase = true) ||
                            transaction.category.contains(query, ignoreCase = true)
                    val matchesType = type == null || transaction.type == type
                    matchesQuery && matchesType
                }
            }.collect { filteredList ->
                _uiState.update { it.copy(transactions = filteredList, isLoading = false) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onTypeFilterChange(type: String?) {
        _uiState.update { it.copy(selectedType = type) }
    }
}
