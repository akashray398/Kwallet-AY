package com.example.kwalletay.data.repository

import com.example.kwalletay.data.local.TransactionDao
import com.example.kwalletay.data.local.TransactionEntity
import com.example.kwalletay.data.local.TransactionStatus
import com.example.kwalletay.data.local.TransactionType
import com.example.kwalletay.data.model.Recipient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

interface TransferRepository {
    fun getSavedRecipients(): Flow<List<Recipient>>
    suspend fun processTransfer(recipient: Recipient, amount: Double, note: String): Result<Int>
    fun getWalletBalance(): Flow<Double>
}

class TransferRepositoryImpl(
    private val transactionDao: TransactionDao
) : TransferRepository {

    override fun getSavedRecipients(): Flow<List<Recipient>> = flow {
        // Mocking saved recipients
        emit(listOf(
            Recipient("1", "Akash Yadav", "akash@upi"),
            Recipient("2", "John Doe", "9876543210"),
            Recipient("3", "Jane Smith", "jane@upi"),
            Recipient("4", "Khushi", "khushi@upi")
        ))
    }

    override suspend fun processTransfer(recipient: Recipient, amount: Double, note: String): Result<Int> {
        return try {
            delay(2000) // Simulate network delay
            
            // Simulation: 95% success rate
            if (kotlin.random.Random.nextFloat() < 0.95f) {
                val transaction = TransactionEntity(
                    title = "Transfer to ${recipient.name}",
                    date = System.currentTimeMillis(),
                    amount = amount,
                    type = TransactionType.DEBIT,
                    status = TransactionStatus.SUCCESS,
                    category = "Transfer",
                    note = note,
                    transactionId = "TXN${UUID.randomUUID().toString().take(8).uppercase()}"
                )
                val id = transactionDao.insertTransaction(transaction).toInt()
                Result.Success(id)
            } else {
                Result.Error("Bank server is busy. Please try again later.")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Transfer Failed")
        }
    }

    override fun getWalletBalance(): Flow<Double> = flow {
        // In a real app, this would be calculated from the DB or fetched from an API
        emit(185540.0)
    }
}
