package com.example.kwalletay.data.repository

import android.app.Activity
import com.example.kwalletay.data.model.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth = try { FirebaseAuth.getInstance() } catch (e: Exception) { null } ?: throw IllegalStateException("Firebase not initialized"),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override val currentUser: User?
        get() = firebaseAuth.currentUser?.toUser()

    override fun login(email: String, password: String): Flow<AuthResource<User>> = flow {
        emit(AuthResource.Loading)
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                emit(AuthResource.Success(it.toUser()))
            } ?: emit(AuthResource.Error("User not found"))
        } catch (e: Exception) {
            emit(AuthResource.Error(e.localizedMessage ?: "Login failed"))
        }
    }

    override fun signup(email: String, password: String): Flow<AuthResource<User>> = flow {
        emit(AuthResource.Loading)
        try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                val newUser = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email,
                    displayName = firebaseUser.displayName,
                    balance = 0.0,
                    referralCode = generateReferralCode(firebaseUser.uid)
                )
                // Create user document in Firestore
                firestore.collection("users").document(firebaseUser.uid).set(newUser).await()
                emit(AuthResource.Success(newUser))
            } else {
                emit(AuthResource.Error("Signup failed"))
            }
        } catch (e: Exception) {
            emit(AuthResource.Error(e.localizedMessage ?: "Signup failed"))
        }
    }

    private fun generateReferralCode(userId: String): String {
        return "KW" + userId.takeLast(4).uppercase() + (100..999).random()
    }

    override fun signInWithCredential(credential: AuthCredential): Flow<AuthResource<User>> = flow {
        emit(AuthResource.Loading)
        try {
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                // Check if user exists in Firestore, if not create them
                val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
                if (!userDoc.exists()) {
                    val newUser = User(
                        uid = firebaseUser.uid,
                        email = firebaseUser.email,
                        displayName = firebaseUser.displayName,
                        balance = 0.0,
                        referralCode = generateReferralCode(firebaseUser.uid)
                    )
                    firestore.collection("users").document(firebaseUser.uid).set(newUser).await()
                    emit(AuthResource.Success(newUser))
                } else {
                    emit(AuthResource.Success(userDoc.toObject(User::class.java)!!))
                }
            } else {
                emit(AuthResource.Error("Sign in failed"))
            }
        } catch (e: Exception) {
            emit(AuthResource.Error(e.localizedMessage ?: "Sign in failed"))
        }
    }

    override fun sendOtp(phoneNumber: String, activity: Activity): Flow<AuthResource<String>> = callbackFlow {
        trySend(AuthResource.Loading)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

            override fun onVerificationFailed(e: FirebaseException) {
                trySend(AuthResource.Error(e.localizedMessage ?: "OTP sending failed"))
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                trySend(AuthResource.Success(verificationId))
            }
        }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        
        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose()
    }

    override fun verifyOtp(verificationId: String, otp: String): Flow<AuthResource<User>> = flow {
        emit(AuthResource.Loading)
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user
            if (firebaseUser != null) {
                val userDoc = firestore.collection("users").document(firebaseUser.uid).get().await()
                if (!userDoc.exists()) {
                    val newUser = User(
                        uid = firebaseUser.uid,
                        email = firebaseUser.email,
                        displayName = firebaseUser.displayName,
                        balance = 0.0,
                        referralCode = generateReferralCode(firebaseUser.uid)
                    )
                    firestore.collection("users").document(firebaseUser.uid).set(newUser).await()
                    emit(AuthResource.Success(newUser))
                } else {
                    emit(AuthResource.Success(userDoc.toObject(User::class.java)!!))
                }
            } else {
                emit(AuthResource.Error("OTP Verification failed"))
            }
        } catch (e: Exception) {
            emit(AuthResource.Error(e.localizedMessage ?: "Invalid OTP"))
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    private fun FirebaseUser.toUser(): User = User(
        uid = uid,
        email = email,
        displayName = displayName
    )
}
