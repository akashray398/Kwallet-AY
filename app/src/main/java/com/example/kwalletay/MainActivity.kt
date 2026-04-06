package com.example.kwalletay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.kwalletay.data.local.AppDatabase
import com.example.kwalletay.data.repository.TransactionRepository
import com.example.kwalletay.ui.navigation.Screen
import com.example.kwalletay.ui.navigation.bottomNavItems
import com.example.kwalletay.ui.screens.*
import com.example.kwalletay.ui.theme.KwalletTheme
import com.example.kwalletay.ui.viewmodel.TransactionHistoryViewModel
import com.example.kwalletay.ui.viewmodel.TransferViewModel
import com.example.kwalletay.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KwalletTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val showBottomBar = bottomNavItems.any { it.route == currentDestination?.route }

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar {
                                bottomNavItems.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { screen.icon?.let { Icon(it, contentDescription = null) } },
                                        label = { Text(screen.title) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Splash.route) {
                            SplashScreen(
                                onNavigateToHome = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Splash.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable(Screen.Login.route) {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                },
                                onNavigateToSignup = { navController.navigate(Screen.Signup.route) },
                                onNavigateToPhoneAuth = { navController.navigate(Screen.PhoneAuth.route) }
                            )
                        }
                        composable(Screen.Signup.route) {
                            SignupScreen(
                                onSignupSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Signup.route) { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.PhoneAuth.route) {
                            PhoneAuthScreen(
                                onCodeSent = { verificationId ->
                                    navController.navigate(Screen.OtpVerify.createRoute(verificationId))
                                },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = Screen.OtpVerify.route,
                            arguments = listOf(navArgument("verificationId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
                            OtpVerificationScreen(
                                verificationId = verificationId,
                                onVerificationSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onProfileClick = { navController.navigate(Screen.Profile.route) },
                                onNotificationClick = { /* Handle */ },
                                onSettingsClick = { /* Handle */ },
                                onDepositClick = { navController.navigate(Screen.Deposit.route) },
                                onPaybillClick = { navController.navigate(Screen.PayBill.route) },
                                onTransferClick = { navController.navigate(Screen.Transfer.route) },
                                onReferClick = { navController.navigate(Screen.ReferAndEarn.route) },
                                onSeeAllClick = { navController.navigate(Screen.History.route) }
                            )
                        }
                        composable(Screen.PayBill.route) {
                            PayBillScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Deposit.route) {
                            DepositScreen(onBackClick = { navController.popBackStack() })
                        }
                        composable(Screen.Transfer.route) {
                            val context = LocalContext.current
                            val transferViewModel: TransferViewModel = viewModel(
                                factory = ViewModelFactory(context)
                            )
                            TransferScreen(
                                viewModel = transferViewModel,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.ReferAndEarn.route) {
                            ReferAndEarnScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Explorer.route) { /* Explorer Screen */ }
                        composable(Screen.History.route) {
                            val context = LocalContext.current
                            val historyViewModel: TransactionHistoryViewModel = viewModel(
                                factory = ViewModelFactory(context)
                            )
                            TransactionHistoryScreen(
                                viewModel = historyViewModel,
                                onBackClick = { navController.popBackStack() },
                                onTransactionClick = { id ->
                                    navController.navigate(Screen.TransactionDetail.createRoute(id))
                                }
                            )
                        }
                        composable(Screen.Profile.route) { /* Profile Screen */ }
                        composable(
                            route = Screen.TransactionDetail.route,
                            arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val context = LocalContext.current
                            val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: 0
                            val repository = TransactionRepository(AppDatabase.getDatabase(context).transactionDao())
                            TransactionDetailScreen(
                                transactionId = transactionId,
                                repository = repository,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
