package com.travel.uzoefuapp.onboardScreen

import OnboardingAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.OnboardingItem
import com.travel.uzoefuapp.databinding.ActivityOnboardBinding
import me.relex.circleindicator.CircleIndicator

class OnboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardBinding
    private lateinit var adapter: OnboardingAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var indicator: CircleIndicator
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    // ✅ Runnable to auto-slide pages
    private val slideRunnable = object : Runnable {
        override fun run() {
            val itemCount = adapter.count
            currentPage = (currentPage + 1) % itemCount
            binding.viewPager.setCurrentItem(currentPage, true)
            handler.postDelayed(this, 3000) // 3 seconds interval
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = findViewById(R.id.viewPager)
        indicator = findViewById(R.id.dotsIndicator)

        val onboardingItems = listOf(
            OnboardingItem(R.raw.onboard1, "Explore", "Uncover hidden gems\n and popular activities"),
            OnboardingItem(R.raw.onboard2, "Discover", "Find exciting things to\n do nearby"),
            OnboardingItem(R.raw.onboard3, "Experience", "Enjoy fun family\n friendly activities"),
            OnboardingItem(R.raw.onboard4, "Wishlist", "Save your future\n adventures with ease")
        )

        adapter = OnboardingAdapter(this, onboardingItems, viewPager)
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)

        startAutoSlide()

    }

    private fun startAutoSlide() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (::adapter.isInitialized) {
                    currentPage = (currentPage + 1) % adapter.count
                    viewPager.setCurrentItem(currentPage, true)
                    handler.postDelayed(this, 4000)
                }
            }
        }, 4000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
