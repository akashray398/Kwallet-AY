package com.example.kwalletay.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector

enum class BillType(val title: String, val icon: ImageVector) {
    ELECTRICITY("Electricity", Icons.Default.ElectricBolt),
    MOBILE("Mobile Recharge", Icons.Default.PhoneAndroid),
    WATER("Water", Icons.Default.WaterDrop)
}

data class Bill(
    val id: String,
    val consumerName: String,
    val amount: Double,
    val dueDate: String,
    val billType: BillType
)
