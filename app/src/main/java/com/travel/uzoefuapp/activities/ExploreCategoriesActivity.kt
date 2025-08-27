package com.travel.uzoefuapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.CategoriesAdapter
import com.travel.uzoefuapp.adapter.Category
import com.travel.uzoefuapp.databinding.ActivityExploreCategoriesBinding

class ExploreCategoriesActivity : AppCompatActivity() {
    lateinit var binding: ActivityExploreCategoriesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExploreCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.forYouArrowImg.setOnClickListener { finish() }

        val categoriesList = listOf(
            Category("Near Me", 400, R.drawable.tours),
            Category("Adventure", 600, R.drawable.tours),
            Category("Culture", 450, R.drawable.tours),
            Category("Food", 1700, R.drawable.tours),
            Category("Entertainment", 350, R.drawable.tours),
            Category("Family Fun", 18, R.drawable.tours),
            Category("Services", 250, R.drawable.tours),
            Category("Religion", 66, R.drawable.tours),
            Category("Outdoors", 131, R.drawable.tours),
            Category("Wildlife", 65, R.drawable.tours),
            Category("Wellness", 50, R.drawable.tours),
            Category("Historical", 67, R.drawable.tours),
            Category("Sport", 47, R.drawable.tours),
            Category("Urban", 32, R.drawable.tours),
            Category("Nature", 200, R.drawable.tours),
            Category("Tours", 123, R.drawable.tours)
        )

        binding.categoriesRecycler.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.categoriesRecycler.adapter = CategoriesAdapter(this, categoriesList)

    }
}