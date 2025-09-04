package com.travel.uzoefuapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.businessActivities.BusinessProfileActivity
import com.travel.uzoefuapp.databinding.ActivityCompleteBusinessProfileSetUpBinding

class CompleteBusinessProfileSetUpActivity : AppCompatActivity() {
    lateinit var binding: ActivityCompleteBusinessProfileSetUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        binding = ActivityCompleteBusinessProfileSetUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        playBackgroundVideo()


        binding.btnCompleteSetup.setOnClickListener {
            val intent = Intent(this@CompleteBusinessProfileSetUpActivity, BusinessProfileActivity::class.java)
            startActivity(intent)
        }

    }

    @Suppress("DEPRECATION")
    private fun makeFullScreen() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = android.graphics.Color.TRANSPARENT
    }

    private fun playBackgroundVideo() {
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.onboard1}")

        binding.topVideo.setVideoURI(videoUri)
        binding.topVideo.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
    }
}