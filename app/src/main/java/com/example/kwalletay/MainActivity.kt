package com.example.kwalletay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kwalletay.Adapter.TransectionAdapter
import com.example.kwalletay.Model.Transection
import com.example.kwalletay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBottomNavigation()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        val transactions = listOf(
            Transection("Add to Wallet", "15 Oct 2023", 923.2),
            Transection("Transaction to Akash Yadav", "14 Oct 2023", -576.2),
            Transection("Salary Deposit", "10 Oct 2023", 2500.0),
            Transection("Netflix Subscription", "08 Oct 2023", -15.99),
            Transection("Starbucks", "07 Oct 2023", -6.50),
            Transection("Credited by Khushi", "05 Oct 2023", 25.0),
            Transection("Cashback Reward", "05 Oct 2023", 25.0)
        )

        binding.transectionView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = TransectionAdapter(transactions)
        }
    }

    private fun setupClickListeners() {
        binding.settingsImg.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.notificationImg.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        binding.profileImg.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.seeAllBtn.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomMenu.apply {
            setItemSelected(R.id.home, true)
            setOnItemSelectedListener { id ->
                when (id) {
                    R.id.home -> {
                        // Already on Home
                    }
                    R.id.explorer -> {
                        Toast.makeText(this@MainActivity, "Explorer clicked", Toast.LENGTH_SHORT).show()
                    }
                    R.id.history -> {
                        startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
                    }
                    R.id.profile -> {
                        startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                    }
                }
            }
        }
    }
}
