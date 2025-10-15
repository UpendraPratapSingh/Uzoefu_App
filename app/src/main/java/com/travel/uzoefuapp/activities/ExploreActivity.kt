package com.travel.uzoefuapp.activities

import CustomProgressDialog
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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistBody
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistViewModel
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activityModl.ActivityBody
import com.travel.uzoefuapp.activityModl.ActivityResponse
import com.travel.uzoefuapp.activityModl.ActivityViewModel
import com.travel.uzoefuapp.adapter.CategoryAdapter
import com.travel.uzoefuapp.adapter.ExploreResultAdapter
import com.travel.uzoefuapp.adapter.OnCategoryClickListener
import com.travel.uzoefuapp.adapter.OnWishlistListener
import com.travel.uzoefuapp.adapter.SearchDestinationAdapter
import com.travel.uzoefuapp.adapter.SearchVerticleDestinationAdapter
import com.travel.uzoefuapp.adapter.SelectPriceAdapter
import com.travel.uzoefuapp.adapter.SelectedDestinationAdapter
import com.travel.uzoefuapp.categoryModel.CategoryResponse
import com.travel.uzoefuapp.categoryModel.CategoryViewModel
import com.travel.uzoefuapp.databinding.ActivityExploreBinding
import com.travel.uzoefuapp.filterActivityModel.FilterActivityBody
import com.travel.uzoefuapp.filterActivityModel.FilterActivityResponse
import com.travel.uzoefuapp.filterActivityModel.FilterActivityViewModel
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.provinceModel.ProvinceViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreActivity : AppCompatActivity(), OnWishlistListener, OnCategoryClickListener {
    lateinit var binding: ActivityExploreBinding
    private val activityViewModel: ActivityViewModel by viewModels()
    private val categoryId = ""
    private var rvCategories: RecyclerView? = null
    private var selectedProvinceId: String = ""
    private lateinit var spinnerCity: AppCompatSpinner
    var data: List<ActivityResponse.Datum> = ArrayList()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private val addWishlistViewModel: AddWishlistViewModel by viewModels()
    private val filterActivityViewModel: FilterActivityViewModel by viewModels()
    var filterActivity: List<FilterActivityResponse.Data.Datum> = ArrayList()
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()
    private var selectedPrice: String = ""
    private val provinceViewModel: ProvinceViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    var data1: List<CategoryResponse.Datum> = ArrayList()


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
        val experienceActivity = intent.getStringExtra("ExperienceActivity")

        val selectedCityId = intent.getStringExtra("selectedCity") ?: ""
        val selectedCategoryId = intent.getStringExtra("selectedRadius") ?: ""
        val selectedPrice = intent.getStringExtra("selectedPrice") ?: ""
        val selectedRatings = intent.getStringExtra("selectedRatings") ?: ""

        val source = intent.getStringExtra("source").toString()


        Log.e("FilterData", "City: $selectedCityId")
        Log.e("FilterData", "Radius: $selectedCategoryId")
        Log.e("FilterData", "Price: $selectedPrice")
        Log.e("FilterData", "Ratings: $selectedRatings")
        Log.e("FilterData", "Source: $source ")

        if (source == "filter") {
            getFilterApi(selectedCityId, selectedCategoryId, selectedPrice, selectedRatings)
            getFilterObserver()
            notificationCountApi()
            notificationCountObserver()
        } else {
            getActivityApi(categoryId)
            getActivityObserver()
            notificationCountApi()
            notificationCountObserver()
        }

        if (experienceActivity == "1") {
            binding.resultCons.visibility = View.GONE
        } else {
            binding.resultCons.visibility = View.VISIBLE
        }


        binding.editFilter.setOnClickListener {
            showFilterPopup()
        }

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this@ExploreActivity, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.menuIcon.setOnClickListener {
            val intent = Intent(this@ExploreActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        /*   binding.categoriesRecycler.layoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding.categoriesRecycler.adapter = ExploreResultAdapter(this)*/

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
            decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
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

        rvCategories = view.findViewById(R.id.rvCategories)
        val rvSelectPrice = view.findViewById<RecyclerView>(R.id.rvSelectPrice)

        spinnerCity = view.findViewById(R.id.spinnerCity)
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

        provinceListApi()

        getCategoryBottomSheetApi()
        getCategoryBottomSheetObserver()

        val radiusOptions = arrayOf(
            "Select Radius", "2 Kilometres", "5 Kilometres", "10 Kilometres", "20 Kilometres"
        )

        val radiusAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, radiusOptions)
        spinnerRadius.adapter = radiusAdapter

        var selectedRadius = ""
        spinnerRadius.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
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

        /*    rvCategories.layoutManager = GridLayoutManager(this, 3)
            rvCategories.adapter = CategoryAdapter(this, categoriesList)*/

        val priceRanges = listOf("0 - 150", "151 - 300", "301 - 500", "500+")

        val selectPriceAdapter = SelectPriceAdapter(this, priceRanges) { selectedPrices ->
            // This lambda is called whenever selection changes
            // Send selectedPrices to your API
            selectedPrice = selectedPrices.toString()
        }

        rvSelectPrice.layoutManager = GridLayoutManager(this, 1)
        rvSelectPrice.adapter = selectPriceAdapter


        /*       rvSelectPrice.layoutManager = GridLayoutManager(this, 1)
               rvSelectPrice.adapter = SelectPriceAdapter(this,)*/

        /*   binding.btnApply.setOnClickListener {
               val selectedPrices = adapter.getSelectedFilters()
               Toast.makeText(requireContext(), "Selected: $selectedPrices", Toast.LENGTH_SHORT).show()
           }*/

        backIcon.setOnClickListener { bottomSheetDialog.dismiss() }
        closePopup.setOnClickListener { bottomSheetDialog.dismiss() }

        btnApply.setOnClickListener {
            // 1️⃣ Selected City
            val selectedCityValue = spinnerCity.selectedItem?.toString() ?: ""

            // 2️⃣ Selected Radius
            val selectedRadiusValue = selectedRadius

            // 3️⃣ Selected Prices (from SelectPriceAdapter singleton or callback)
            //  val selectedPriceValue = SelectedFilters.selectedPrices.joinToString(",")

            // 4️⃣ Selected Ratings (from rating checkboxes)
            val selectedRatingsValue = ratingCheckboxes
                .filter { it.isChecked }
                .mapIndexed { index, _ -> index + 1 }  // rating 1..5
                .joinToString(",")

            val intent = Intent(this@ExploreActivity, ExploreActivity::class.java)
            intent.putExtra("selectedCity", selectedProvinceId)
            intent.putExtra("selectedRadius", categoryId)
            intent.putExtra("selectedPrice", selectedPrice)
            intent.putExtra("selectedRatings", selectedRatingsValue)
            intent.putExtra("source", "filter")

            startActivity(intent)
            bottomSheetDialog.dismiss()
        }

        /*
                btnApply.setOnClickListener {
                    val intent = Intent(this@SelectDestinationActivity, ExploreActivity::class.java)
                    intent.putExtra("selectedCity", selectedProvinceId)
                    intent.putExtra("selectedRadius", categoryId)
                    intent.putExtra("selectedPrice", selectedPrice)
                    intent.putExtra("selectedRatings", selectedRatingsValue)
                    intent.putExtra("source", "filter")
                    startActivity(intent)
                    bottomSheetDialog.dismiss()
                }
        */

        bottomSheetDialog.show()
    }


    private fun getCategoryBottomSheetApi() {
        categoryViewModel.getCategory(progressDialog, this)
    }

    private fun provinceListApi() {
        provinceViewModel.provinceListApi(progressDialog, this)
    }

    private fun getCategoryBottomSheetObserver() {
        categoryViewModel.progressIndicator.observe(this) {}

        categoryViewModel.mCategoryResponse.observe(this) { event ->
            val content = event.peekContent()
            val success = content.success
            val message = content.message
            data1 = content.data ?: emptyList()

            val category = content.Datum()

            if (success == true) {
                if (data1.isEmpty()) {
                    rvCategories?.visibility = View.GONE
                } else {
                    rvCategories?.visibility = View.VISIBLE
                    rvCategories?.layoutManager =
                        GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
                    val categoryAdapter = CategoryAdapter(this, data1, this, categoryId)
                    rvCategories?.adapter = categoryAdapter
                }
            } else {
                Toast.makeText(
                    this, message ?: "Failed to load categories",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        categoryViewModel.errorResponse.observe(this) { error ->
            ErrorUtil.handlerGeneralError(this@ExploreActivity, error)
        }
    }


    private fun getFilterObserver() {
        filterActivityViewModel.progressIndicator.observe(this) {

        }
        filterActivityViewModel.filterActivityResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val activityCount = response.peekContent().activityCount
            filterActivity = response.peekContent().data?.data ?: emptyList()

            if (success == true) {
                if (filterActivity.isEmpty()) {
                    binding.destinationRecycler.visibility = View.GONE
                    binding.resultCons.visibility = View.GONE
                    binding.noDataText.visibility = View.VISIBLE
                } else {
                    binding.resultCons.visibility = View.VISIBLE
                    binding.noDataText.visibility = View.GONE
                    binding.destinationRecycler.visibility = View.VISIBLE
                    binding.textView.text = "${activityCount} Results"
                    val limitedList = filterActivity.take(2)
                    binding.destinationRecycler.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    val categoryAdapter = SearchDestinationAdapter(this, limitedList, this)
                    binding.destinationRecycler.adapter = categoryAdapter

                    val remainingList = filterActivity.drop(2)
                    binding.categoriesRecycler.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    val verticalAdapter =
                        SearchVerticleDestinationAdapter(this, remainingList, this)
                    binding.categoriesRecycler.adapter = verticalAdapter

                }
            } else {
                Toast.makeText(this, message ?: "Failed to load categories", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        filterActivityViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@ExploreActivity, it)
        }
    }

    private fun getFilterApi(
        selectedCityId: String,
        selectedCategoryId: String,
        selectedPrice: String,
        selectedRatings: String
    ) {
        val body = FilterActivityBody(
            provinceId = selectedCityId,
            price = selectedPrice,
            rating = selectedRatings,
            categoryId = selectedCategoryId
        )
        filterActivityViewModel.filterActivityBody(progressDialog, this, body)
    }

    private fun getActivityObserver() {
        activityViewModel.progressIndicator.observe(this) {}
        activityViewModel.categoryActivitiesResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            data = response.peekContent().data ?: emptyList()

            if (success == true) {
                if (data.isEmpty()) {
                    binding.destinationRecycler.visibility = View.GONE
                } else {
                    binding.destinationRecycler.visibility = View.VISIBLE
                    val limitedList = data.take(2)
                    binding.destinationRecycler.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    val categoryAdapter = SelectedDestinationAdapter(this, limitedList, this)
                    binding.destinationRecycler.adapter = categoryAdapter

                    val remainingList = data.drop(2)
                    binding.categoriesRecycler.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    val verticalAdapter = ExploreResultAdapter(this, remainingList, this)
                    binding.categoriesRecycler.adapter = verticalAdapter

                }
            } else {
                Toast.makeText(this, message ?: "Failed to load categories", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        activityViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun notificationCountObserver() {
        notificationCountViewModel.progressIndicator.observe(this) {

        }
        notificationCountViewModel.notificationCountResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data
            if (success == true) {
                val count = data ?: 0

                if (count == 0) {
                    binding.notificationBadge.visibility = View.GONE
                } else {
                    binding.notificationBadge.visibility = View.VISIBLE
                    binding.notificationBadge.text = count.toString()
                }

            }

        }
        notificationCountViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun notificationCountApi() {
        notificationCountViewModel.notificationCountApi(this, progressDialog)

    }

    private fun getActivityApi(categoryId: String) {
        val body = ActivityBody(categoryId = categoryId, branchId = "")
        activityViewModel.getActivitiesByCategory(progressDialog, this, body)
    }

    override fun onWishlistClick(product: ActivityResponse.Datum, position: Int) {
        product.isWish = !(product.isWish ?: false)

        val viewHolder = binding.destinationRecycler.findViewHolderForAdapterPosition(position)
                as? SelectedDestinationAdapter.ViewHolder

        viewHolder?.favIcon?.setImageResource(
            if (product.isWish == true) R.drawable.wishlist_color
            else R.drawable.ic_wish
        )
        addToWishlistApi(product.id)

    }

    override fun onWishlistClicked(product: ActivityResponse.Datum, position: Int) {
        product.isWish = !(product.isWish ?: false)

        val viewHolder = binding.categoriesRecycler.findViewHolderForAdapterPosition(position)
                as? ExploreResultAdapter.ViewHolder

        viewHolder?.favIcon?.setImageResource(
            if (product.isWish == true) R.drawable.wishlist_color
            else R.drawable.ic_wish
        )
        addToWishlistApi(product.id)

    }

    private fun addToWishlistApi(id: Int?) {
        val body = AddWishlistBody(activity_id = id.toString())
        addWishlistViewModel.addToWishListApi(progressDialog, this, body)
    }

    override fun onCategoryClick(categoryId: String, categoryName: String) {


    }

}