package com.example.kwalletay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kwalletay.data.local.TransactionEntity
import com.example.kwalletay.ui.screens.DepositScreen
import com.example.kwalletay.ui.screens.SuccessScreen
import com.example.kwalletay.ui.theme.KwalletTheme
import com.example.kwalletay.ui.viewmodel.DepositViewModel
import com.example.kwalletay.ui.viewmodel.ViewModelFactory

class DepositActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KwalletTheme {
                val navController = rememberNavController()
                val viewModel: DepositViewModel = viewModel(
                    factory = ViewModelFactory(this)
                )

                NavHost(navController = navController, startDestination = "deposit_input") {
                    composable("deposit_input") {
                        DepositScreen(
                            viewModel = viewModel,
                            onBackClick = { finish() },
                            onSuccess = { transaction ->
                                // Using transaction as TransactionEntity
                                val txn = transaction as TransactionEntity
                                navController.navigate("success/${txn.transactionId}/${txn.amount}/${txn.date}/${txn.paymentMethod}")
                            }
                        )
                    }
                    composable("success/{txnId}/{amount}/{date}/{method}") { backStackEntry ->
                        val txnId = backStackEntry.arguments?.getString("txnId") ?: ""
                        val amount = backStackEntry.arguments?.getString("amount")?.toDoubleOrNull() ?: 0.0
                        val date = backStackEntry.arguments?.getString("date") ?: ""
                        val method = backStackEntry.arguments?.getString("method") ?: ""
                        
                        SuccessScreen(
                            transaction = TransactionEntity(
                                transactionId = txnId,
                                amount = amount,
                                date = date,
                                paymentMethod = method,
                                title = "Deposit to Wallet"
                            ),
                            onBackHome = { finish() },
                            onViewTransactions = {
                                // Navigate to history or finish with a result
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }
}
