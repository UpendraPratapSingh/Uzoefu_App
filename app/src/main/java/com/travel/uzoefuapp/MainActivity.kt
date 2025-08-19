package com.travel.uzoefuapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.travel.uzoefuapp.dashboard.DashboardActivity
import com.travel.uzoefuapp.databinding.ActivityMainBinding
import com.travel.uzoefuapp.onboardScreen.OnboardActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var splashAnimation: Animation
    private lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)
        binding.ivLogo.animation = splashAnimation
        activity = this

        splashAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(activity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 1500) // 1500 milliseconds = 1.5 seconds delay
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
    }
}