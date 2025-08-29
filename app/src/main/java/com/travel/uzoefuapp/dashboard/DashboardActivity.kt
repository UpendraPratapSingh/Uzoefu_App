package com.travel.uzoefuapp.dashboard

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply only left, right, and bottom padding
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)

            insets
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        selectedHome()

        binding.HomeFragmentUser.setOnClickListener { selectedHome() }

        binding.destinationFragmentUser.setOnClickListener { selectedDestination() }

        binding.pastOrderFragmentUser.setOnClickListener { selectWishlistTab() }

        binding.profileFragmentUser.setOnClickListener { selectedProfile() }
    }

    private fun selectedHome() {
        binding.homeIconUser.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_cyan))
        binding.destinationIconUsers.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
        binding.pastOrderIconUsers.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
        binding.profileIconUser.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))

        binding.destinationTxtUSer.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.nuetral_bold_color
            )
        )
        binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.pastOrderTextUser.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.nuetral_bold_color
            )
        )
        binding.profileTextViewUser.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.nuetral_bold_color
            )
        )

        replaceFragment(HomeFragment(), true)
    }

    fun selectedDestination() {
        binding.destinationIconUsers.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_cyan))
        binding.homeIconUser.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
        binding.pastOrderIconUsers.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
        binding.profileIconUser.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))

        binding.destinationTxtUSer.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
        binding.pastOrderTextUser.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.nuetral_bold_color
            )
        )
        binding.profileTextViewUser.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.nuetral_bold_color
            )
        )

        replaceFragment(ExploreFragment(), true)
    }

    fun selectWishlistTab() {
        // Set Wishlist icon active
        binding.pastOrderIconUsers.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_cyan))
        binding.homeIconUser.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
        binding.destinationIconUsers.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
        binding.profileIconUser.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))

        // Set text colors
        binding.pastOrderTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
        binding.destinationTxtUSer.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.nuetral_bold_color
            )
        )
        binding.profileTextViewUser.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.nuetral_bold_color
            )
        )

        // Replace fragment
        replaceFragment(WishlistFragment(), true)
    }

    private fun selectedProfile() {
        binding.profileIconUser.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_cyan))
        binding.homeIconUser.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
        binding.destinationIconUsers.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
        binding.pastOrderIconUsers.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))

        binding.profileTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))
        binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
        binding.destinationTxtUSer.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.nuetral_bold_color
            )
        )
        binding.pastOrderTextUser.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.nuetral_bold_color
            )
        )

        replaceFragment(ProfileFragment(), true)
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