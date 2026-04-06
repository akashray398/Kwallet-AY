package com.example.kwalletay.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kwalletay.R
import com.example.kwalletay.data.local.TransactionEntity
import com.example.kwalletay.data.repository.TransactionRepository
import com.example.kwalletay.ui.components.LottieAnimationView
import com.example.kwalletay.ui.components.bounceClick
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SuccessScreen(
    transactionId: Int,
    repository: TransactionRepository,
    onBackHome: () -> Unit,
    onViewHistory: () -> Unit
) {
    var transaction by remember { mutableStateOf<TransactionEntity?>(null) }
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(transactionId) {
        transaction = repository.getTransactionById(transactionId)
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Lottie Success Animation
        // Note: Add success_anim.json to res/raw/
        LottieAnimationView(
            resId = R.raw.success_anim,
            modifier = Modifier.size(180.dp),
            iterations = 1
        )

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(1000)) + expandVertically(tween(1000))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Payment Successful!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your transaction has been processed.",
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Transaction Details Card with Entrance Animation
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn()
        ) {
            transaction?.let { tx ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        DetailRowItem(label = "Amount", value = "₹${tx.amount}")
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
                        DetailRowItem(label = "Recipient/Title", value = tx.title)
                        DetailRowItem(label = "Transaction ID", value = tx.transactionId)
                        DetailRowItem(label = "Date", value = formatDate(tx.date))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Buttons with Bounce Animation
        Button(
            onClick = onBackHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .bounceClick(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Back to Home", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onViewHistory,
            modifier = Modifier
                .fillMaxWidth()
                .bounceClick()
        ) {
            Text(
                "View Transaction History",
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun DetailRowItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
