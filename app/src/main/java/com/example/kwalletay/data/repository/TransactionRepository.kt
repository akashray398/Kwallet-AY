package com.example.kwalletay.data.repository

import com.example.kwalletay.data.local.TransactionDao
import com.example.kwalletay.data.local.TransactionEntity
import com.example.kwalletay.data.local.TransactionType
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    fun getAllTransactions(): Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

    fun searchTransactions(query: String): Flow<List<TransactionEntity>> = 
        transactionDao.searchTransactions(query)

    fun filterByType(type: TransactionType): Flow<List<TransactionEntity>> = 
        transactionDao.filterByType(type)

    suspend fun getTransactionById(id: Int): TransactionEntity? = 
        transactionDao.getTransactionById(id)

    suspend fun insertTransaction(transaction: TransactionEntity) = 
        transactionDao.insertTransaction(transaction)

    fun getBalance(): Flow<Double?> = transactionDao.getBalance()
}
