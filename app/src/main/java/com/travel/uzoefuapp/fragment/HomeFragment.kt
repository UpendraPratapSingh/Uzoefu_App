package com.travel.uzoefuapp.fragment

import CustomProgressDialog
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistBody
import com.travel.uzoefuapp.AddToWishlistModel.AddWishlistViewModel
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.SearchActivityModel.SearchActivityBody
import com.travel.uzoefuapp.SearchActivityModel.SearchActivityViewModel
import com.travel.uzoefuapp.activities.ExploreActivity
import com.travel.uzoefuapp.activities.ExploreCategoriesActivity
import com.travel.uzoefuapp.activityModl.ActivityBody
import com.travel.uzoefuapp.activityModl.ActivityResponse
import com.travel.uzoefuapp.activityModl.ActivityViewModel
import com.travel.uzoefuapp.adapter.CategoryAdapter
import com.travel.uzoefuapp.adapter.DiscoverAdapter
import com.travel.uzoefuapp.adapter.ExperienceAdapter
import com.travel.uzoefuapp.adapter.ExploreAdapter
import com.travel.uzoefuapp.adapter.OnCategoryClickListener
import com.travel.uzoefuapp.adapter.OnWishlistClickListener
import com.travel.uzoefuapp.adapter.OnWishlistListener
import com.travel.uzoefuapp.adapter.SearchAdapter
import com.travel.uzoefuapp.adapter.SelectPriceAdapter
import com.travel.uzoefuapp.categoryModel.CategoryResponse
import com.travel.uzoefuapp.categoryModel.CategoryViewModel
import com.travel.uzoefuapp.dashboard.DashboardActivity
import com.travel.uzoefuapp.databinding.FragmentHomeBinding
import com.travel.uzoefuapp.discoverDestinationModel.DiscoverDestinationResponse
import com.travel.uzoefuapp.discoverDestinationModel.DiscoverDestinationViewModel
import com.travel.uzoefuapp.provinceModel.ProvinceViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import com.travel.uzoefuapp.branchWishlistModel.BranchWishlistBody
import com.travel.uzoefuapp.branchWishlistModel.BranchWishlistViewModel
import kotlinx.coroutines.Job


