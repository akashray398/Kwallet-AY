package com.example.kwalletay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kwalletay.Adapter.TransectionAdapter
import com.example.kwalletay.Model.Transection
import com.example.kwalletay.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val transactions = listOf(
            Transection("Add to Wallet", "15 Oct 2023", 923.2),
            Transection("Transaction to Akash Yadav", "14 Oct 2023", -576.2),
            Transection("Salary Deposit", "10 Oct 2023", 2500.0),
            Transection("Netflix Subscription", "08 Oct 2023", -15.99),
            Transection("Starbucks", "07 Oct 2023", -6.50),
            Transection("Credited by Khushi", "05 Oct 2023", 25.0),
            Transection("Cashback Reward", "05 Oct 2023", 25.0),
            Transection("Grocery Store", "04 Oct 2023", -45.0),
            Transection("Electricity Bill", "02 Oct 2023", -120.0),
            Transection("Internet Bill", "01 Oct 2023", -60.0)
        )

        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = TransectionAdapter(transactions)
        }
    }
}
