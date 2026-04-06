package com.example.kwalletay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kwalletay.data.model.Recipient
import com.example.kwalletay.data.repository.Result
import com.example.kwalletay.data.repository.TransferRepository
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
    val transferResult: String? = null,
    val showConfirmation: Boolean = false
)

class TransferViewModel(
    private val repository: TransferRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            combine(
                repository.getWalletBalance(),
                repository.getSavedRecipients()
            ) { balance, recipients ->
                _uiState.update { it.copy(balance = balance, savedRecipients = recipients) }
            }.collect()
        }
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

    fun onRecipientSelected(recipient: Recipient) {
        _uiState.update { it.copy(recipient = recipient.identifier) }
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
        val selectedRecipient = state.savedRecipients.find { it.identifier == state.recipient }
            ?: Recipient("", state.recipient, state.recipient)

        _uiState.update { it.copy(showConfirmation = false, isLoading = true) }

        viewModelScope.launch {
            val result = repository.processTransfer(selectedRecipient, amountVal, state.note)
            when (result) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false, transferResult = "Success") }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                else -> {}
            }
        }
    }

    fun dismissConfirmation() {
        _uiState.update { it.copy(showConfirmation = false) }
    }
    
    fun resetResult() {
        _uiState.update { it.copy(transferResult = null) }
    }
}
