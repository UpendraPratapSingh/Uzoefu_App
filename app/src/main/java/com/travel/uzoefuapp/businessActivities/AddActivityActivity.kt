package com.travel.uzoefuapp.businessActivities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.ActivityAddActivityBinding

class AddActivityActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backArrow.setOnClickListener { finish() }

        binding.stepsContainer.setOnClickListener {
            val intent = Intent(this@AddActivityActivity, ActivityDetail::class.java)
            startActivity(intent)
        }

        binding.contactContainer.setOnClickListener {
            val intent = Intent(this@AddActivityActivity, ActivityDecriptionActivity::class.java)
            startActivity(intent)
        }
    }
}