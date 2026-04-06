package com.example.kwalletay.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    // Auth Screens
    object Splash : Screen("splash", "Splash")
    object Login : Screen("login", "Login")
    object Signup : Screen("signup", "Signup")
    object PhoneAuth : Screen("phone_auth", "Phone Auth")
    object OtpVerify : Screen("otp_verify/{verificationId}", "OTP Verify") {
        fun createRoute(verificationId: String) = "otp_verify/$verificationId"
    }

    // Main Screens
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Explorer : Screen("explorer", "Explorer", Icons.Default.Search)
    object History : Screen("history", "History", Icons.Default.History)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    
    // Feature Screens
    object PayBill : Screen("pay_bill", "Pay Bill")
    object Deposit : Screen("deposit", "Deposit")
    object Transfer : Screen("transfer", "Transfer")
    object ReferAndEarn : Screen("refer_and_earn", "Refer & Earn")
    object TransactionDetail : Screen("transaction_detail/{transactionId}", "Transaction Detail") {
        fun createRoute(transactionId: Int) = "transaction_detail/$transactionId"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Explorer,
    Screen.History,
    Screen.Profile
)
