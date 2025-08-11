package com.travel.uzoefuapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.CategoryAdapter
import com.travel.uzoefuapp.adapter.DiscoverAdapter
import com.travel.uzoefuapp.adapter.ExperienceAdapter
import com.travel.uzoefuapp.adapter.ExploreAdapter
import com.travel.uzoefuapp.adapter.SearchAdapter
import com.travel.uzoefuapp.adapter.SearchItem
import com.travel.uzoefuapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.trendingRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.trendingRecyclerview.adapter = DiscoverAdapter(requireContext())

        binding.bestOfferRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.bestOfferRecyclerview.adapter = ExperienceAdapter(requireContext())

        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.adapter = CategoryAdapter(requireContext())

        binding.popularcontryRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.popularcontryRecyclerView.adapter = ExploreAdapter(requireContext())

        binding.filterData.setOnClickListener { showFilterPopup() }

        binding.searchData.setOnClickListener { searchExperience() }

        return binding.root
    }

    @SuppressLint("CutPasteId")
    private fun searchExperience() {
        val bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.FullScreenRoundedBottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.search_bottom_sheet, null)
        bottomSheetDialog.setContentView(view)

        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        val btnClose = view.findViewById<ImageView>(R.id.btnClose)

        // Make it fullscreen
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT

        bottomSheet?.setBackgroundResource(R.drawable.bg_bottom_sheet_rounded)

        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        behavior.isFitToContents = false

        btnClose.setOnClickListener { bottomSheetDialog.dismiss() }

        // RecyclerView setup
        val recycler = view.findViewById<RecyclerView>(R.id.rvResults)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val sampleData = listOf(
            SearchItem(R.drawable.ic_paw, "Magaliesburg Game Reserve", "Wildlife · Magaliesburg"),
            SearchItem(R.drawable.food, "Magaliesburg Eatery", "Food & Cuisine · Magaliesburg"),
            SearchItem(R.drawable.ic_paw, "Magaliesburg Spa", "Food & Cuisine · Magaliesburg"),
            SearchItem(R.drawable.food,"Magaliesburg Sports Club","Food & Cuisine · Magaliesburg"),
            SearchItem(R.drawable.ic_paw, "Magaliesburg Swimming Pool","Food & Cuisine · Magaliesburg")
        )

        // ✅ Show keyboard automatically
        bottomSheetDialog.setOnShowListener {
            etSearch.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT)
        }

        val adapter = SearchAdapter(sampleData)
        recycler.adapter = adapter

        bottomSheetDialog.show()
    }

    @SuppressLint("MissingInflatedId")
    private fun showFilterPopup() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.filter_bottom_sheet, null)
        bottomSheetDialog.setContentView(view)
        // Apply blur to the background
        // Make sure it draws over navigation bar
        bottomSheetDialog.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val btnApply = view.findViewById<Button>(R.id.btnApplyFilters)
        val backIcon = view.findViewById<ImageView>(R.id.imageView)
        val closePopup = view.findViewById<ImageView>(R.id.closePopup)

        backIcon.setOnClickListener { bottomSheetDialog.dismiss() }

        closePopup.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Make fragment draw behind status bar
        requireActivity().window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = android.graphics.Color.TRANSPARENT
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
