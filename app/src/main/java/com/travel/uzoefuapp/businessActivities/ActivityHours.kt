package com.travel.uzoefuapp.businessActivities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.ActiveHoursAdapter
import com.travel.uzoefuapp.adapter.DayAvailability
import com.travel.uzoefuapp.adapter.DayItem
import com.travel.uzoefuapp.adapter.DaysAdapter
import com.travel.uzoefuapp.databinding.ActivityHoursBinding

class ActivityHours : AppCompatActivity() {
    private lateinit var binding: ActivityHoursBinding
    private lateinit var adapterDays: DaysAdapter
    private lateinit var adapterHours: ActiveHoursAdapter

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

        val dayList = mutableListOf(
            DayItem("Mon"), DayItem("Tue"), DayItem("Wed"),
            DayItem("Thu"), DayItem("Fri"), DayItem("Sat"),
            DayItem("Sun"), DayItem("Public Holidays")
        )

        adapterDays = DaysAdapter(this,  dayList) { day, pos ->
            Toast.makeText(this, "${day.dayName} selected: ${day.isSelected}", Toast.LENGTH_SHORT)
                .show()
        }

        binding.recyclerDays.layoutManager = GridLayoutManager(this, 5)
        binding.recyclerDays.adapter = adapterDays

        val hoursList = mutableListOf(
            DayAvailability("Mon"),
            DayAvailability("Tue"),
            DayAvailability("Wed"),
            DayAvailability("Thu"),
            DayAvailability("Fri"),
            DayAvailability("Sat"),
            DayAvailability("Sun"),
            DayAvailability("Public Holidays")
        )

        adapterHours = ActiveHoursAdapter(this, hoursList)

        binding.recyclerDaysPrint.layoutManager = LinearLayoutManager(this)
        binding.recyclerDaysPrint.adapter = adapterHours

        binding.recyclerDaysPrint.visibility = View.VISIBLE
        binding.textHoursView4.text = "Select the availability times for the activity"
        binding.recyclerDays.visibility = View.GONE
        binding.switchSameTime.isChecked = false

        binding.switchSameTime.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.recyclerDays.visibility = View.VISIBLE
                binding.textHoursView4.text = "Select which days you are open and hours."
                binding.recyclerDaysPrint.visibility = View.GONE

            } else {
                binding.recyclerDays.visibility = View.GONE
                binding.recyclerDaysPrint.visibility = View.VISIBLE
                binding.textHoursView4.text = "Select the availability times for the activity"

            }
        }
    }
}
