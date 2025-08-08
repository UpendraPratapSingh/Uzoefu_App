package com.travel.uzoefuapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.travel.uzoefuapp.databinding.ActivityCreateBusinessAccountBinding

class CreateBusinessAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateBusinessAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBusinessAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}