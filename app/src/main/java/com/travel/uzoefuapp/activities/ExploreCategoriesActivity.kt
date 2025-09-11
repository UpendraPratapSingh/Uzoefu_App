package com.travel.uzoefuapp.activities

import android.content.Intent
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
import com.travel.uzoefuapp.notification.NotificationActivity

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

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this@ExploreCategoriesActivity, NotificationActivity::class.java)
            startActivity(intent)
        }

        val categoriesList = listOf(
            Category("Near Me", 400, R.drawable.ic_location),
            Category("Adventure", 600, R.drawable.adventure),
            Category("Culture", 450, R.drawable.culture),
            Category("Food", 1700, R.drawable.food),
            Category("Entertainment", 350, R.drawable.entertainment),
            Category("Family Fun", 18, R.drawable.family_fun),
            Category("Services", 250, R.drawable.local_service),
            Category("Religion", 66, R.drawable.religion),
            Category("Outdoors", 131, R.drawable.outdoor_adventures),
            Category("Wildlife", 65, R.drawable.wildlife),
            Category("Wellness", 50, R.drawable.wellness),
            Category("Historical", 67, R.drawable.historical),
            Category("Sport", 47, R.drawable.sports),
            Category("Urban", 32, R.drawable.urban_discovery),
            Category("Nature", 200, R.drawable.nature),
            Category("Tours", 123, R.drawable.tours)
        )

        binding.categoriesRecycler.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.categoriesRecycler.adapter = CategoriesAdapter(this, categoriesList)

    }
}