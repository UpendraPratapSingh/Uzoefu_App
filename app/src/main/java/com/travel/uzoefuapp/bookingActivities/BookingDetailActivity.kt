package com.travel.uzoefuapp.bookingActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.travel.uzoefuapp.databinding.ActivityBookingDetailBinding

class BookingDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookingDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}