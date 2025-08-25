package com.travel.uzoefuapp.bookingActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.adapter.BookingTabAdapter
import com.travel.uzoefuapp.databinding.ActivityBookListBinding

class BookListActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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