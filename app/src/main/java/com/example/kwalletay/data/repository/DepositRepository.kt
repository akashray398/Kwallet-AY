package com.example.kwalletay.data.repository

import com.example.kwalletay.data.local.TransactionDao
import com.example.kwalletay.data.local.TransactionEntity
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

class DepositRepository(private val transactionDao: TransactionDao) {

    suspend fun processDeposit(amount: Double, paymentMethod: String): Result<TransactionEntity> {
        return try {
            delay(2000) // Simulate network delay
            
            // Randomly simulate success or failure (80% success)
            if (Random.nextFloat() < 0.8f) {
                val transaction = TransactionEntity(
                    title = "Deposit to Wallet",
                    date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date()),
                    amount = amount,
                    paymentMethod = paymentMethod,
                    transactionId = "TXN${UUID.randomUUID().toString().take(8).uppercase()}"
                )
                transactionDao.insertTransaction(transaction)
                Result.Success(transaction)
            } else {
                Result.Error("Payment failed. Please try again.")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }
}
