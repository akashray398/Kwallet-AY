package com.example.kwalletay.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kwalletay.data.local.TransactionEntity
import com.example.kwalletay.data.local.TransactionType
import com.example.kwalletay.R
import com.example.kwalletay.ui.theme.LightGreen
import com.example.kwalletay.ui.theme.LightRed
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionItem(
    transaction: TransactionEntity,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isPositive = transaction.type == TransactionType.CREDIT
        val iconBackground = if (isPositive) LightGreen else LightRed
        val iconRes = if (isPositive) R.drawable.deposite else R.drawable.transfer

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(iconBackground, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            val dateString = sdf.format(Date(transaction.date))
            Text(
                text = dateString,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Text(
            text = if (isPositive) "+₹${transaction.amount}" else "-₹${transaction.amount}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isPositive) Color(0xFF10972C) else Color(0xFFA50D0D)
        )
    }
}
