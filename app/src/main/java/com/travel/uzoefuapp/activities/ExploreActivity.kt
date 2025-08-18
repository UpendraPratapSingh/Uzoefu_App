package com.travel.uzoefuapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.adapter.CategoriesAdapter
import com.travel.uzoefuapp.adapter.DestinationAdapter
import com.travel.uzoefuapp.databinding.ActivityExploreBinding

class ExploreActivity : AppCompatActivity() {
    lateinit var binding: ActivityExploreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forYouArrowImg.setOnClickListener { finish() }

        binding.destinationRecycler.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.destinationRecycler.adapter = DestinationAdapter(this)

        binding.categoriesRecycler.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.categoriesRecycler.adapter = CategoriesAdapter(this)

    }
}