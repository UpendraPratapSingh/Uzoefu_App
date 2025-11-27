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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
import com.travel.uzoefuapp.adapter.ExploreResultAdapter
import com.travel.uzoefuapp.adapter.FilterCategoryAdapter
import com.travel.uzoefuapp.adapter.OnCategoryClickListener
import com.travel.uzoefuapp.adapter.OnWishlistListener
import com.travel.uzoefuapp.adapter.SelectPriceAdapter
import com.travel.uzoefuapp.adapter.SelectedDestinationAdapter
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.categoryModel.CategoryResponse
import com.travel.uzoefuapp.categoryModel.CategoryViewModel
import com.travel.uzoefuapp.databinding.ActivitySelectDestinationBinding
import com.travel.uzoefuapp.discoverDestinationModel.DestinationDetailResponse
import com.travel.uzoefuapp.discoverDestinationModel.DestinationDetailViewModel
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.provinceModel.ProvinceViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectDestinationActivity : AppCompatActivity(), OnWishlistListener, OnCategoryClickListener {
    lateinit var binding: ActivitySelectDestinationBinding
    private val activityViewModel: ActivityViewModel by viewModels()
    var data: List<ActivityResponse.Datum> = ArrayList()
    var data1: List<CategoryResponse.Datum> = ArrayList()
    private var selectedPrice: String = ""
    var categoryId = ""
    private var rvCategories: RecyclerView? = null
    private var selectedProvinceId: String = ""
    private lateinit var spinnerCity: AppCompatSpinner
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private val addWishlistViewModel: AddWishlistViewModel by viewModels()
    private val destinationDetailViewModel: DestinationDetailViewModel by viewModels()
    private var destinationDetail: List<DestinationDetailResponse.Datum> = ArrayList()
    private val provinceViewModel: ProvinceViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()
    private var selectCategory = ""
    private lateinit var categoryAdapter: FilterCategoryAdapter
    private var isAllSelected = false


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

        window.apply {
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )

            statusBarColor = Color.TRANSPARENT

            WindowInsetsControllerCompat(this, decorView).isAppearanceLightStatusBars = false
        }

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.menuIcon.setOnClickListener {
            showSettingsBottomSheet()
        }

        val categoryId = intent.getStringExtra("categoryId") ?: ""
        val branchId = intent.getStringExtra("branchId") ?: ""

        //observer
        getActivityApi(categoryId, branchId)
        getActivityObserver()
        activityAddToWishListObserver()
        provinceListObserver()
        notificationCountApi()
        notificationCountObserver()

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

        if (type == "1") {
            binding.textView.text = name
        } else {
            binding.textView.text = categoryName ?: ""
        }
        binding.filterData.setOnClickListener { showFilterPopup() }
    }

    @SuppressLint("MissingInflatedId")
    private fun showSettingsBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_settings, null)
        bottomSheetDialog.setContentView(view)

        val aboutLayout = view.findViewById<LinearLayout>(R.id.aboutLayout)
        val settingsLayout = view.findViewById<LinearLayout>(R.id.settingsLayout)
        val helpLayout = view.findViewById<LinearLayout>(R.id.helpLayout)
        val feedbackLayout = view.findViewById<LinearLayout>(R.id.feedbackLayout)
        val legalLayout = view.findViewById<LinearLayout>(R.id.legalLayout)
        val referralLayout = view.findViewById<LinearLayout>(R.id.referralLayout)

        aboutLayout.setOnClickListener {
            val intent = Intent(this, TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "terms")
            startActivity(intent)
            bottomSheetDialog.dismiss()
        }

        settingsLayout.setOnClickListener {
            val intent = Intent(this, TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "privacy")
            startActivity(intent)
        }

        helpLayout.setOnClickListener {
            val intent = Intent(this, TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "refund")

            startActivity(intent)
        }

        feedbackLayout.setOnClickListener {
            val intent = Intent(this, TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "faq")
            startActivity(intent)
        }

        legalLayout.setOnClickListener {
            val intent = Intent(this, SupportActivity::class.java)
            startActivity(intent)
        }

        referralLayout.setOnClickListener {
            val referCode = Uzoefu.encryptedPrefs.statusDone
            //val referLink = "https://yourapp.com/referral?code=$referCode"
            val referLink = "https://uzoefu.co.za/reward/$referCode"

            Log.e("referralCode", "showSettingsBottomSheetAAAAAAAAAAAA $referCode")

            val shareMessage = """
                
        🎉✨ **Exclusive Offer Just for You!** ✨🎉
        
        Hey there! I’ve been using **Uzoefu App**, and it’s been an amazing experience.  
        You can now join too — and guess what? You’ll get **₹150 bonus** just for signing up! 💰
        
        🔹 Here’s how it works:
        1️⃣ Click on the link below to download or open the app  
        2️⃣ Sign up using my referral code: **$referCode**  
        3️⃣ You’ll instantly receive your reward once you complete your first activity! 🚀
        
        💡 **Why you’ll love Uzoefu App:**
        - Easy and secure to use  
        - Exciting rewards for every action  
        - Trusted by thousands of happy users  
        - Quick payouts and referral bonuses  

        👉 Tap the link now to get started:  
        $referLink

        🌟 Don’t miss this chance — invite your friends and earn together! 🌟
        
        — Sent via Uzoefu ❤️
        
    """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Invite & Earn ₹150 with Uzoefu App")
                putExtra(Intent.EXTRA_TEXT, shareMessage)
            }

            startActivity(Intent.createChooser(intent, "Share via"))
        }
        bottomSheetDialog.show()
    }

    private fun provinceListObserver() {
        provinceViewModel.progressIndicator.observe(this) {}

        provinceViewModel.getTripResponse.observe(this) { event ->
            val response = event.peekContent()
            val success = response.success
            val provinces = response.data ?: emptyList()

            if (success == true) {
                val provinceNames = mutableListOf("Select Province")
                provinceNames.addAll(provinces.mapNotNull { it.name })

                val provinceAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    provinceNames
                )
                spinnerCity.adapter = provinceAdapter

                spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (position > 0) {
                            // Get selected province ID
                            selectedProvinceId = provinces[position - 1].id.toString()

                            // 🔹 Fetch cities for this province

                        } else {
                            selectedProvinceId = ""
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        selectedProvinceId = ""
                    }
                }
            }
        }

        provinceViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun activityAddToWishListObserver() {
        addWishlistViewModel.progressIndicator.observe(this) {

        }
        addWishlistViewModel.mCategoryResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            if (success == true) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        addWishlistViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun getActivityObserver() {
        activityViewModel.progressIndicator.observe(this) {

        }
        activityViewModel.categoryActivitiesResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            data = response.peekContent().data ?: emptyList()

            if (success == true) {
                if (data.isEmpty()) {
                    binding.destinationRecycler.visibility = View.GONE
                    binding.noDataText.visibility = View.VISIBLE
                } else {
                    binding.noDataText.visibility = View.GONE
                    binding.destinationRecycler.visibility = View.VISIBLE
                    val limitedList = data.take(2)
                    binding.destinationRecycler.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    val categoryAdapter = SelectedDestinationAdapter(this, limitedList, this)
                    binding.destinationRecycler.adapter = categoryAdapter

                    // Vertical
                    val remainingList = data.drop(2)
                    binding.destinationRecyclerView.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    val verticalAdapter = ExploreResultAdapter(this, remainingList, this)
                    binding.destinationRecyclerView.adapter = verticalAdapter

                }
            } else {
                Toast.makeText(this, message ?: "Failed to load categories", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        activityViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@SelectDestinationActivity, it)
        }
    }

    private fun getActivityApi(categoryId: String, branchId: String) {
        val body = ActivityBody(
            categoryId = categoryId,
            branchId = branchId
        )
        activityViewModel.getActivitiesByCategory(progressDialog, this, body)

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

        /*        val cbAllRatings = view.findViewById<CheckBox>(R.id.cbAllRatings)
                val cbRating1 = view.findViewById<CheckBox>(R.id.cbRating1)
                val cbRating2 = view.findViewById<CheckBox>(R.id.cbRating2)
                val cbRating3 = view.findViewById<CheckBox>(R.id.cbRating3)
                val cbRating4 = view.findViewById<CheckBox>(R.id.cbRating4)
                val cbRating5 = view.findViewById<CheckBox>(R.id.cbRating5)

                val ratingCheckboxes = listOf(cbRating1, cbRating2, cbRating3, cbRating4, cbRating5)

                val selectedRatings = mutableSetOf<Int>()

                ratingCheckboxes.forEachIndexed { index, checkbox ->
                    checkbox.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            selectedRatings.add(index + 1)
                        } else {
                            selectedRatings.remove(index + 1)
                        }

                        cbAllRatings.isChecked = selectedRatings.size == ratingCheckboxes.size
                    }
                }

                cbAllRatings.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        ratingCheckboxes.forEach { it.isChecked = true }
                        selectedRatings.clear()
                        selectedRatings.addAll(1..5)
                    } else {
                        ratingCheckboxes.forEach { it.isChecked = false }
                        selectedRatings.clear()
                    }
                }
                */

        var isUpdating = false // class-level variable

        val cbAllRatings = view.findViewById<CheckBox>(R.id.cbAllRatings)
        val cbRating1 = view.findViewById<CheckBox>(R.id.cbRating1)
        val cbRating2 = view.findViewById<CheckBox>(R.id.cbRating2)
        val cbRating3 = view.findViewById<CheckBox>(R.id.cbRating3)
        val cbRating4 = view.findViewById<CheckBox>(R.id.cbRating4)
        val cbRating5 = view.findViewById<CheckBox>(R.id.cbRating5)

        val ratingCheckboxes = listOf(cbRating1, cbRating2, cbRating3, cbRating4, cbRating5)
        val selectedRatings = mutableSetOf<Int>()

        ratingCheckboxes.forEachIndexed { index, checkbox ->
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isUpdating) return@setOnCheckedChangeListener

                if (isChecked) {
                    selectedRatings.add(index + 1)
                } else {
                    selectedRatings.remove(index + 1)
                }

                // agar sab select ho gaye to "All Ratings" bhi check ho
                isUpdating = true
                cbAllRatings.isChecked = selectedRatings.size == ratingCheckboxes.size
                isUpdating = false
            }
        }

        cbAllRatings.setOnCheckedChangeListener { _, isChecked ->
            if (isUpdating) return@setOnCheckedChangeListener

            isUpdating = true
            if (isChecked) {
                ratingCheckboxes.forEach { it.isChecked = true }
                selectedRatings.clear()
                selectedRatings.addAll(listOf(1, 2, 3, 4, 5))
            } else {
                ratingCheckboxes.forEach { it.isChecked = false }
                selectedRatings.clear()
            }
            isUpdating = false
        }


        /*      cbAllRatings.setOnCheckedChangeListener { _, isChecked ->
                  ratingCheckboxes.forEach { it.isChecked = isChecked }
              }

              ratingCheckboxes.forEach { cb ->
                  cb.setOnCheckedChangeListener { _, _ ->
                      cbAllRatings.isChecked = ratingCheckboxes.all { it.isChecked }
                  }
              }*/

        provinceListApi()

        getCategoryBottomSheetApi()
        getCategoryBottomSheetObserver()

        val radiusOptions = arrayOf(
            "Select Radius", "2 Km", "5 Km", "10 Km", "20 Km"
        )

        val radiusAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, radiusOptions)
        spinnerRadius.adapter = radiusAdapter

        val tvSelectAll = view.findViewById<TextView>(R.id.tvSelectAll)

        tvSelectAll?.setOnClickListener {
            if (isAllSelected) {
                categoryAdapter.clearAll()
                tvSelectAll.text = "Select All"
            } else {
                categoryAdapter.selectAll()
                tvSelectAll.text = "Clear All"
            }
            isAllSelected = !isAllSelected
        }


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

            //   selectedPrice = selectedPrices.toString()
            selectedPrice =
                selectedPrices.joinToString(",") { it.replace(" ", "").replace("-", "-") }

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
            val selectedCityValue = spinnerCity.selectedItem?.toString() ?: ""

            val selectedRadiusValue = selectedRadius

            // 3️⃣ Selected Prices (from SelectPriceAdapter singleton or callback)
            val selectedRatingsValue = selectedRatings.joinToString(",")

            /*     // 4️⃣ Selected Ratings (from rating checkboxes)
                 val selectedRatingsValue = ratingCheckboxes
                     .filter { it.isChecked }
                     .mapIndexed { index, _ -> index + 1 }
                     .joinToString(",")
     */

            /*
                        val selectedRatingsValue = ratingCheckboxes
                            .mapIndexedNotNull { index, checkbox ->
                                if (checkbox.isChecked) index + 1 else null
                            }
                            .maxOrNull()
                            ?.toString() ?: ""
            */

            val intent = Intent(this@SelectDestinationActivity, ExploreActivity::class.java)
            intent.putExtra("selectedCity", selectedProvinceId)
            intent.putExtra("selectedCategory", selectCategory)
            intent.putExtra("selectedPrice", selectedPrice)
            intent.putExtra("selectedRatings", selectedRatingsValue)
            intent.putExtra("selectedRadius", selectedRadius)
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

    /*
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
                ErrorUtil.handlerGeneralError(this@SelectDestinationActivity, error)
            }
        }
    */

    private fun getCategoryBottomSheetObserver() {
        categoryViewModel.progressIndicator.observe(this) { isLoading ->
            // Optional: show or hide progress bar
            // progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        categoryViewModel.mCategoryResponse.observe(this) { event ->
            val content = event.peekContent()
            val success = content.success
            val message = content.message
            val data = content.data ?: emptyList<CategoryResponse.Datum>()

            if (success == true) {
                if (data.isEmpty()) {
                    rvCategories?.visibility = View.GONE
                } else {
                    rvCategories?.visibility = View.VISIBLE

                    rvCategories?.layoutManager = GridLayoutManager(
                        this,
                        3,
                        GridLayoutManager.VERTICAL,
                        false
                    )

                    // Initialize adapter
                    categoryAdapter = FilterCategoryAdapter(
                        this,
                        data,
                        object : FilterCategoryAdapter.OnCategoryClickListener {
                            override fun onCategoryClick(
                                selectedIds: List<Int>,
                                selectedNames: List<String>
                            ) {
                                // Convert selected IDs into a comma-separated string (e.g. "1,2,3,4,5")
                                selectCategory = selectedIds.joinToString(",")

                                // Optional: If you also want names
                                val selectedNamesString = selectedNames.joinToString(", ")

                                Log.d("SelectedCategories", "IDs: $selectCategory")
                                Log.d("SelectedCategories", "Names: $selectedNamesString")
                            }
                        }
                    )

                    rvCategories?.adapter = categoryAdapter

                }
            } else {
                Toast.makeText(this, message ?: "Failed to load categories", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        categoryViewModel.errorResponse.observe(this) { error ->
            ErrorUtil.handlerGeneralError(this@SelectDestinationActivity, error)
        }
    }


    private fun getCategoryBottomSheetApi() {
        categoryViewModel.getCategory(progressDialog, this)
    }

    private fun provinceListApi() {
        provinceViewModel.provinceListApi(progressDialog, this)
    }

    override fun onWishlistClick(product: ActivityResponse.Datum, position: Int) {
        product.isWish = !(product.isWish ?: false)

        val viewHolder =
            binding.destinationRecycler.findViewHolderForAdapterPosition(position) as? SelectedDestinationAdapter.ViewHolder

        viewHolder?.favIcon?.setImageResource(
            if (product.isWish == true) R.drawable.wishlist_color
            else R.drawable.ic_wish
        )
        addToWishlistApi(product.id)

    }

    override fun onWishlistClicked(product: ActivityResponse.Datum, position: Int) {
        product.isWish = !(product.isWish ?: false)

        val viewHolder =
            binding.destinationRecyclerView.findViewHolderForAdapterPosition(position) as? ExploreResultAdapter.ViewHolder

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
        this.categoryId = categoryId
    }

    override fun onResume() {
        super.onResume()
        categoryId = ""
        selectedProvinceId = ""
    }

}