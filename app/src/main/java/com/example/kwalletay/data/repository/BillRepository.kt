package com.example.kwalletay.data.repository

import com.example.kwalletay.data.model.Bill
import com.example.kwalletay.data.model.BillType
import com.example.kwalletay.data.local.TransactionType
import com.example.kwalletay.data.local.TransactionStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

interface BillRepository {
    suspend fun fetchBill(consumerNumber: String, type: BillType): Flow<Result<Bill>>
    suspend fun payBill(bill: Bill): Flow<Result<String>>
}

class BillRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : BillRepository {

    override suspend fun fetchBill(consumerNumber: String, type: BillType): Flow<Result<Bill>> = flow {
        emit(Result.Loading)
        delay(1000) 
        try {
            // In a real app, you'd fetch this from an API or Firestore 'bills' collection
            val mockBill = Bill(
                id = UUID.randomUUID().toString(),
                consumerName = "John Doe",
                amount = when(type) {
                    BillType.ELECTRICITY -> 1250.50
                    BillType.MOBILE -> 499.00
                    BillType.WATER -> 300.25
                },
                dueDate = "25 Oct 2024",
                billType = type
            )
            emit(Result.Success(mockBill))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Failed to fetch bill"))
        }
    }

    override suspend fun payBill(bill: Bill): Flow<Result<String>> = flow {
        emit(Result.Loading)
        val userId = auth.currentUser?.uid ?: run {
            emit(Result.Error("User not authenticated"))
            return@flow
        }

        try {
            val userDoc = firestore.collection("users").document(userId)
            
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDoc)
                val currentBalance = snapshot.getDouble("balance") ?: 0.0
                
                if (currentBalance < bill.amount) {
                    throw Exception("Insufficient balance")
                }

                transaction.update(userDoc, "balance", currentBalance - bill.amount)
                
                val txnData = hashMapOf(
                    "userId" to userId,
                    "title" to "Bill Payment: ${bill.billType.name}",
                    "amount" to bill.amount,
                    "type" to TransactionType.DEBIT.name,
                    "status" to TransactionStatus.SUCCESS.name,
                    "date" to System.currentTimeMillis(),
                    "category" to "Bills",
                    "note" to "Consumer No: ${bill.id}"
                )
                transaction.set(firestore.collection("transactions").document(), txnData)
            }.await()

            emit(Result.Success("Payment Successful"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Payment failed"))
        }
    }
}
