package com.example.kwalletay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kwalletay.data.local.TransactionEntity
import com.example.kwalletay.data.local.TransactionType
import com.example.kwalletay.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TransactionHistoryUiState(
    val transactions: List<TransactionEntity> = emptyList(),
    val searchQuery: String = "",
    val selectedType: TransactionType? = null,
    val isLoading: Boolean = false
)

class TransactionHistoryViewModel(private val repository: TransactionRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionHistoryUiState())
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            combine(
                repository.getAllTransactions(),
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

    fun onTypeFilterChange(type: TransactionType?) {
        _uiState.update { it.copy(selectedType = type) }
    }
}
