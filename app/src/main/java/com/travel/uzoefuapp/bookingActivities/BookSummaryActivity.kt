package com.travel.uzoefuapp.bookingActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.travel.uzoefuapp.databinding.ActivityBookSummaryBinding

class BookSummaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookSummaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}