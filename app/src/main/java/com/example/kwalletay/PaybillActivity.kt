package com.example.kwalletay

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kwalletay.databinding.ActivityPaybillBinding

class PaybillActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaybillBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaybillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.confirmBtn.setOnClickListener {
            val billId = binding.billIdEt.text.toString()
            val amount = binding.amountEt.text.toString()

            if (billId.isNotEmpty() && amount.isNotEmpty()) {
                Toast.makeText(this, "Bill $billId of ₹$amount paid successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
