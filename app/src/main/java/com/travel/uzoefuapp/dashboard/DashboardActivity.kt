package com.travel.uzoefuapp.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.ActivityDashboardBinding
import com.travel.uzoefuapp.fragment.ExploreFragment
import com.travel.uzoefuapp.fragment.HomeFragment
import com.travel.uzoefuapp.fragment.ProfileFragment
import com.travel.uzoefuapp.fragment.WishlistFragment


class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment(), true)

        binding.homeIconUser.setOnClickListener {
            binding.homeIconUser.setImageResource(R.drawable.dashboard)
            binding.destinationIconUsers.setImageResource(R.drawable.explore)
            binding.pastOrderIconUsers.setImageResource(R.drawable.wishlist)
            binding.profileIconUser.setImageResource(R.drawable.profile)

            binding.destinationTxtUSer.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.pastOrderTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.profileTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))

            replaceFragment(HomeFragment(), true)
        }

        binding.destinationFragmentUser.setOnClickListener {
            binding.homeIconUser.setImageResource(R.drawable.dashboard)
            binding.destinationIconUsers.setImageResource(R.drawable.explore)
            binding.pastOrderIconUsers.setImageResource(R.drawable.wishlist)
            binding.profileIconUser.setImageResource(R.drawable.profile)

            binding.destinationTxtUSer.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.pastOrderTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.profileTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))

            replaceFragment(ExploreFragment(), true)
        }

        binding.pastOrderFragmentUser.setOnClickListener {
            binding.homeIconUser.setImageResource(R.drawable.dashboard)
            binding.destinationIconUsers.setImageResource(R.drawable.explore)
            binding.pastOrderIconUsers.setImageResource(R.drawable.wishlist)
            binding.profileIconUser.setImageResource(R.drawable.profile)

            binding.destinationTxtUSer.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.pastOrderTextUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.profileTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))

            replaceFragment(WishlistFragment(), true)
        }

        binding.profileFragmentUser.setOnClickListener {
            binding.homeIconUser.setImageResource(R.drawable.dashboard)
            binding.destinationIconUsers.setImageResource(R.drawable.explore)
            binding.pastOrderIconUsers.setImageResource(R.drawable.wishlist)
            binding.profileIconUser.setImageResource(R.drawable.profile)

            binding.destinationTxtUSer.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.pastOrderTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.profileTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))

            replaceFragment(ProfileFragment(), true)
        }
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.userFrameLayout, fragment)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }
}