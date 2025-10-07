package com.travel.uzoefuapp.notification

import CustomProgressDialog
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.NotificationAdapter
import com.travel.uzoefuapp.databinding.ActivityNotificationBinding
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.notificationModel.NotificationListResponse
import com.travel.uzoefuapp.notificationModel.NotificationListViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private val notificationListViewModel: NotificationListViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var notifications: List<NotificationListResponse.Datum> = ArrayList()
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.imageView2.setOnClickListener { finish() }

        notificationListApi()
        notificationListObserver()
        notificationCountApi()
        notificationCountObserver()

    }

    private fun notificationCountObserver() {
        notificationCountViewModel.progressIndicator.observe(this){

        }
        notificationCountViewModel.notificationCountResponse.observe(this){ response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data
            if (success == true){
                binding.notificationBadge.text = data.toString()
            }

        }
        notificationCountViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@NotificationActivity, it)
        }
    }

    private fun notificationCountApi() {
        notificationCountViewModel.notificationCountApi(this, progressDialog)

    }

    private fun notificationListObserver() {
        notificationListViewModel.progressIndicator.observe(this){

        }
        notificationListViewModel.notificationListResponse.observe(this) { event ->
            val response = event.peekContent()
            val success = response.success
            val message = response.message
            val notifications = response.data ?: emptyList()

            if (success == true) {
                if (notifications.isEmpty()) {
                    binding.notificationRecyclerView.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.tvNoData.text = "No notifications available"
                } else {
                    binding.tvNoData.visibility = View.GONE
                    binding.notificationRecyclerView.visibility = View.VISIBLE

                    binding.notificationRecyclerView.layoutManager = GridLayoutManager(this, 1)
                    binding.notificationRecyclerView.adapter = NotificationAdapter(this,notifications)
                }
            } else {
                binding.tvNoData.visibility = View.VISIBLE
                binding.tvNoData.text = message ?: "Something went wrong!"
            }

        }
        notificationListViewModel.errorResponse.observe(this){
            ErrorUtil.handlerGeneralError(this@NotificationActivity, it)
        }

    }

    private fun notificationListApi() {
        notificationListViewModel.notificationListApi(this, progressDialog)

    }
}