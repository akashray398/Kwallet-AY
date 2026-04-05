package com.example.kwalletay.data.repository

import com.example.kwalletay.Model.Transection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TransactionRepository {

    fun getTransactions(): Flow<List<Transection>> = flow {
        // Simulate network delay
        delay(1500)
        emit(
            listOf(
                Transection("Add to Wallet", "15 Oct 2023", 923.2),
                Transection("Transaction to Akash Yadav", "14 Oct 2023", -576.2),
                Transection("Salary Deposit", "10 Oct 2023", 2500.0),
                Transection("Netflix Subscription", "08 Oct 2023", -15.99),
                Transection("Starbucks", "07 Oct 2023", -6.50),
                Transection("Credited by Khushi", "05 Oct 2023", 25.0),
                Transection("Cashback Reward", "05 Oct 2023", 25.0)
            )
        )
    }

    fun getBalance(): Flow<String> = flow {
        delay(1000)
        emit("₹185,540.00")
    }
}
