package com.example.kwalletay.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kwalletay.data.model.BillType
import com.example.kwalletay.ui.viewmodel.PayBillViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayBillScreen(
    onBackClick: () -> Unit,
    onSuccess: (Int) -> Unit,
    viewModel: PayBillViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.paymentStatus) {
        if (uiState.paymentStatus == "SUCCESS") {
            // In a real app, the repository would return the inserted ID. 
            // Here we use a dummy or let the ViewModel provide it.
            // For now, let's assume we navigate to history or a generic success if ID isn't easily available.
            // Since we want the flow to SuccessScreen, we'd need the ID.
            // For mock purposes, let's use a hardcoded or random ID if the ViewModel doesn't have it.
            onSuccess(1) // Placeholder ID
            viewModel.resetStatus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pay Bill") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select Bill Type",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BillType.entries.forEach { type ->
                    val isSelected = uiState.selectedBillType == type
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { viewModel.onBillTypeSelect(type) }
                            .padding(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(60.dp)
                        ) {
                            Icon(
                                imageVector = type.icon,
                                contentDescription = type.title,
                                modifier = Modifier.padding(12.dp),
                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = type.title,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }
            }

            OutlinedTextField(
                value = uiState.consumerNumber,
                onValueChange = { viewModel.onConsumerNumberChange(it) },
                label = { Text("Consumer Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.fetchBill() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading && uiState.bill == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Fetch Bill")
                }
            }

            uiState.error?.let {
                Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            uiState.bill?.let { bill ->
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Bill Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        BillDetailRowComponent("Consumer", bill.consumerName)
                        BillDetailRowComponent("Amount", "₹${bill.amount}")
                        BillDetailRowComponent("Due Date", bill.dueDate)
                        BillDetailRowComponent("Type", bill.billType.title)

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.payBill() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            enabled = !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Pay Now ₹${bill.amount}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BillDetailRowComponent(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Medium)
    }
}
