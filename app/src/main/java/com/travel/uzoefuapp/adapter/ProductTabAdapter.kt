package com.travel.uzoefuapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.travel.uzoefuapp.productFragment.FAQFragment
import com.travel.uzoefuapp.productFragment.InformationFragment
import com.travel.uzoefuapp.productFragment.ProductOverviewFragment
import com.travel.uzoefuapp.productFragment.ProductReviewFragment

class ProductTabAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProductOverviewFragment()
            1 -> InformationFragment()
            2 -> ProductReviewFragment()
            3 -> FAQFragment()
            else -> ProductOverviewFragment()
        }
    }
}
