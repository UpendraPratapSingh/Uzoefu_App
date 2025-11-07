package com.travel.uzoefuapp.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.travel.uzoefuapp.productFragment.FAQFragment
import com.travel.uzoefuapp.productFragment.InformationFragment
import com.travel.uzoefuapp.productFragment.ProductOverviewFragment
import com.travel.uzoefuapp.productFragment.ProductReviewFragment

class ProductTabAdapter(
    activity: AppCompatActivity,
    private val categoryId: Int,
    private val activeHour: String,
    private val activityName: String
) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = ProductOverviewFragment()
                val bundle = Bundle()
                bundle.putInt("categoryId", categoryId)
                bundle.putString("activeHour", activeHour)
                fragment.arguments = bundle
                fragment
            }

            1 -> {
                val fragment = InformationFragment()
                val bundle = Bundle()
                bundle.putInt("categoryId", categoryId)
                fragment.arguments = bundle
                fragment
            }

            2 -> {
                val fragment = ProductReviewFragment()
                val bundle = Bundle()
                bundle.putInt("categoryId", categoryId)
                bundle.putString("activityName", activityName)
                android.util.Log.e("ProductTabAdapter", "Sending to ProductReviewFragment -> categoryId: $categoryId, activityName: $activityName")

                fragment.arguments = bundle
                fragment
            }

            3 -> {
                val fragment = FAQFragment()
                val bundle = Bundle()
                bundle.putInt("categoryId", categoryId)
                fragment.arguments = bundle
                fragment

            }

            else -> ProductOverviewFragment()
        }
    }
}

