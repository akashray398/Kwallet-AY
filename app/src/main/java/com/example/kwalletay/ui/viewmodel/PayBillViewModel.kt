package com.example.kwalletay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kwalletay.data.model.Bill
import com.example.kwalletay.data.model.BillType
import com.example.kwalletay.data.repository.BillRepository
import com.example.kwalletay.data.repository.BillRepositoryImpl
import com.example.kwalletay.data.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PayBillUiState(
    val isLoading: Boolean = false,
    val bill: Bill? = null,
    val error: String? = null,
    val paymentStatus: String? = null,
    val consumerNumber: String = "",
    val selectedBillType: BillType = BillType.ELECTRICITY
)

class PayBillViewModel(
    private val repository: BillRepository = BillRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PayBillUiState())
    val uiState: StateFlow<PayBillUiState> = _uiState.asStateFlow()

    fun onConsumerNumberChange(number: String) {
        _uiState.update { it.copy(consumerNumber = number, error = null) }
    }

    fun onBillTypeSelect(type: BillType) {
        _uiState.update { it.copy(selectedBillType = type, bill = null, error = null) }
    }

    fun fetchBill() {
        val currentState = _uiState.value
        if (currentState.consumerNumber.isBlank()) {
            _uiState.update { it.copy(error = "Please enter consumer number") }
            return
        }

        viewModelScope.launch {
            repository.fetchBill(currentState.consumerNumber, currentState.selectedBillType).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true, error = null) }
                    is Result.Success -> _uiState.update { it.copy(isLoading = false, bill = result.data) }
                    is Result.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun payBill() {
        val bill = _uiState.value.bill ?: return
        viewModelScope.launch {
            repository.payBill(bill.id, bill.amount).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Result.Success -> _uiState.update { it.copy(isLoading = false, paymentStatus = "SUCCESS", bill = null) }
                    is Result.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun resetStatus() {
        _uiState.update { it.copy(paymentStatus = null) }
    }
}
