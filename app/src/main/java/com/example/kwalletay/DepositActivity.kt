package com.example.kwalletay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kwalletay.data.local.AppDatabase
import com.example.kwalletay.data.local.TransactionEntity
import com.example.kwalletay.data.repository.TransactionRepository
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
                val context = LocalContext.current

                // Initialize ViewModel using the shared factory
                val viewModel: DepositViewModel = viewModel(
                    factory = ViewModelFactory(context)
                )

                NavHost(navController = navController, startDestination = "deposit_input") {
                    composable("deposit_input") {
                        DepositScreen(
                            viewModel = viewModel,
                            onBackClick = { finish() },
                            onSuccess = { transaction ->
                                // Cast Any to TransactionEntity and use the auto-generated 'id'
                                val txn = transaction as TransactionEntity
                                navController.navigate("success/${txn.id}") {
                                    popUpTo("deposit_input") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(
                        route = "success/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: 0
                        val repository = TransactionRepository(AppDatabase.getDatabase(context).transactionDao())

                        SuccessScreen(
                            transactionId = id,
                            repository = repository,
                            onBackHome = { finish() },
                            onViewHistory = {
                                // Finish and return to MainActivity to view history
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }
}