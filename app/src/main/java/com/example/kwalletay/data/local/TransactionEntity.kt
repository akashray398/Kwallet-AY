package com.example.kwalletay.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionType {
    CREDIT, DEBIT
}

enum class TransactionStatus {
    SUCCESS, FAILED, PENDING
}

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val status: TransactionStatus,
    val date: Long, // Using timestamp for easier sorting/filtering
    val category: String = "General",
    val note: String? = null,
    val transactionId: String
)
