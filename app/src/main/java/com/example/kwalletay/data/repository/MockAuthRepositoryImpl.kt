package com.example.kwalletay.data.repository

import android.app.Activity
import com.example.kwalletay.data.model.User
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockAuthRepositoryImpl : AuthRepository {
    
    override val currentUser: User? = null

    override fun login(email: String, password: String): Flow<AuthResource<User>> = flow {
        emit(AuthResource.Loading)
        delay(1500)
        emit(AuthResource.Success(User(uid = "123", email = email, displayName = "Mock User")))
    }

    override fun signup(email: String, password: String): Flow<AuthResource<User>> = flow {
        emit(AuthResource.Loading)
        delay(1500)
        emit(AuthResource.Success(User(uid = "123", email = email, displayName = "Mock User")))
    }

    override fun signInWithCredential(credential: AuthCredential): Flow<AuthResource<User>> = flow {
        emit(AuthResource.Loading)
        delay(1500)
        emit(AuthResource.Success(User(uid = "123", email = "google@example.com", displayName = "Google User")))
    }

    override fun sendOtp(phoneNumber: String, activity: Activity): Flow<AuthResource<String>> = flow {
        emit(AuthResource.Loading)
        delay(1500)
        emit(AuthResource.Success("mock_verification_id"))
    }

    override fun verifyOtp(verificationId: String, otp: String): Flow<AuthResource<User>> = flow {
        emit(AuthResource.Loading)
        delay(1500)
        emit(AuthResource.Success(User(uid = "123", email = "phone@example.com", displayName = "Phone User")))
    }

    override fun logout() {
        // Mock logout
    }
}
