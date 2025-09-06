package com.travel.uzoefuapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.Category
import com.travel.uzoefuapp.adapter.CategoryAdapter
import com.travel.uzoefuapp.adapter.ExploreResultAdapter
import com.travel.uzoefuapp.adapter.SelectPriceAdapter
import com.travel.uzoefuapp.adapter.SelectedDestinationAdapter
import com.travel.uzoefuapp.databinding.ActivitySelectDestinationBinding

class SelectDestinationActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectDestinationBinding

    private val categoriesList = listOf(
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
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySelectDestinationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.forYouArrowImg.setOnClickListener { finish() }

        val categoryName = intent.getStringExtra("categoryName")
        val Name = intent.getStringExtra("Name")
        val type = intent.getStringExtra("type")
        binding.textView.text = Name


        if (type == "1") {
            binding.textView.text = Name
        } else {
            binding.textView.text = categoryName
        }

        val name = intent.getStringExtra("Name").toString()

        Log.e("UserDestination", "user destination : $name , $type ")

        if (type == "1") {
            binding.textView.text = name ?: ""       // show Name if type is 1
        } else {
            binding.textView.text = categoryName ?: "" // otherwise show categoryName
        }


        binding.filterData.setOnClickListener { showFilterPopup() }

        binding.destinationRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.destinationRecycler.adapter = SelectedDestinationAdapter(this)

        binding.destinationRecyclerView.layoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding.destinationRecyclerView.adapter = ExploreResultAdapter(this)

    }

    @SuppressLint("MissingInflatedId")
    private fun showFilterPopup() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.filter_bottom_sheet, null)
        bottomSheetDialog.setContentView(view)

        bottomSheetDialog.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val btnApply = view.findViewById<Button>(R.id.btnApplyFilters)
        val backIcon = view.findViewById<ImageView>(R.id.imageView)
        val closePopup = view.findViewById<ImageView>(R.id.closePopup)

        val ivToggleDistance = view.findViewById<ConstraintLayout>(R.id.distanceLayout)
        val categoryLayout = view.findViewById<ConstraintLayout>(R.id.categoryLayout)
        val ratingLayout = view.findViewById<ConstraintLayout>(R.id.ratingLayout)
        val priceLayout = view.findViewById<ConstraintLayout>(R.id.priceLayout)

        val plusImageView = view.findViewById<ImageView>(R.id.plusImageView)
        val plusCategory = view.findViewById<ImageView>(R.id.plusCategory)
        val plusRating = view.findViewById<ImageView>(R.id.plusRating)
        val plusPrice = view.findViewById<ImageView>(R.id.plusPrice)
        val layoutCityRadius = view.findViewById<LinearLayout>(R.id.layoutCityRadius)
        val categoriesSection = view.findViewById<ConstraintLayout>(R.id.categoriesSection)
        val ratingFilterContainer = view.findViewById<LinearLayout>(R.id.ratingFilterContainer)

        val rvCategories = view.findViewById<RecyclerView>(R.id.rvCategories)
        val rvSelectPrice = view.findViewById<RecyclerView>(R.id.rvSelectPrice)

        val spinnerCity = view.findViewById<AppCompatSpinner>(R.id.spinnerCity)
        val spinnerRadius = view.findViewById<Spinner>(R.id.spinnerRadius)

        val cbAllRatings = view.findViewById<CheckBox>(R.id.cbAllRatings)
        val cbRating1 = view.findViewById<CheckBox>(R.id.cbRating1)
        val cbRating2 = view.findViewById<CheckBox>(R.id.cbRating2)
        val cbRating3 = view.findViewById<CheckBox>(R.id.cbRating3)
        val cbRating4 = view.findViewById<CheckBox>(R.id.cbRating4)
        val cbRating5 = view.findViewById<CheckBox>(R.id.cbRating5)

        val ratingCheckboxes = listOf(cbRating1, cbRating2, cbRating3, cbRating4, cbRating5)

        cbAllRatings.setOnCheckedChangeListener { _, isChecked ->
            ratingCheckboxes.forEach { it.isChecked = isChecked }
        }

        ratingCheckboxes.forEach { cb ->
            cb.setOnCheckedChangeListener { _, _ ->
                cbAllRatings.isChecked = ratingCheckboxes.all { it.isChecked }
            }
        }

        val cities = arrayOf("Select City", "Johannesburg", "Cape Town", "Durban", "Pretoria")
        val cityAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cities)
        spinnerCity.adapter = cityAdapter

        var selectedCity = ""
        spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCity = if (position > 0) cities[position] else ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val radiusOptions = arrayOf(
            "Select Radius",
            "2 Kilometres",
            "5 Kilometres",
            "10 Kilometres",
            "20 Kilometres"
        )

        val radiusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, radiusOptions)
        spinnerRadius.adapter = radiusAdapter

        var selectedRadius = ""
        spinnerRadius.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedRadius = if (position > 0) radiusOptions[position] else ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        ivToggleDistance.setOnClickListener {
            if (layoutCityRadius.visibility == View.GONE) {
                layoutCityRadius.visibility = View.VISIBLE
                plusImageView.setImageResource(R.drawable.baseline_remove)
            } else {
                layoutCityRadius.visibility = View.GONE
                plusImageView.setImageResource(R.drawable.baseline_add_24)
            }
        }

        categoryLayout.setOnClickListener {
            if (categoriesSection.visibility == View.GONE) {
                categoriesSection.visibility = View.VISIBLE
                plusCategory.setImageResource(R.drawable.baseline_remove)
            } else {
                categoriesSection.visibility = View.GONE
                plusCategory.setImageResource(R.drawable.baseline_add_24)
            }
        }

        ratingLayout.setOnClickListener {
            if (ratingFilterContainer.visibility == View.GONE) {
                ratingFilterContainer.visibility = View.VISIBLE
                plusRating.setImageResource(R.drawable.baseline_remove)
            } else {
                ratingFilterContainer.visibility = View.GONE
                plusRating.setImageResource(R.drawable.baseline_add_24)
            }
        }

        priceLayout.setOnClickListener {
            if (rvSelectPrice.visibility == View.GONE) {
                rvSelectPrice.visibility = View.VISIBLE
                plusPrice.setImageResource(R.drawable.baseline_remove)
            } else {
                rvSelectPrice.visibility = View.GONE
                plusPrice.setImageResource(R.drawable.baseline_add_24)
            }
        }

        rvCategories.layoutManager = GridLayoutManager(this, 3)
        rvCategories.adapter = CategoryAdapter(this, categoriesList)


        rvSelectPrice.layoutManager = GridLayoutManager(this, 1)
        rvSelectPrice.adapter = SelectPriceAdapter(this)

        /*   binding.btnApply.setOnClickListener {
               val selectedPrices = adapter.getSelectedFilters()
               Toast.makeText(requireContext(), "Selected: $selectedPrices", Toast.LENGTH_SHORT).show()
           }*/

        backIcon.setOnClickListener { bottomSheetDialog.dismiss() }
        closePopup.setOnClickListener { bottomSheetDialog.dismiss() }

        btnApply.setOnClickListener {
            val intent = Intent(this@SelectDestinationActivity, ExploreActivity::class.java)
            startActivity(intent)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }
}