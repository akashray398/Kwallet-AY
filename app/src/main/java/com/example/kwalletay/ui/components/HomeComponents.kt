package com.example.kwalletay.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kwalletay.R
import com.example.kwalletay.ui.theme.DarkGreen
import com.example.kwalletay.ui.theme.Green

@Composable
fun HomeHeader(
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "Profile",
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .clickable { onProfileClick() },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onNotificationClick,
            modifier = Modifier
                .size(55.dp)
                .background(Color.White, RoundedCornerShape(10.dp))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.notification),
                contentDescription = "Notifications",
                modifier = Modifier.padding(16.dp),
                tint = Color.Unspecified
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .size(55.dp)
                .background(Color.White, RoundedCornerShape(10.dp))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.setting),
                contentDescription = "Settings",
                modifier = Modifier.padding(16.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
fun BalanceSection(balance: String) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = "Checking account..",
            fontSize = 16.sp,
            color = Color.Black
        )
        Text(
            text = balance,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun ActionButtons(
    onDeposit: () -> Unit,
    onPaybill: () -> Unit,
    onTransfer: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionItem(
            iconRes = R.drawable.deposite,
            label = "Deposit",
            modifier = Modifier.weight(1f),
            onClick = onDeposit
        )
        ActionItem(
            iconRes = R.drawable.paybill,
            label = "Paybill",
            modifier = Modifier.weight(1f),
            onClick = onPaybill
        )
        ActionItem(
            iconRes = R.drawable.transfer,
            label = "Transfer",
            modifier = Modifier.weight(1f),
            onClick = onTransfer
        )
    }
}

@Composable
fun ActionItem(iconRes: Int, label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(15.dp))
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun ReferralCard(onReferClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(215.dp)
            .padding(vertical = 16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(text = "Refer friends", fontSize = 18.sp, color = Color.Black)
                Text(text = "Get ₹10", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(
                    text = "For every new user you refer to Kwallet",
                    color = Color.Black,
                    modifier = Modifier.width(150.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onReferClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Refer Now", color = Color.White)
                }
            }
            Image(
                painter = painterResource(id = R.drawable.banner),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                contentScale = ContentScale.FillHeight
            )
        }
    }
}

@Composable
fun TransactionHistoryHeader(onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Transaction History",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "See All",
            fontWeight = FontWeight.Bold,
            color = DarkGreen,
            modifier = Modifier.clickable { onSeeAll() }
        )
    }
}
