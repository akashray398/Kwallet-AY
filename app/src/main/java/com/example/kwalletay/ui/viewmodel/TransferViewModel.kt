package com.example.kwalletay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kwalletay.data.model.Recipient
import com.example.kwalletay.data.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TransferUiState(
    val balance: Double = 0.0,
    val savedRecipients: List<Recipient> = emptyList(),
    val recipient: String = "",
    val amount: String = "",
    val note: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false,
    val showConfirmation: Boolean = false
)

class TransferViewModel(
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()
    
    private val auth = FirebaseAuth.getInstance()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.getUserData(userId).collect { user ->
                _uiState.update { it.copy(balance = user?.balance ?: 0.0) }
            }
        }
        // Recipients could also be fetched from Firestore, but keeping it simple for now
    }

    fun onRecipientChange(value: String) {
        _uiState.update { it.copy(recipient = value, errorMessage = null) }
    }

    fun onAmountChange(value: String) {
        if (value.all { it.isDigit() || it == '.' }) {
            _uiState.update { it.copy(amount = value, errorMessage = null) }
        }
    }

    fun onNoteChange(value: String) {
        _uiState.update { it.copy(note = value) }
    }

    fun initiateTransfer() {
        val state = _uiState.value
        val amountVal = state.amount.toDoubleOrNull() ?: 0.0

        when {
            state.recipient.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "Please enter a valid recipient") }
            }
            amountVal <= 0 -> {
                _uiState.update { it.copy(errorMessage = "Please enter a valid amount") }
            }
            amountVal > state.balance -> {
                _uiState.update { it.copy(errorMessage = "Insufficient balance") }
            }
            else -> {
                _uiState.update { it.copy(showConfirmation = true) }
            }
        }
    }

    fun confirmTransfer() {
        val state = _uiState.value
        val amountVal = state.amount.toDoubleOrNull() ?: 0.0
        val senderId = auth.currentUser?.uid ?: return

        _uiState.update { it.copy(showConfirmation = false, isLoading = true) }

        viewModelScope.launch {
            try {
                repository.performTransfer(senderId, state.recipient, amountVal, state.note)
                _uiState.update { it.copy(isLoading = false, success = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Transfer failed") }
            }
        }
    }

    fun dismissConfirmation() {
        _uiState.update { it.copy(showConfirmation = false) }
    }
    
    fun resetState() {
        _uiState.update { it.copy(success = false, errorMessage = null) }
    }
}
