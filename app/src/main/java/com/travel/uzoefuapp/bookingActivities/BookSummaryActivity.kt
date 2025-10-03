package com.travel.uzoefuapp.bookingActivities

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
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
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookSummaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookSummaryBinding
    private val bookingDetailViewModel: BookingDetailViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }

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
        val bookingId = intent.getStringExtra("bookingId").toString()


        bookingDetailApi(bookingId)
        bookingDetailObserver()

        binding.imageView2.setOnClickListener { finish() }

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(this@BookSummaryActivity, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.menuIcon.setOnClickListener {
            val intent = Intent(this@BookSummaryActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bookingDetailObserver() {
        bookingDetailViewModel.progressIndicator.observe(this) {

        }
        bookingDetailViewModel.mCategoryResponse.observe(this) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data?.bookingDetail

            if (success == true) {
                binding.bookingSummaryLayout.visibility = View.VISIBLE
                binding.activityName.text = data?.activityName
                binding.selectedDate.text = data?.bookingDate
                binding.refrenceId.text = data?.paymentId
                binding.contactName.text = data?.contactName
                binding.email.text = data?.email
                binding.addresss.text = data?.billingAddress
                binding.adultAmount.text = data?.adultAmount
                binding.kidsAmount.text = data?.kidsAmount
                binding.adultQuantity.text = data?.adultQty.toString()
                binding.kidsQuantity.text = data?.kidsQty.toString()
                binding.adultDescription.text = data?.activityName
                binding.kidsDescription.text = data?.activityName
                binding.subTotal.text = data?.subtotal
                binding.totalPrice.text = data?.total

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