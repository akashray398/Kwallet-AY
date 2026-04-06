package com.example.kwalletay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kwalletay.data.repository.FirestoreRepository
import com.example.kwalletay.data.repository.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DepositViewModel(private val repository: FirestoreRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DepositUiState>(DepositUiState.Idle)
    val uiState: StateFlow<DepositUiState> = _uiState.asStateFlow()

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _selectedMethod = MutableStateFlow("UPI")
    val selectedMethod: StateFlow<String> = _selectedMethod.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val auth = FirebaseAuth.getInstance()

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
        val userId = auth.currentUser?.uid ?: return
        
        if (amountVal == null || amountVal <= 0) {
            _error.value = "Please enter a valid amount"
            return
        }
        
        if (amountVal < 100) {
            _error.value = "Minimum deposit amount is ₹100"
            return
        }

        viewModelScope.launch {
            _uiState.value = DepositUiState.Loading
            try {
                repository.performDeposit(userId, amountVal, "Deposit via ${_selectedMethod.value}")
                // Since Firestore performDeposit returns Unit (void) on success, we simulate a result
                // In a real app, performDeposit might return the transaction ID
                _uiState.value = DepositUiState.Success(null) 
            } catch (e: Exception) {
                _uiState.value = DepositUiState.Error(e.message ?: "Deposit failed")
            }
        }
    }

    fun resetState() {
        _uiState.value = DepositUiState.Idle
    }
}
