package com.travel.uzoefuapp.companyActivities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.Action
import com.travel.uzoefuapp.adapter.ActionAdapter
import com.travel.uzoefuapp.adapter.ProductSliderAdapter
import com.travel.uzoefuapp.adapter.ProductTabAdapter
import com.travel.uzoefuapp.adapter.SliderAdapter
import com.travel.uzoefuapp.bookingActivities.BookSummaryActivity
import com.travel.uzoefuapp.bookingActivities.BookingDetailStep1Activity
import com.travel.uzoefuapp.databinding.ActivityBookingProductBinding
import me.relex.circleindicator.CircleIndicator3

class BookingProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookingProductBinding

    private lateinit var viewPager: ViewPager2
    private lateinit var indicator: CircleIndicator3

    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    private val slideRunnable = object : Runnable {
        override fun run() {
            val itemCount = viewPager.adapter?.itemCount ?: 0
            if (itemCount > 0) {
                currentPage = (currentPage + 1) % itemCount
                viewPager.setCurrentItem(currentPage, true)
            }
            handler.postDelayed(this, 3000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            statusBarColor = Color.TRANSPARENT
        }

        binding.btnBack.setOnClickListener { finish() }

        binding.button2.setOnClickListener {
            val intent = Intent(this@BookingProductActivity, BookingDetailStep1Activity::class.java)
            startActivity(intent)
        }


        viewPager = findViewById(R.id.viewPager)
        indicator = findViewById(R.id.dotsIndicator)

        val images = listOf(R.drawable.balloonslide, R.drawable.balloon, R.drawable.balloonslide)

        viewPager.adapter = SliderAdapter(images)
        indicator.setViewPager(viewPager)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }
        })

        binding.productRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val sampleImages = listOf(
            R.drawable.balloonslide, R.drawable.product, R.drawable.birds, R.drawable.product,
            R.drawable.balloon, R.drawable.product, R.drawable.product
        )

        val thumbnailAdapter = ProductSliderAdapter(sampleImages) { position ->
            // viewPagerImages.setCurrentItem(position, true)
        }
        binding.productRecyclerView.adapter = thumbnailAdapter

        binding.actionRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val actions = listOf(
            Action(R.drawable.ic_copy, "Call"),
            Action(R.drawable.ic_copy, "Map"),
            Action(R.drawable.ic_copy, "Add to Trip"),
            Action(R.drawable.ic_copy, "Share")
        )

        val actionAdapter = ActionAdapter(actions) { action ->
            when (action.label) {
                "Call" -> {
                }

                "Map" -> {
                }

                "Add to Trip" -> {
                }

                "Share" -> {
                }
            }
        }

        binding.actionRecyclerView.adapter = actionAdapter

        val adapter = ProductTabAdapter(this)
        binding.viewPagerData.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPagerData) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Information"
                2 -> "Reviews"
                3 -> "FAQ"
                else -> ""
            }
        }.attach()

        binding.viewPagerData.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // Hide Book button on Reviews tab (position 2)
                binding.button2.visibility = if (position == 2) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        })
    }


    override fun onResume() {
        super.onResume()
        handler.postDelayed(slideRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(slideRunnable)
    }

}