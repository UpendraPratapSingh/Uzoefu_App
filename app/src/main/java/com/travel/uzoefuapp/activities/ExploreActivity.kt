package com.travel.uzoefuapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.ExploreAdapter
import com.travel.uzoefuapp.adapter.ExploreResultAdapter
import com.travel.uzoefuapp.databinding.ActivityExploreBinding

class ExploreActivity : AppCompatActivity() {
    lateinit var binding: ActivityExploreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.forYouArrowImg.setOnClickListener { finish() }

        binding.destinationRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.destinationRecycler.adapter = ExploreAdapter(this)

        binding.categoriesRecycler.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding.categoriesRecycler.adapter = ExploreResultAdapter(this)

    }
}