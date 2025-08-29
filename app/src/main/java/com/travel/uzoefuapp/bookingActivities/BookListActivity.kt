package com.travel.uzoefuapp.bookingActivities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.BookingTabAdapter
import com.travel.uzoefuapp.databinding.ActivityBookListBinding

class BookListActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.forYouArrowImg.setOnClickListener { finish() }

        val adapter = BookingTabAdapter(this)
        binding.viewPager.adapter = adapter


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Active"
                1 -> "Past"
                2 -> "Cancelled"
                else -> ""
            }
        }.attach()
    }

}