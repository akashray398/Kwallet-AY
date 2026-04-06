package com.example.kwalletay.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kwalletay.data.model.Referral
import com.example.kwalletay.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReferralUiState(
    val referralCode: String = "",
    val referralHistory: List<Referral> = emptyList(),
    val rewardAmount: Double = 100.0,
    val isLoading: Boolean = false,
    val user: User? = null
)

class ReferralViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReferralUiState())
    val uiState: StateFlow<ReferralUiState> = _uiState.asStateFlow()

    init {
        loadReferralData()
    }

    private fun loadReferralData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Mocking user and referral data
            val mockUser = User(uid = "USER123", email = "test@example.com", displayName = "John Doe")
            val generatedCode = generateReferralCode(mockUser.uid)
            
            val mockHistory = listOf(
                Referral("1", "Alice Smith", "12 Oct 2024", "Completed", 100.0),
                Referral("2", "Bob Johnson", "15 Oct 2024", "Pending", 0.0),
                Referral("3", "Charlie Brown", "20 Oct 2024", "Completed", 100.0)
            )

            _uiState.update { 
                it.copy(
                    user = mockUser,
                    referralCode = generatedCode,
                    referralHistory = mockHistory,
                    isLoading = false
                )
            }
        }
    }

    private fun generateReferralCode(userId: String): String {
        return "KW" + userId.takeLast(4).uppercase() + (100..999).random()
    }
}
