package com.example.kwalletay.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kwalletay.data.local.AppDatabase
import com.example.kwalletay.data.repository.DepositRepository
import com.example.kwalletay.data.repository.FirestoreRepository
import com.example.kwalletay.data.repository.TransactionRepository
import com.example.kwalletay.data.repository.TransferRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getDatabase(context)
        val transactionDao = database.transactionDao()
        val transactionRepository = TransactionRepository(transactionDao)
        val firestoreRepository = FirestoreRepository(FirebaseFirestore.getInstance())
        
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HomeViewModel(firestoreRepository) as T
            }
            modelClass.isAssignableFrom(DepositViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                DepositViewModel(firestoreRepository) as T
            }
            modelClass.isAssignableFrom(TransferViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                TransferViewModel(firestoreRepository) as T
            }
            modelClass.isAssignableFrom(TransactionHistoryViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                TransactionHistoryViewModel(firestoreRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
