package com.travel.uzoefuapp.bookingActivities

import CustomProgressDialog
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.SupportActivity
import com.travel.uzoefuapp.activities.TermAndConditionActivity
import com.travel.uzoefuapp.adapter.BookingTabAdapter
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.databinding.ActivityBookListBinding
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookListActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookListBinding
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.apply {
            decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

            statusBarColor = Color.TRANSPARENT

            WindowInsetsControllerCompat(this, decorView).isAppearanceLightStatusBars = false
        }

        binding.forYouArrowImg.setOnClickListener { finish() }

        notificationCountApi()
        notificationCountObserver()

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this@BookListActivity, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.menuIcon.setOnClickListener {
            showSettingsBottomSheet()
        }

        val adapter = BookingTabAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Active"
                1 -> "Past"
                2 -> "Cancelled"
                else -> ""
            }
        }.attach()
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

    private fun notificationCountObserver() {
        notificationCountViewModel.progressIndicator.observe(this) {

        }
        notificationCountViewModel.notificationCountResponse.observe(this) { response ->
            val success = response.peekContent().success
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
}