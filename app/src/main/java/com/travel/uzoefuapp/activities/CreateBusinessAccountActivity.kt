package com.travel.uzoefuapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.ActivityCreateBusinessAccountBinding

class CreateBusinessAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBusinessAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBusinessAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}