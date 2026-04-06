package com.example.kwalletay.data.repository

import com.example.kwalletay.data.local.TransactionDao
import com.example.kwalletay.data.local.TransactionEntity
import com.example.kwalletay.data.model.Recipient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

interface TransferRepository {
    fun getSavedRecipients(): Flow<List<Recipient>>
    suspend fun processTransfer(recipient: Recipient, amount: Double, note: String): Result<String>
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

    override suspend fun processTransfer(recipient: Recipient, amount: Double, note: String): Result<String> {
        return try {
            delay(2000) // Simulate network delay
            
            // In a real app, you'd call an API here.
            // If API succeeds, save to local DB:
            val transaction = TransactionEntity(
                title = "Transfer to ${recipient.name}",
                date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                amount = -amount,
                paymentMethod = "Wallet",
                transactionId = "TXN${UUID.randomUUID().toString().take(8).uppercase()}"
            )
            transactionDao.insertTransaction(transaction)
            
            Result.Success("Transfer Successful")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Transfer Failed")
        }
    }

    override fun getWalletBalance(): Flow<Double> = flow {
        // Mock current balance. In real app, this might come from a DataStore or DB.
        emit(185540.0)
    }
}
