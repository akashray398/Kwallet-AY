package com.example.kwalletay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kwalletay.data.repository.DepositRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: DepositRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DepositUiState>(DepositUiState.Idle)
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _selectedMethod = MutableStateFlow("UPI")
    val selectedMethod: StateFlow<String> = _selectedMethod.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onAmountChange(newAmount: String) {
        if (newAmount.all { it.isDigit() }) {
            _amount.value = newAmount
            _error.value = null
        }
    }

    fun onMethodSelect(method: String) {
        _selectedMethod.value = method
    }

    fun onQuickAmountClick(quickAmount: Int) {
        _amount.value = quickAmount.toString()
        _error.value = null
    }

    fun processDeposit() {
        val amountVal = _amount.value.toDoubleOrNull()
        
        if (amountVal == null || amountVal <= 0) {
            _error.value = "Please enter a valid amount"
            return
        }
        
        if (amountVal < 100) {
            _error.value = "Minimum deposit amount is ₹100"
            return
        }

        if (amountVal > 100000) {
            _error.value = "Maximum deposit limit is ₹1,00,000"
            return
        }

        viewModelScope.launch {
            _uiState.value = DepositUiState.Loading
            val result = repository.processDeposit(amountVal, _selectedMethod.value)
            result.onSuccess { transaction ->
                _uiState.value = DepositUiState.Success(transaction)
            }.onFailure { exception ->
                _uiState.value = DepositUiState.Error(exception.message ?: "Transaction failed")
            }
        }
    }

    fun resetState() {
        _uiState.value = DepositUiState.Idle
    }
}
