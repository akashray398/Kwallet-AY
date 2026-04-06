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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kwalletay.R
import com.example.kwalletay.ui.components.LottieAnimationView
import com.example.kwalletay.ui.components.bounceClick

@Composable
fun FailureScreen(
    errorMessage: String,
    onRetry: () -> Unit,
    onBackHome: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Lottie Failure Animation
        // Note: Add failure_anim.json to res/raw/
        LottieAnimationView(
            resId = R.raw.failure_anim,
            modifier = Modifier.size(150.dp),
            iterations = 1
        )

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(800)) + expandVertically(tween(800))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Transaction Failed",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = errorMessage,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Buttons with Bounce Animation
        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .bounceClick(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Try Again", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBackHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .bounceClick(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Back to Home", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
