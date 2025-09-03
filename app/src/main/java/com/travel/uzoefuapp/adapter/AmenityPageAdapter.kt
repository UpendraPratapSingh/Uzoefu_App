package com.travel.uzoefuapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.travel.uzoefuapp.amenityFragment.AmenitiesPageFragment

class AmenityPageAdapter(
    private val fragmentActivity: FragmentActivity,
    private val pages: List<List<String>>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = pages.size

    override fun createFragment(position: Int): Fragment {
        return AmenitiesPageFragment.newInstance(pages[position])
    }
}