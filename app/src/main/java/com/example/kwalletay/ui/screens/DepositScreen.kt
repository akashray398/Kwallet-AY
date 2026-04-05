package com.example.kwalletay.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kwalletay.ui.viewmodel.DepositUiState
import com.example.kwalletay.ui.viewmodel.DepositViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepositScreen(
    viewModel: DepositViewModel,
    onBackClick: () -> Unit,
    onSuccess: (transaction: Any) -> Unit // TransactionEntity passed as Any to avoid strict typing in preview
) {
    val uiState by viewModel.uiState.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val selectedMethod by viewModel.selectedMethod.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is DepositUiState.Success) {
            onSuccess((uiState as DepositUiState.Success).transaction)
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Deposit Money", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Amount Input
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Enter Amount", color = Color.Gray, fontSize = 14.sp)
                    OutlinedTextField(
                        value = amount,
                        onValueChange = viewModel::onAmountChange,
                        modifier = Modifier.fillMaxWidth(),
                        prefix = { Text("₹", fontWeight = FontWeight.Bold) },
                        placeholder = { Text("0.00") },
                        isError = error != null,
                        supportingText = { error?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                }

                // Quick Amount Selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf(100, 500, 1000, 2000).forEach { valAmt ->
                        QuickAmountChip(
                            amount = "₹$valAmt",
                            onClick = { viewModel.onQuickAmountClick(valAmt) }
                        )
                    }
                }

                // Payment Methods
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Select Payment Method", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    PaymentMethodItem(
                        title = "UPI (GPay, PhonePe, Paytm)",
                        icon = Icons.Default.QrCode,
                        selected = selectedMethod == "UPI",
                        onClick = { viewModel.onMethodSelect("UPI") }
                    )
                    PaymentMethodItem(
                        title = "Credit / Debit Card",
                        icon = Icons.Default.CreditCard,
                        selected = selectedMethod == "CARD",
                        onClick = { viewModel.onMethodSelect("CARD") }
                    )
                    PaymentMethodItem(
                        title = "Net Banking",
                        icon = Icons.Default.AccountBalance,
                        selected = selectedMethod == "NETBANKING",
                        onClick = { viewModel.onMethodSelect("NETBANKING") }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = viewModel::processDeposit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = uiState !is DepositUiState.Loading
                ) {
                    Text("Proceed to Pay ₹${amount.ifEmpty { "0" }}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (uiState is DepositUiState.Loading) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(100.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickAmountChip(amount: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = AssistChipDefaults.assistChipBorder(enabled = true),
        modifier = Modifier.height(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(amount, fontSize = 14.sp)
        }
    }
}

@Composable
fun PaymentMethodItem(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = AssistChipDefaults.assistChipBorder(enabled = true),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(icon, contentDescription = null, tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray)
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
            RadioButton(selected = selected, onClick = onClick)
        }
    }
}
