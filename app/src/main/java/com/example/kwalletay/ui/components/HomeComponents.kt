package com.example.kwalletay.ui.components

import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.shadow
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
                .bounceClick()
                .clickable { onProfileClick() },
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = onNotificationClick,
            modifier = Modifier
                .size(55.dp)
                .background(Color.White, RoundedCornerShape(10.dp))
                .bounceClick()
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
                .bounceClick()
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
            color = Color.Black.copy(alpha = 0.6f)
        )
        Text(
            text = balance,
            fontSize = 42.sp,
            fontWeight = FontWeight.ExtraBold,
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
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(18.dp))
            .background(Color.White, RoundedCornerShape(18.dp))
            .bounceClick()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(42.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@Composable
fun ReferralCard(onReferClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(vertical = 16.dp)
            .bounceClick()
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(text = "Refer friends", fontSize = 16.sp, color = Color.Gray)
                Text(text = "Get ₹100", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                Text(
                    text = "For every new user you refer to Kwallet",
                    color = Color.Black.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    modifier = Modifier.width(160.dp)
                )
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = onReferClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Green),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(text = "Refer Now", color = Color.White, fontWeight = FontWeight.Bold)
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
            .padding(top = 24.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Recent Transactions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "See All",
            fontWeight = FontWeight.Bold,
            color = DarkGreen,
            modifier = Modifier
                .bounceClick()
                .clickable { onSeeAll() }
                .padding(4.dp)
        )
    }
}
