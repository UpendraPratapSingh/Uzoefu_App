package com.travel.uzoefuapp.businessActivities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.ActivityPaymentDetailBinding

class PaymentDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityPaymentDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backArrow.setOnClickListener { finish() }

        val bankNames = listOf("Axis Bank", "ICICI Bank", "HDFC Bank", "SBI Bank")

        val bankDropdown = findViewById<AutoCompleteTextView>(R.id.spinnerBankName)

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bankNames)
        bankDropdown.setAdapter(adapter)

    }
}