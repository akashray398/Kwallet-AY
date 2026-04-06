package com.example.kwalletay.data.repository

import com.example.kwalletay.data.model.Transaction
import com.example.kwalletay.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val firestore = FirebaseFirestore.getInstance()

    // Get live user data
    fun getUserData(userId: String): Flow<User?> = callbackFlow {
        val subscription = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, _ ->
                val user = snapshot?.toObject(User::class.java)
                trySend(user)
            }
        awaitClose { subscription.remove() }
    }

    // Get live user balance
    fun getUserBalance(userId: String): Flow<Double> = callbackFlow {
        val subscription = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, _ ->
                val balance = snapshot?.getDouble("balance") ?: 0.0
                trySend(balance)
            }
        awaitClose { subscription.remove() }
    }

    // Get live transaction history
    fun getTransactions(userId: String): Flow<List<Transaction>> = callbackFlow {
        val query = firestore.collection("transactions")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val subscription = query.addSnapshotListener { snapshot, _ ->
            val transactions = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Transaction::class.java)?.copy(id = doc.id)
            } ?: emptyList()
            trySend(transactions)
        }
        awaitClose { subscription.remove() }
    }

    // Atomic Transaction: Update balance and add record
    suspend fun performTransaction(
        userId: String,
        amount: Double,
        type: String, // "CREDIT" or "DEBIT"
        title: String,
        category: String = "General"
    ): Result<String> = try {
        firestore.runTransaction { transaction ->
            val userRef = firestore.collection("users").document(userId)
            val userSnapshot = transaction.get(userRef)
            val currentBalance = userSnapshot.getDouble("balance") ?: 0.0

            val newBalance = if (type == "CREDIT") currentBalance + amount else currentBalance - amount

            if (newBalance < 0) throw Exception("Insufficient balance")

            // Update balance
            transaction.update(userRef, "balance", newBalance)

            // Create transaction record
            val txRef = firestore.collection("transactions").document()
            val txData = Transaction(
                userId = userId,
                amount = amount,
                type = type,
                title = title,
                category = category,
                timestamp = System.currentTimeMillis(),
                status = "SUCCESS"
            )
            transaction.set(txRef, txData)
        }.await()
        Result.success("Success")
    } catch (e: Exception) {
        Result.failure(e)
    }
}
