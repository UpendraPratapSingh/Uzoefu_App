package com.travel.uzoefuapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.adapter.CategoriesAdapter
import com.travel.uzoefuapp.databinding.ActivityExploreCategoriesBinding

class ExploreCategoriesActivity : AppCompatActivity() {
    lateinit var binding: ActivityExploreCategoriesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forYouArrowImg.setOnClickListener { finish() }

        binding.categoriesRecycler.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.categoriesRecycler.adapter = CategoriesAdapter(this)

    }
}