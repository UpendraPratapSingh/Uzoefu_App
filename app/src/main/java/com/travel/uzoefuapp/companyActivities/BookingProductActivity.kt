package com.travel.uzoefuapp.companyActivities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.Action
import com.travel.uzoefuapp.adapter.ActionAdapter
import com.travel.uzoefuapp.adapter.ProductSliderAdapter
import com.travel.uzoefuapp.adapter.ProductTabAdapter
import com.travel.uzoefuapp.adapter.SliderAdapter
import com.travel.uzoefuapp.bookingActivities.BookingDetailStep1Activity
import com.travel.uzoefuapp.databinding.ActivityBookingProductBinding
import com.travel.uzoefuapp.globalSettings.SettingsActivity
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
        enableEdgeToEdge()
        binding = ActivityBookingProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

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

        binding.btnMore.setOnClickListener {
            val intent = Intent(this@BookingProductActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.tvCategory.setOnClickListener {
            val intent = Intent(this@BookingProductActivity, CompanyLandingActivity::class.java)
            startActivity(intent)
        }

        viewPager = findViewById(R.id.viewPager)
        indicator = findViewById(R.id.dotsIndicator)

        val images =
            listOf(R.drawable.balloonslide, R.drawable.balloonslide, R.drawable.balloonslide)

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
            Action(R.drawable.ic_call, "Call"),
            Action(R.drawable.ic_shared, "Map"),
            Action(R.drawable.ic_copy, "Add to Trip"),
            Action(R.drawable.ic_shared, "Share")
        )

        val actionAdapter = ActionAdapter(actions) { action ->
            when (action.label) {
                "Call" -> {
                    val phone = +919876543210
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$phone")
                    }
                    startActivity(intent)
                }

                "Map" -> {
                    val location = "Taj Mahal, Agra"
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("geo:0,0?q=$location")
                    }
                    startActivity(intent)
                }

                "Add to Trip" -> {
                    Toast.makeText(this, "${action.label} added to trip!", Toast.LENGTH_SHORT)
                        .show()
                }

                "Share" -> {
                    val shareText = "Visit the Taj Mahal!"
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    startActivity(Intent.createChooser(intent, "Share via"))
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

        binding.viewPagerData.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

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