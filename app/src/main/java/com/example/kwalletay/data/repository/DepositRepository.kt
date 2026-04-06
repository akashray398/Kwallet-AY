package com.example.kwalletay.data.repository

import com.example.kwalletay.data.local.TransactionDao
import com.example.kwalletay.data.local.TransactionEntity
import com.example.kwalletay.data.local.TransactionStatus
import com.example.kwalletay.data.local.TransactionType
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.random.Random

class DepositRepository(private val transactionDao: TransactionDao) {

    suspend fun processDeposit(amount: Double, paymentMethod: String): Result<TransactionEntity> {
        return try {
            delay(2000) // Simulate network delay
            
            // Randomly simulate success or failure (90% success for demo)
            if (Random.nextFloat() < 0.9f) {
                val transaction = TransactionEntity(
                    title = "Deposit via $paymentMethod",
                    date = System.currentTimeMillis(),
                    amount = amount,
                    type = TransactionType.CREDIT,
                    status = TransactionStatus.SUCCESS,
                    category = "Deposit",
                    transactionId = "DEP${UUID.randomUUID().toString().take(8).uppercase()}"
                )
                val id = transactionDao.insertTransaction(transaction).toInt()
                Result.Success(transaction.copy(id = id))
            } else {
                Result.Error("Payment declined by bank. Please try another method.")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }
}
