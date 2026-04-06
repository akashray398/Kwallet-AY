package com.example.kwalletay.data.repository

import android.app.Activity
import com.example.kwalletay.data.model.User
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

sealed class AuthResource<out T> {
    data class Success<out T>(val data: T) : AuthResource<T>()
    data class Error(val message: String) : AuthResource<Nothing>()
    object Loading : AuthResource<Nothing>()
}

interface AuthRepository {
    val currentUser: User?
    fun login(email: String, password: String): Flow<AuthResource<User>>
    fun signup(email: String, password: String): Flow<AuthResource<User>>
    fun signInWithCredential(credential: AuthCredential): Flow<AuthResource<User>>
    fun sendOtp(phoneNumber: String, activity: Activity): Flow<AuthResource<String>>
    fun verifyOtp(verificationId: String, otp: String): Flow<AuthResource<User>>
    fun logout()
}
