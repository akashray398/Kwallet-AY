package com.example.kwalletay.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kwalletay.data.local.AppDatabase
import com.example.kwalletay.data.repository.DepositRepository
import com.example.kwalletay.data.repository.TransactionRepository
import com.example.kwalletay.data.repository.TransferRepositoryImpl

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getDatabase(context)
        val transactionDao = database.transactionDao()
        val transactionRepository = TransactionRepository(transactionDao)
        
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HomeViewModel(transactionRepository) as T
            }
            modelClass.isAssignableFrom(DepositViewModel::class.java) -> {
                val repository = DepositRepository(transactionDao)
                @Suppress("UNCHECKED_CAST")
                DepositViewModel(repository) as T
            }
            modelClass.isAssignableFrom(TransferViewModel::class.java) -> {
                val repository = TransferRepositoryImpl(transactionDao)
                @Suppress("UNCHECKED_CAST")
                TransferViewModel(repository) as T
            }
            modelClass.isAssignableFrom(TransactionHistoryViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                TransactionHistoryViewModel(transactionRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
