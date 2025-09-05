package com.travel.uzoefuapp.businessAccount

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.BusinessTabAdapter
import com.travel.uzoefuapp.databinding.ActivityBusinessAccountBinding

class BusinessAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusinessAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBusinessAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = BusinessTabAdapter(this)
        binding.viewPager.adapter = adapter

        binding.viewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Profile"
                2 -> "Activities"
                3 -> "Booking"
                else -> null
            }
        }.attach()

        val openFragment = intent.getStringExtra("openFragment")
        if (openFragment == "profile") {
            binding.viewPager.setCurrentItem(1, false)
        }
    }
}
