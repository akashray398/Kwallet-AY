package com.example.kwalletay.data.repository

import com.example.kwalletay.data.model.Bill
import com.example.kwalletay.data.model.BillType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

interface BillRepository {
    suspend fun fetchBill(consumerNumber: String, type: BillType): Flow<Result<Bill>>
    suspend fun payBill(billId: String, amount: Double): Flow<Result<String>>
}

class BillRepositoryImpl : BillRepository {
    override suspend fun fetchBill(consumerNumber: String, type: BillType): Flow<Result<Bill>> = flow {
        emit(Result.Loading)
        delay(1500) // Simulate network delay
        try {
            // Mock data
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

    override suspend fun payBill(billId: String, amount: Double): Flow<Result<String>> = flow {
        emit(Result.Loading)
        delay(2000)
        emit(Result.Success("Transaction ID: ${UUID.randomUUID()}"))
    }
}

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}
