package com.travel.uzoefuapp.notification

import CustomProgressDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.NotificationAdapter
import com.travel.uzoefuapp.adapter.NotificationDeleteListener
import com.travel.uzoefuapp.adapter.NotificationOnClickListener
import com.travel.uzoefuapp.databinding.ActivityNotificationBinding
import com.travel.uzoefuapp.notificationModel.NotificationDeleteBody
import com.travel.uzoefuapp.notificationModel.NotificationDeleteViewModel
import com.travel.uzoefuapp.notificationModel.NotificationListResponse
import com.travel.uzoefuapp.notificationModel.NotificationListViewModel
import com.travel.uzoefuapp.notificationModel.NotificationSeenBody
import com.travel.uzoefuapp.notificationModel.NotificationSeenViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity(), NotificationOnClickListener,
    NotificationDeleteListener {
    private lateinit var binding: ActivityNotificationBinding
    private val notificationListViewModel: NotificationListViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var notifications: List<NotificationListResponse.Datum> = ArrayList()
    private val notificationSeenViewModel: NotificationSeenViewModel by viewModels()
    private val notificationDeleteViewModel: NotificationDeleteViewModel by viewModels()

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
        notificationSeenObserver()
        notificationDeleteObserver()
        notificationAllDeleteObserver()

        binding.clearAllData.setOnClickListener {
            openDeletePopup()
        }

    }

    private fun notificationAllDeleteObserver() {
        notificationDeleteViewModel.progressIndicator.observe(this) {

        }
        notificationDeleteViewModel.notificationDeleteResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                (binding.notificationRecyclerView.adapter as? NotificationAdapter)?.updateList(
                    emptyList()
                )
                notificationListApi()
            }

        }
        notificationDeleteViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@NotificationActivity, it)
        }
    }

    private fun openDeletePopup() {
        val deleteDialog = Dialog(this)
        deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        deleteDialog.setContentView(R.layout.delete_all_notification)
        val noDialog = deleteDialog.findViewById<LinearLayout>(R.id.noDialog)
        val yesDialog = deleteDialog.findViewById<LinearLayout>(R.id.yesDialog)

        val window = deleteDialog.window
        window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        yesDialog.setOnClickListener {
            deleteAllNotification()
            deleteDialog.dismiss()
        }

        noDialog.setOnClickListener {
            deleteDialog.dismiss()
        }
        deleteDialog.show()
    }

    private fun deleteAllNotification() {
        val body = NotificationDeleteBody(
            notificationId = ""
        )
        notificationDeleteViewModel.notificationDeleteApi(this, progressDialog, body)
    }


    private fun notificationDeleteObserver() {
        notificationDeleteViewModel.progressIndicator.observe(this) {

        }
        notificationDeleteViewModel.notificationDeleteResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                notificationListApi()
            }

        }
        notificationDeleteViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@NotificationActivity, it)
        }
    }

    private fun notificationSeenObserver() {
        notificationSeenViewModel.progressIndicator.observe(this) {

        }
        notificationSeenViewModel.notificationSeenResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                notificationListApi()

            }
        }
        notificationSeenViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun notificationListApi() {
        notificationListViewModel.notificationListApi(this, progressDialog)

    }
    private fun notificationListObserver() {
        notificationListViewModel.progressIndicator.observe(this) {

        }
        notificationListViewModel.notificationListResponse.observe(this) { event ->
            val response = event.peekContent()
            val success = response.success
            val message = response.message
            notifications = response.data!!

            Log.e("TAGASA", "notificationListObserver: $success...out..notifications ${notifications.size}")
            if (success == true) {
                Log.e("TAGASA", "notificationListObserver: $success..in..notifications ${notifications.size}")
                if (notifications.isEmpty()) {
                    Log.e("TAG", "notificationListObserver: ")
                    binding.clearAllData.visibility = View.GONE
                    binding.notificationRecyclerView.visibility = View.GONE
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.tvNoData.text = "No notifications available"
                } else {
                    binding.tvNoData.visibility = View.GONE
                    binding.notificationRecyclerView.visibility = View.VISIBLE
                    binding.clearAllData.visibility = View.VISIBLE
                    binding.notificationRecyclerView.layoutManager = GridLayoutManager(this, 1)
                    binding.notificationRecyclerView.adapter = NotificationAdapter(this, notifications, this, this)

                }
            } else {
                binding.tvNoData.visibility = View.VISIBLE
                binding.tvNoData.text = message ?: "Something went wrong!"
            }

        }
        notificationListViewModel.errorResponse.observe(this) {
            Log.e("TAGASA", "notificationListObserver. olk..notifications ${notifications.size}")
            ErrorUtil.handlerGeneralError(this@NotificationActivity, it)
        }
    }



    override fun onNotificationClick(notificationId: String) {

        notificationSeenApi(notificationId)

    }

    private fun notificationSeenApi(notificationId: String) {
        val body = NotificationSeenBody(
            notificationId = notificationId
        )
        notificationSeenViewModel.notificationSeenApi(this, progressDialog, body)
    }

    override fun onDeleteNotification(notification: String) {
        notificationDeleteApi(notification)
    }

    private fun notificationDeleteApi(notification: String) {
        val body = NotificationDeleteBody(
            notificationId = notification,
        )
        notificationDeleteViewModel.notificationDeleteApi(this, progressDialog, body)

    }
}