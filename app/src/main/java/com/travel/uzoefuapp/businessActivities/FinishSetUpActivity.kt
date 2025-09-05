package com.travel.uzoefuapp.businessActivities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.businessAccount.BusinessAccountActivity
import com.travel.uzoefuapp.databinding.ActivityFinishSetUpBinding

class FinishSetUpActivity : AppCompatActivity() {
    lateinit var binding: ActivityFinishSetUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFinishSetUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backArrow.setOnClickListener { finish() }

        binding.btnViewActivities.setOnClickListener {
            val intent = Intent(this@FinishSetUpActivity, BusinessAccountActivity::class.java)
            intent.putExtra("openFragment", "profile")
            startActivity(intent)
        }
        
        binding.btnSave.setOnClickListener {
            val intent = Intent(this@FinishSetUpActivity, AddActivityActivity::class.java)
            startActivity(intent)
        }
    }
}