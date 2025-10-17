package com.travel.uzoefuapp.bookingActivities

import CustomProgressDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.bookingCompleteModel.BookingDetailBody
import com.travel.uzoefuapp.bookingCompleteModel.BookingDetailViewModel
import com.travel.uzoefuapp.databinding.ActivityBookSummaryBinding
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.notificationModel.NotificationCountViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookSummaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookSummaryBinding
    private val bookingDetailViewModel: BookingDetailViewModel by viewModels()
    private val notificationCountViewModel: NotificationCountViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var bookingId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bookingId = intent.getStringExtra("bookingId").toString()

        bookingDetailApi(bookingId)
        bookingDetailObserver()
        notificationCountApi()
        notificationCountObserver()

        binding.imageView2.setOnClickListener { finish() }

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this@BookSummaryActivity, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.menuIcon.setOnClickListener {
            val intent = Intent(this@BookSummaryActivity, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.downloadSignedIndemnity.setOnClickListener { downloadInvoice() }

        binding.downloadReceipt.setOnClickListener { downloadInvoice1() }

        binding.downloadInvoice.setOnClickListener { downloadInvoice1() }

    }
    private fun notificationCountObserver() {
        notificationCountViewModel.progressIndicator.observe(this) {

        }
        notificationCountViewModel.notificationCountResponse.observe(this) { response ->
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
        notificationCountViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this, it)
        }
    }

    private fun notificationCountApi() {
        notificationCountViewModel.notificationCountApi(this, progressDialog)

    }

    private fun downloadInvoice() {
        val url = "https://uzoefu.co.za/indeminity/form?id=$bookingId"
        val fileName = "invoice.pdf"
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading Invoice")
            .setDescription("Downloading your invoice...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun downloadInvoice1() {
        val url = "https://uzoefu.co.za/invoice/form?id=$bookingId"
        val fileName = "invoice.pdf"
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading Invoice")
            .setDescription("Downloading your invoice...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun bookingDetailObserver() {
        bookingDetailViewModel.progressIndicator.observe(this) {

        }
        bookingDetailViewModel.mCategoryResponse.observe(this) { response ->
            val success = response.peekContent().success
            val data = response.peekContent().data?.bookingDetail

            if (success == true) {
                binding.bookingSummaryLayout.visibility = View.VISIBLE
                binding.activityName.text = data?.activityName
                binding.userName.text = data?.contactName
                binding.selectedDate.text = data?.bookingDate
                binding.refrenceId.text = data?.paymentId
                binding.contactName.text = data?.contactNumber
                binding.email.text = data?.email
                binding.addresss.text = data?.billingAddress
                binding.adultAmount.text = "R${data?.adultAmount}"
                binding.kidsAmount.text = "R${data?.kidsAmount}"
                binding.adultQuantity.text = data?.adultQty.toString()
                binding.kidsQuantity.text = data?.kidsQty.toString()
                binding.adultDescription.text = data?.activityName
                binding.kidsDescription.text = data?.activityName
                binding.subTotal.text = "R${data?.subtotal}"
                binding.totalPrice.text = "R${data?.total}"

            } else {
                binding.bookingSummaryLayout.visibility = View.GONE
            }
        }
        bookingDetailViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@BookSummaryActivity, it)
        }
    }

    private fun bookingDetailApi(bookingId: String?) {
        val body = BookingDetailBody(
            booking_id = bookingId.toString()
        )
        bookingDetailViewModel.bookingDetailApi(progressDialog, this, body)

    }
}