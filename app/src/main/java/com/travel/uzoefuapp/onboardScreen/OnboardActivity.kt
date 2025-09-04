package com.travel.uzoefuapp.onboardScreen

import com.travel.uzoefuapp.adapter.OnboardingAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.CreateAccountActivity
import com.travel.uzoefuapp.activities.SelectUserTypeActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = findViewById(R.id.viewPager)
        indicator = findViewById(R.id.dotsIndicator)

        val onboardingItems = listOf(
            OnboardingItem(R.raw.onboard1, "Explore","Uncover hidden gems\n and popular activities"),
            OnboardingItem(R.raw.onboard2, "Discover", "Find exciting things to\n do nearby"),
            OnboardingItem(R.raw.onboard3, "Experience", "Enjoy fun family\n friendly activities"),
            OnboardingItem(R.raw.onboard4, "Wishlist", "Save your future\n adventures with ease")
        )

        adapter = OnboardingAdapter(this, onboardingItems, viewPager)
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)

        startAutoSlide()

    }

    @Suppress("DEPRECATION")
    private fun makeFullScreen() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = android.graphics.Color.TRANSPARENT
    }

    private fun startAutoSlide() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (::adapter.isInitialized) {
                    if (currentPage == adapter.count - 1) {
                        startActivity(
                            Intent(
                                this@OnboardActivity,
                                SelectUserTypeActivity::class.java
                            )
                        )
                        finish()
                    } else {
                        currentPage++
                        viewPager.setCurrentItem(currentPage, true)
                        handler.postDelayed(this, 4000)
                    }
                }
            }
        }, 4000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