@AndroidEntryPoint
class HomeFragment : Fragment(), OnCategoryClickListener, OnWishlistClickListener,
    OnWishlistListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private var rvCategories: RecyclerView? = null
    private lateinit var spinnerCity: AppCompatSpinner
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by viewModels()
    private val addWishlistViewModel: AddWishlistViewModel by viewModels()
    private val provinceViewModel: ProvinceViewModel by viewModels()
    private val discoverDestinationViewModel: DiscoverDestinationViewModel by viewModels()
    var data: List<CategoryResponse.Datum> = ArrayList()
    private var activityList: List<ActivityResponse.Datum> = ArrayList()
    private var discoverList: List<DiscoverDestinationResponse.Datum> = ArrayList()
    private var categoryId = ""
    var selectedCity = ""
    private var selectedProvinceId: String = ""
    private var selectedPrice: String = ""
    private val searchActivityViewModel: SearchActivityViewModel by viewModels()
    private val branchWishlistViewModel: BranchWishlistViewModel by viewModels()
    private var searchJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        discoverDestinationApi()
        getActivityApi()
        getCategoryApi()
        discoverDestinationObserver()
        getActivityObserver()
        getActivityByCategory(categoryId)
        getActivityByCategoryObserver()
        getCategoryObserver()
        activityAddToWishListObserver()
        provinceListObserver()
        branchAddToWishListObserver()

        binding.filterData.setOnClickListener { showFilterPopup() }

        binding.exploreExp.setOnClickListener { searchExperience() }

        binding.viewMoreArrow.setOnClickListener {
            (activity as? DashboardActivity)?.selectedDestination()
        }

        binding.viewMoreArrow1.setOnClickListener {
            val intent = Intent(requireContext(), ExploreActivity::class.java)
            intent.putExtra("ExperienceActivity", "1")
            startActivity(intent)
        }

        binding.rightArrow2.setOnClickListener {
            val intent = Intent(requireContext(), ExploreCategoriesActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    private fun branchAddToWishListObserver() {
        branchWishlistViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        branchWishlistViewModel.branchWishlistResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
        branchWishlistViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun provinceListObserver() {
        provinceViewModel.progressIndicator.observe(viewLifecycleOwner) {}

        provinceViewModel.getTripResponse.observe(viewLifecycleOwner) { event ->
            val response = event.peekContent()
            val success = response.success
            val provinces = response.data ?: emptyList()

            if (success == true) {
                val provinceNames = mutableListOf("Select Province")
                provinceNames.addAll(provinces.mapNotNull { it.name })

                val provinceAdapter = ArrayAdapter(
                    requireContext(),
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

        provinceViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }


    private fun discoverDestinationObserver() {
        discoverDestinationViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        discoverDestinationViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            discoverList = response.peekContent().data ?: emptyList()

            if (success == true) {
                if (discoverList.isEmpty()) {
                    binding.trendingRecyclerview.visibility = View.GONE
                    binding.trendingConst.visibility = View.GONE
                } else {
                    binding.trendingConst.visibility = View.VISIBLE
                    binding.trendingRecyclerview.visibility = View.VISIBLE
                    binding.trendingRecyclerview.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    val categoryAdapter = DiscoverAdapter(requireContext(), discoverList, this)
                    binding.trendingRecyclerview.adapter = categoryAdapter
                }
            } else {

            }
        }
        discoverDestinationViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }

    private fun discoverDestinationApi() {
        discoverDestinationViewModel.discoverDestinationApi(progressDialog, requireActivity())
    }

    private fun activityAddToWishListObserver() {
        addWishlistViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        addWishlistViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            if (success == true) {
                Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
            }
        }
        addWishlistViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getActivityByCategoryObserver() {
        activityViewModel.progressIndicator.observe(viewLifecycleOwner) {}

        activityViewModel.categoryActivitiesResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            val categoryActivityList = response.peekContent().data ?: emptyList()

            if (success == true) {
                if (categoryActivityList.isEmpty()) {
                    binding.popularcontryRecyclerView.visibility = View.GONE
                } else {
                    binding.popularcontryRecyclerView.visibility = View.VISIBLE
                    binding.popularcontryRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    val categoryAdapter =
                        ExploreAdapter(requireContext(), categoryActivityList, this)
                    binding.popularcontryRecyclerView.adapter = categoryAdapter
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    message ?: "Failed to load categories",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        activityViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getActivityObserver() {
        activityViewModel.progressIndicator.observe(viewLifecycleOwner) {}
        activityViewModel.allActivitiesResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            activityList = response.peekContent().data ?: emptyList()

            if (success == true) {
                if (activityList.isEmpty()) {
                    binding.bestOfferRecyclerview.visibility = View.GONE
                    binding.popularCountryConst.visibility = View.GONE
                } else {
                    binding.popularCountryConst.visibility = View.VISIBLE
                    binding.bestOfferRecyclerview.visibility = View.VISIBLE
                    binding.bestOfferRecyclerview.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    val categoryAdapter = ExperienceAdapter(requireContext(), activityList, this)
                    binding.bestOfferRecyclerview.adapter = categoryAdapter
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    message ?: "Failed to load categories",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        activityViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getActivityApi() {
        val body = ActivityBody(categoryId = "", branchId = "")
        activityViewModel.getAllActivities(progressDialog, requireActivity(), body)
    }

    private fun getCategoryObserver() {
        categoryViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }

        categoryViewModel.mCategoryResponse.observe(viewLifecycleOwner) { event ->
            val content = event.peekContent()
            val success = content.success
            val message = content.message
            data = content.data ?: emptyList()

            if (success == true) {
                if (data.isEmpty()) {
                    binding.categoriesRecyclerView.visibility = View.GONE
                } else {
                    binding.categoriesRecyclerView.visibility = View.VISIBLE
                    binding.categoriesRecyclerView.layoutManager =
                        GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
                    val categoryAdapter = CategoryAdapter(requireContext(), data, this, categoryId)
                    binding.categoriesRecyclerView.adapter = categoryAdapter
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    message ?: "Failed to load categories",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        categoryViewModel.errorResponse.observe(viewLifecycleOwner) { error ->
            ErrorUtil.handlerGeneralError(requireContext(), error)
        }
    }

    private fun getCategoryApi() {
        categoryViewModel.getCategory(progressDialog, requireActivity())
    }

    private fun searchExperience() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.search_bottom_sheet, null)
        bottomSheetDialog.setContentView(view)

        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        val btnClose = view.findViewById<ImageView>(R.id.btnClose)
        val recycler = view.findViewById<RecyclerView>(R.id.rvResults)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SearchAdapter(requireContext(), mutableListOf())
        recycler.adapter = adapter

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheet?.let {
            val params = it.layoutParams as ViewGroup.MarginLayoutParams
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            params.topMargin = (30 * resources.displayMetrics.density).toInt()
            it.layoutParams = params
            it.setBackgroundResource(R.drawable.bg_bottom_sheet_rounded)
        }

        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        behavior.isFitToContents = false

        btnClose.setOnClickListener { bottomSheetDialog.dismiss() }

        // Keep keyboard always open
        etSearch.requestFocus()
        etSearch.post {
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT)
        }

        // API search with debounce
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                searchJob?.cancel()

                searchJob = lifecycleScope.launch {
                    delay(400) // debounce


                    val body = SearchActivityBody(activityName = query)
                    searchActivityViewModel.searchActivityApi(requireActivity(), body)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Observe API response
        searchActivityViewModel.searchActivityResponse.observe(viewLifecycleOwner) { event ->
            val response = event.peekContent()
            val success = response.status
            val data = response.data ?: emptyList()

            adapter.updateData(if (success == true) data else emptyList())
        }

        bottomSheetDialog.show()
    }


    @SuppressLint("MissingInflatedId")
    private fun showFilterPopup() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
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

        //call api province
        provinceListApi()

        getCategoryBottomSheetApi()
        getCategoryBottomSheetObserver()

        val radiusOptions = arrayOf(
            "Select Radius",
            "2 Kilometres",
            "5 Kilometres",
            "10 Kilometres",
            "20 Kilometres"
        )

        val radiusAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            radiusOptions
        )
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

        /*
                rvCategories.layoutManager = GridLayoutManager(requireContext(), 3)
                rvCategories.adapter = CategoryAdapter(requireContext(), categoriesList)
        */
        val priceRanges = listOf("0 - 150", "151 - 300", "301 - 500", "500+")

        val selectPriceAdapter =
            SelectPriceAdapter(requireContext(), priceRanges) { selectedPrices ->
                // This lambda is called whenever selection changes
                // Send selectedPrices to your API
                selectedPrice = selectedPrices.toString()
            }
        rvSelectPrice.layoutManager = GridLayoutManager(requireContext(), 1)
        rvSelectPrice.adapter = selectPriceAdapter


        /*
                rvSelectPrice.adapter = SelectPriceAdapter(requireContext())
        */

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

            // Send via Intent
            val intent = Intent(requireContext(), ExploreActivity::class.java)
            intent.putExtra("selectedCity", selectedProvinceId)
            intent.putExtra("selectedRadius", categoryId)
            intent.putExtra("selectedPrice", selectedPrice)
            intent.putExtra("selectedRatings", selectedRatingsValue)
            intent.putExtra("source", "filter")

            startActivity(intent)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    /*    private fun filterActivityApi() {
            val body = FilterActivityBody(
                provinceId = selectedProvinceId,
                price = selectedPrice,
                rating = ,
                categoryId = categoryId
            )

        }*/

    private fun provinceListApi() {
        provinceViewModel.provinceListApi(progressDialog, requireActivity())
    }

    private fun getCategoryBottomSheetObserver() {
        categoryViewModel.progressIndicator.observe(viewLifecycleOwner) {}

        categoryViewModel.mCategoryResponse.observe(viewLifecycleOwner) { event ->
            val content = event.peekContent()
            val success = content.success
            val message = content.message
            data = content.data ?: emptyList()

            val category = content.Datum()

            if (success == true) {
                if (data.isEmpty()) {
                    rvCategories?.visibility = View.GONE
                } else {
                    rvCategories?.visibility = View.VISIBLE
                    rvCategories?.layoutManager =
                        GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
                    val categoryAdapter = CategoryAdapter(requireContext(), data, this, categoryId)
                    rvCategories?.adapter = categoryAdapter
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    message ?: "Failed to load categories",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        categoryViewModel.errorResponse.observe(viewLifecycleOwner) { error ->
            ErrorUtil.handlerGeneralError(requireContext(), error)
        }
    }

    private fun getCategoryBottomSheetApi() {
        categoryViewModel.getCategory(progressDialog, requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getCategoryApi()
        getActivityApi()
        getActivityByCategory(categoryId)

    }

    override fun onCategoryClick(categoryId: String, categoryName: String) {
        this.categoryId = categoryId
        getActivityByCategory(categoryId)
    }

    private fun getActivityByCategory(categoryId: String) {
        val body = ActivityBody(categoryId = categoryId, branchId = "")
        activityViewModel.getActivitiesByCategory(progressDialog, requireActivity(), body)
    }

    override fun onWishlistClicked(product: ActivityResponse.Datum, position: Int) {
        product.isWish = !(product.isWish ?: false)

        val viewHolder = binding.bestOfferRecyclerview.findViewHolderForAdapterPosition(position)
                as? ExperienceAdapter.ViewHolder

        viewHolder?.favIcon?.setImageResource(
            if (product.isWish == true) R.drawable.wishlist_color
            else R.drawable.ic_wish
        )
        addToWishlistApi(product.id)

        /*       val viewHolderCat =
                   binding.popularcontryRecyclerView.findViewHolderForAdapterPosition(position)
                           as? ExperienceAdapter.ViewHolder

               viewHolderCat?.favIcon?.setImageResource(
                   if (product.isWish == true) R.drawable.wishlist_color
                   else R.drawable.ic_wish
               )
               addToWishlistApi(product.id)*/

    }

    override fun onWishlistDestinationClicked(
        product: DiscoverDestinationResponse.Datum,
        position: Int
    ) {
        product.iswish = !(product.iswish ?: false)

        val viewHolder = binding.trendingRecyclerview.findViewHolderForAdapterPosition(position)
                as? DiscoverAdapter.ViewHolder

        viewHolder?.favIcon?.setImageResource(
            if (product.iswish == true) R.drawable.wishlist_color
            else R.drawable.ic_wish
        )
        branchAddToWishlistApi(product.branchId)

    }

    private fun branchAddToWishlistApi(id: Int?) {
        val body = BranchWishlistBody(
            branchId = id.toString()
        )
        branchWishlistViewModel.addBranchWishlistApi(requireActivity(), progressDialog, body)

    }

    private fun addToWishlistApi(id: Int?) {
        val body = AddWishlistBody(activity_id = id.toString())
        addWishlistViewModel.addToWishListApi(progressDialog, requireActivity(), body)
    }

    override fun onWishlistClick(product: ActivityResponse.Datum, position: Int) {
        product.isWish = !(product.isWish ?: false)

        val viewHolder =
            binding.popularcontryRecyclerView.findViewHolderForAdapterPosition(position)
                    as? ExploreAdapter.ViewHolder

        viewHolder?.favIcon?.setImageResource(
            if (product.isWish == true) R.drawable.wishlist_color
            else R.drawable.ic_wish
        )
        addToWishlistApi(product.id)

    }
}
