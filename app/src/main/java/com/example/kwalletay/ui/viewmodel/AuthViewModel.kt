package com.example.kwalletay.ui.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kwalletay.data.repository.AuthRepository
import com.example.kwalletay.data.repository.AuthResource
import com.example.kwalletay.data.repository.AuthRepositoryImpl
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val user: FirebaseUser? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccessful: Boolean = false,
    val verificationId: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState(user = repository.currentUser))
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password).collect { result ->
                when (result) {
                    is AuthResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is AuthResource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = result.data,
                            isLoginSuccessful = true
                        )
                    }
                    is AuthResource.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            repository.signup(email, password).collect { result ->
                when (result) {
                    is AuthResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is AuthResource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = result.data,
                            isLoginSuccessful = true
                        )
                    }
                    is AuthResource.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }

    fun sendOtp(phoneNumber: String, activity: Activity, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            repository.sendOtp(phoneNumber, activity).collect { result ->
                when (result) {
                    is AuthResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is AuthResource.Success -> {
                        _uiState.value = _uiState.value.copy(isLoading = false, verificationId = result.data)
                        onSuccess(result.data)
                    }
                    is AuthResource.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }

    fun verifyOtp(verificationId: String, otp: String) {
        viewModelScope.launch {
            repository.verifyOtp(verificationId, otp).collect { result ->
                when (result) {
                    is AuthResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is AuthResource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = result.data,
                            isLoginSuccessful = true
                        )
                    }
                    is AuthResource.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }

    fun signInWithCredential(credential: AuthCredential) {
        viewModelScope.launch {
            repository.signInWithCredential(credential).collect { result ->
                when (result) {
                    is AuthResource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is AuthResource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = result.data,
                            isLoginSuccessful = true
                        )
                    }
                    is AuthResource.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = AuthUiState()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
