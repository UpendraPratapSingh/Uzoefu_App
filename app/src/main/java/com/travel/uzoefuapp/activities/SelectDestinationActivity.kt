package com.travel.uzoefuapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.ExploreResultAdapter
import com.travel.uzoefuapp.adapter.SelectedDestinationAdapter
import com.travel.uzoefuapp.databinding.ActivitySelectDestinationBinding

class SelectDestinationActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectDestinationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectDestinationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.forYouArrowImg.setOnClickListener { finish() }

        val categoryName = intent.getStringExtra("categoryName")
        val Name = intent.getStringExtra("Name")
        val type = intent.getStringExtra("type")

        if (type == "1"){
            binding.textView.text = Name
        }else{
            binding.textView.text = categoryName
        }

        binding.destinationRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.destinationRecycler.adapter = SelectedDestinationAdapter(this)

        binding.destinationRecyclerView.layoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding.destinationRecyclerView.adapter = ExploreResultAdapter(this)

    }
}