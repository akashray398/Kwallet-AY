package com.example.kwalletay.data.repository

import android.app.Activity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthOptions
import kotlinx.coroutines.flow.Flow

sealed class AuthResource<out T> {
    data class Success<out T>(val data: T) : AuthResource<T>()
    data class Error(val message: String) : AuthResource<Nothing>()
    object Loading : AuthResource<Nothing>()
}

interface AuthRepository {
    val currentUser: FirebaseUser?
    fun login(email: String, password: String): Flow<AuthResource<FirebaseUser>>
    fun signup(email: String, password: String): Flow<AuthResource<FirebaseUser>>
    fun signInWithCredential(credential: AuthCredential): Flow<AuthResource<FirebaseUser>>
    fun sendOtp(phoneNumber: String, activity: Activity): Flow<AuthResource<String>>
    fun verifyOtp(verificationId: String, otp: String): Flow<AuthResource<FirebaseUser>>
    fun logout()
}
