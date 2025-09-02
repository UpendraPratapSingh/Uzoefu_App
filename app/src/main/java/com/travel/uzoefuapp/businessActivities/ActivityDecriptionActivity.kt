package com.travel.uzoefuapp.businessActivities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.Category
import com.travel.uzoefuapp.adapter.CategoryAdapter
import com.travel.uzoefuapp.databinding.ActivityDecriptionBinding

class ActivityDecriptionActivity : AppCompatActivity() {
    lateinit var binding: ActivityDecriptionBinding

    private val categoriesList = listOf(
        Category("Near Me", 400, R.drawable.tours),
        Category("Culture", 450, R.drawable.tours),
        Category("Food", 1700, R.drawable.tours),
        Category("Services", 250, R.drawable.tours),
        Category("Religion", 66, R.drawable.tours),
        Category("Wildlife", 65, R.drawable.tours),
        Category("Sport", 47, R.drawable.tours),
        Category("Urban", 32, R.drawable.tours),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDecriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backArrow.setOnClickListener { finish() }

        binding.rvSelectPrice.layoutManager = GridLayoutManager(this, 3)
        binding.rvSelectPrice.adapter = CategoryAdapter(this, categoriesList)


    }
}