package com.example.kwalletay.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kwalletay.data.model.Recipient
import com.example.kwalletay.ui.viewmodel.TransferViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    viewModel: TransferViewModel,
    onBackClick: () -> Unit,
    onSuccess: (Int) -> Unit,
    onFailure: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.successTransactionId) {
        uiState.successTransactionId?.let { id ->
            onSuccess(id)
            viewModel.resetState()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            if (!uiState.showConfirmation && !uiState.isLoading) {
                // We might want to show this via onFailure if it's a fatal error
                // but for field validation errors we usually show it in the UI.
                // Let's assume FATAL errors (like API failure) should go to onFailure screen.
                // For now, let's just keep field validation in UI and potentially move others.
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transfer Money", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Balance Header
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Current Balance", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                            Text("₹${String.format("%.2f", uiState.balance)}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Recipient Input
                Text("Recipient", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                OutlinedTextField(
                    value = uiState.recipient,
                    onValueChange = { viewModel.onRecipientChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter UPI ID or Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(16.dp))

                // Saved Recipients
                Text("Saved Recipients", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(uiState.savedRecipients) { recipient ->
                        RecipientItem(recipient = recipient) {
                            viewModel.onRecipientSelected(recipient)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Amount and Note
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = uiState.amount,
                        onValueChange = { viewModel.onAmountChange(it) },
                        modifier = Modifier.weight(1f),
                        label = { Text("Amount") },
                        prefix = { Text("₹") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = uiState.note,
                        onValueChange = { viewModel.onNoteChange(it) },
                        modifier = Modifier.weight(1.5f),
                        label = { Text("Note (Optional)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Transfer Button
                Button(
                    onClick = { viewModel.initiateTransfer() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Transfer Now", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Error Message
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 90.dp)
                )
            }

            // Loading Overlay
            if (uiState.isLoading) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            }
        }
    }

    // Confirmation Dialog
    if (uiState.showConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissConfirmation() },
            title = { Text("Confirm Transfer") },
            text = {
                Column {
                    Text("You are sending ₹${uiState.amount} to:")
                    Text(uiState.recipient, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    if (uiState.note.isNotBlank()) {
                        Text("Note: ${uiState.note}", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.confirmTransfer() }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissConfirmation() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun RecipientItem(recipient: Recipient, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = recipient.avatarUrl ?: "https://ui-avatars.com/api/?name=${recipient.name}&background=random",
            contentDescription = null,
            modifier = Modifier.size(48.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(recipient.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(recipient.identifier, color = Color.Gray, fontSize = 14.sp)
        }
    }
}
