package com.example.kwalletay

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kwalletay.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.accountBtn.setOnClickListener {
            Toast.makeText(this, "Account Information clicked", Toast.LENGTH_SHORT).show()
        }

        binding.securityBtn.setOnClickListener {
            Toast.makeText(this, "Security settings clicked", Toast.LENGTH_SHORT).show()
        }

        binding.notifSettingsBtn.setOnClickListener {
            Toast.makeText(this, "Notification settings clicked", Toast.LENGTH_SHORT).show()
        }

        binding.privacyBtn.setOnClickListener {
            Toast.makeText(this, "Privacy Policy clicked", Toast.LENGTH_SHORT).show()
        }

        binding.helpBtn.setOnClickListener {
            Toast.makeText(this, "Help & Support clicked", Toast.LENGTH_SHORT).show()
        }

        binding.logoutBtn.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
