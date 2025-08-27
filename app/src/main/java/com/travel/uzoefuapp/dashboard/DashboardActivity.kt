package com.travel.uzoefuapp.dashboard

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
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
        /*enableEdgeToEdge()

        // Handle insets for bottom nav
        ViewCompat.setOnApplyWindowInsetsListener(binding.limeee) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // add bottom padding so nav bar sits above gesture area
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                systemBars.bottom
            )

            insets
        }*/

        // Status bar aur navigation bar ko transparent kar do
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        replaceFragment(HomeFragment(), true)

        binding.HomeFragmentUser.setOnClickListener {
           // binding.homeIconUser.setImageResource(R.drawable.dashboard)
            binding.homeIconUser.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_cyan))
         /*   binding.destinationIconUsers.setImageResource(R.drawable.explore)
            binding.pastOrderIconUsers.setImageResource(R.drawable.wishlist)
            binding.profileIconUser.setImageResource(R.drawable.profile)*/
            binding.destinationIconUsers.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.pastOrderIconUsers.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.profileIconUser.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))

            binding.destinationTxtUSer.setTextColor(ContextCompat.getColor(this,R.color.nuetral_bold_color))
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.pastOrderTextUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.profileTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))

            replaceFragment(HomeFragment(), true)

        }

        binding.destinationFragmentUser.setOnClickListener {
           // binding.homeIconUser.setImageResource(R.drawable.dashboard)
          //  binding.destinationIconUsers.setImageResource(R.drawable.explore)
            binding.destinationIconUsers.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_cyan))

           // binding.pastOrderIconUsers.setImageResource(R.drawable.wishlist)
          //  binding.profileIconUser.setImageResource(R.drawable.profile)

            binding.homeIconUser.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.pastOrderIconUsers.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.profileIconUser.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))

            binding.destinationTxtUSer.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.pastOrderTextUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.profileTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))

            replaceFragment(ExploreFragment(), true)
        }

        binding.pastOrderFragmentUser.setOnClickListener {
          //  binding.homeIconUser.setImageResource(R.drawable.dashboard)
          //  binding.destinationIconUsers.setImageResource(R.drawable.explore)
           // binding.pastOrderIconUsers.setImageResource(R.drawable.wishlist)
            binding.pastOrderIconUsers.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_cyan))
          //  binding.profileIconUser.setImageResource(R.drawable.profile)

            binding.homeIconUser.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.destinationIconUsers.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.profileIconUser.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))

            binding.destinationTxtUSer.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.pastOrderTextUser.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.profileTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))

            replaceFragment(WishlistFragment(), true)
        }

        binding.profileFragmentUser.setOnClickListener {
           // binding.homeIconUser.setImageResource(R.drawable.dashboard)
           // binding.destinationIconUsers.setImageResource(R.drawable.explore)
           // binding.pastOrderIconUsers.setImageResource(R.drawable.wishlist)
           // binding.profileIconUser.setImageResource(R.drawable.profile)
            binding.profileIconUser.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dark_cyan))

            binding.homeIconUser.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.destinationIconUsers.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.pastOrderIconUsers.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.nuetral_bold_color))

            binding.destinationTxtUSer.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.homeTextUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.pastOrderTextUser.setTextColor(ContextCompat.getColor(this, R.color.nuetral_bold_color))
            binding.profileTextViewUser.setTextColor(ContextCompat.getColor(this, R.color.black))

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