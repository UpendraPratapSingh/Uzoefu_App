package com.travel.uzoefuapp.bookingActivities

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.BookingTabAdapter
import com.travel.uzoefuapp.databinding.ActivityBookListBinding
import com.travel.uzoefuapp.globalSettings.SettingsActivity
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

        binding.forYouArrowImg.setOnClickListener { finish() }

        notificationCountApi()
        notificationCountObserver()

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this@BookListActivity, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.menuIcon.setOnClickListener {
            val intent = Intent(this@BookListActivity, SettingsActivity::class.java)
            startActivity(intent)
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

    private fun notificationCountObserver() {
        notificationCountViewModel.progressIndicator.observe(this) {

        }
        notificationCountViewModel.notificationCountResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data
            if (success == true) {
                binding.notificationBadge.text = data.toString()
            }

        }
        notificationCountViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@BookListActivity, it)
        }
    }

    private fun notificationCountApi() {
        notificationCountViewModel.notificationCountApi(this, progressDialog)

    }

}