package com.travel.uzoefuapp.businessActivities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.ActivityBusinessProfileBinding

class BusinessProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityBusinessProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBusinessProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backArrow.setOnClickListener { finish() }

        binding.stepsContainer.setOnClickListener {
            val intent = Intent(this@BusinessProfileActivity, CompanyDetailActivity::class.java)
            startActivity(intent)
        }

        binding.contactContainer.setOnClickListener {
            val intent = Intent(this@BusinessProfileActivity, ContactActivity::class.java)
            startActivity(intent)
        }

        binding.operatingContainer.setOnClickListener {
            val intent = Intent(this@BusinessProfileActivity, OperatingHoursActivity::class.java)
            startActivity(intent)
        }

        binding.paymentContainer.setOnClickListener {
            val intent = Intent(this@BusinessProfileActivity, PaymentDetailActivity::class.java)
            startActivity(intent)
        }

        binding.finishContainer.setOnClickListener {
            val intent = Intent(this@BusinessProfileActivity, FinishSetUpActivity::class.java)
            startActivity(intent)
        }
    }
}