package com.travel.uzoefuapp.businessActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.ActiveHoursAdapter
import com.travel.uzoefuapp.adapter.DayAvailability
import com.travel.uzoefuapp.databinding.ActivityHoursBinding

class ActivityHours : AppCompatActivity() {
    lateinit var binding: ActivityHoursBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHoursBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backArrow.setOnClickListener { finish() }

        val recyclerDays = findViewById<RecyclerView>(R.id.recyclerDays)

        val daysList = mutableListOf(
            DayAvailability("Mon"),
            DayAvailability("Tue"),
            DayAvailability("Wed"),
            DayAvailability("Thu"),
            DayAvailability("Fri"),
            DayAvailability("Sat"),
            DayAvailability("Sun"),
            DayAvailability("Public Holidays")
        )

        val adapter = ActiveHoursAdapter(this, daysList)
        recyclerDays.adapter = adapter

    }
}