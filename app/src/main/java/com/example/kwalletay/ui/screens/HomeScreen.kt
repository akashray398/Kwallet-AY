package com.example.kwalletay.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kwalletay.ui.components.*
import com.example.kwalletay.ui.theme.Background
import com.example.kwalletay.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onProfileClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onDepositClick: () -> Unit,
    onPaybillClick: () -> Unit,
    onTransferClick: () -> Unit,
    onReferClick: () -> Unit,
    onSeeAllClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Background,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    HomeHeader(
                        onProfileClick = onProfileClick,
                        onNotificationClick = onNotificationClick,
                        onSettingsClick = onSettingsClick
                    )
                }
                
                item {
                    BalanceSection(balance = uiState.balance)
                }
                
                item {
                    ActionButtons(
                        onDeposit = onDepositClick,
                        onPaybill = onPaybillClick,
                        onTransfer = onTransferClick
                    )
                }
                
                item {
                    ReferralCard(onReferClick = onReferClick)
                }
                
                item {
                    TransactionHistoryHeader(onSeeAll = onSeeAllClick)
                }

                if (uiState.isLoading && !uiState.isRefreshing) {
                    items(5) {
                        TransactionShimmerItem()
                    }
                } else if (uiState.errorMessage != null) {
                    item {
                        ErrorState(
                            message = uiState.errorMessage!!,
                            onRetry = { viewModel.loadHomeData() }
                        )
                    }
                } else if (uiState.transactions.isEmpty()) {
                    item {
                        EmptyState(message = "No recent transactions")
                    }
                } else {
                    items(uiState.transactions) { transaction ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInHorizontally()
                        ) {
                            TransactionItem(
                                transaction = transaction,
                                onClick = { /* Handle transaction click */ }
                            )
                        }
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = Color.Red,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF35E167)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Retry", color = Color.White)
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message, color = Color.Gray)
    }
}
