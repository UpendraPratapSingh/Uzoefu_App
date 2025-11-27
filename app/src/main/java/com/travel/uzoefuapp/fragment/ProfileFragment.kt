package com.travel.uzoefuapp.fragment

import CustomProgressDialog
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.SupportActivity
import com.travel.uzoefuapp.activities.TermAndConditionActivity
import com.travel.uzoefuapp.adapter.TabAdapter
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.databinding.FragmentProfileBinding
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment(private var i: Int) : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        //call api and observer
        notificationCountApi()
        notificationCountObserver()

        binding.menuIcon.setOnClickListener {
            showSettingsBottomSheet()
        }

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        /*   val adapter = TabAdapter(requireActivity())
           binding.viewPager.adapter = adapter
        */
        val adapter = TabAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        binding.viewPager.isUserInputEnabled = false

        // Set default tab based on i
        binding.viewPager.currentItem = when (i) {
            2 -> 2   // Reviews
            1 -> 1   // Profile
            else -> 0 // Overview
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Profile"
                2 -> "Reviews"
                3 -> "Rewards"
                else -> ""
            }
        }.attach()

        return binding.root
    }

    private fun notificationCountObserver() {
        notificationCountViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        notificationCountViewModel.notificationCountResponse.observe(viewLifecycleOwner) { response ->
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
        notificationCountViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showSettingsBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_settings, null)
        bottomSheetDialog.setContentView(view)

        val aboutLayout = view.findViewById<LinearLayout>(R.id.aboutLayout)
        val settingsLayout = view.findViewById<LinearLayout>(R.id.settingsLayout)
        val helpLayout = view.findViewById<LinearLayout>(R.id.helpLayout)
        val feedbackLayout = view.findViewById<LinearLayout>(R.id.feedbackLayout)
        val legalLayout = view.findViewById<LinearLayout>(R.id.legalLayout)
        val referralLayout = view.findViewById<LinearLayout>(R.id.referralLayout)

        aboutLayout.setOnClickListener {
            val intent = Intent(requireContext(), TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "terms")
            startActivity(intent)
            bottomSheetDialog.dismiss()
        }

        settingsLayout.setOnClickListener {
            val intent = Intent(requireContext(), TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "privacy")
            startActivity(intent)
        }

        helpLayout.setOnClickListener {
            val intent = Intent(requireContext(), TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "refund")

            startActivity(intent)
        }

        feedbackLayout.setOnClickListener {
            val intent = Intent(requireContext(), TermAndConditionActivity::class.java)
            intent.putExtra("page_type", "faq")
            startActivity(intent)
        }

        legalLayout.setOnClickListener {
            val intent = Intent(requireContext(), SupportActivity::class.java)
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

    private fun notificationCountApi() {
        notificationCountViewModel.notificationCountApi(requireActivity(), progressDialog)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        notificationCountApi()
    }

}
