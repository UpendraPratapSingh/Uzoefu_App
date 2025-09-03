package com.travel.uzoefuapp.businessActivities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.AmenityPageAdapter
import com.travel.uzoefuapp.databinding.ActivityAmenitiesBinding
import me.relex.circleindicator.CircleIndicator3

class AmenitiesActivity : AppCompatActivity() {
    lateinit var binding: ActivityAmenitiesBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var indicator: CircleIndicator3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAmenitiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backArrow.setOnClickListener { finish() }

        viewPager = findViewById(R.id.viewPager)
        indicator = binding.indicator

        val amenitiesPages = listOf(
            listOf("Accessibility", "Baths", "Beauty Salon", "Bike Paths", "Beach", "Braai Area", "Printing", "Office Services", "Parking", "Play Area", "Swimming Pool", "Wifi", "PicNic Area"),
            listOf("Clinic", "Club House", "Community Centre", "Eatery Spa", "Fitness Centre", "Gardens"),
            listOf("Childcare", "Clean Air", "Gym", "Hair Salon", "Childcare", "Gardens", "Fitness Centre", "Club House")
        )

        val adapter = AmenityPageAdapter(this, amenitiesPages)
        viewPager.adapter = adapter

        indicator.setViewPager(viewPager)
    }
}