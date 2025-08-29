package com.travel.uzoefuapp.companyActivities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.ProductAdapter
import com.travel.uzoefuapp.adapter.SliderAdapter
import com.travel.uzoefuapp.databinding.ActivityCompanyLandingBinding
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import me.relex.circleindicator.CircleIndicator3

class CompanyLandingActivity : AppCompatActivity() {
    lateinit var binding: ActivityCompanyLandingBinding

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
        binding = ActivityCompanyLandingBinding.inflate(layoutInflater)
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

        binding.btnMore.setOnClickListener {
            val intent = Intent(this@CompanyLandingActivity, SettingsActivity::class.java)
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

        binding.landingRecyclerView.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.landingRecyclerView.adapter = ProductAdapter(this)

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